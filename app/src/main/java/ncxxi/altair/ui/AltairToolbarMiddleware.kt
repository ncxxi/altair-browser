package ncxxi.altair.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mozilla.components.browser.state.selector.selectedTab
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.compose.browser.toolbar.concept.PageOrigin
import mozilla.components.compose.browser.toolbar.store.BrowserDisplayToolbarAction.PageOriginUpdated
import mozilla.components.compose.browser.toolbar.store.BrowserDisplayToolbarAction.UpdateProgressBarConfig
import mozilla.components.compose.browser.toolbar.store.BrowserEditToolbarAction.SearchQueryUpdated
import mozilla.components.compose.browser.toolbar.store.BrowserToolbarAction
import mozilla.components.compose.browser.toolbar.store.BrowserToolbarAction.CommitUrl
import mozilla.components.compose.browser.toolbar.store.BrowserToolbarAction.EnterEditMode
import mozilla.components.compose.browser.toolbar.store.BrowserToolbarAction.ExitEditMode
import mozilla.components.compose.browser.toolbar.store.BrowserToolbarAction.Init
import mozilla.components.compose.browser.toolbar.store.BrowserToolbarInteraction.BrowserToolbarEvent
import mozilla.components.compose.browser.toolbar.store.BrowserToolbarState
import mozilla.components.compose.browser.toolbar.store.ProgressBarConfig
import mozilla.components.compose.browser.toolbar.ui.BrowserToolbarQuery
import mozilla.components.feature.session.SessionUseCases
import mozilla.components.lib.state.Middleware
import mozilla.components.lib.state.Store
import mozilla.components.lib.state.ext.flow
import ncxxi.altair.util.normalizeUrl
import mozilla.components.compose.browser.toolbar.R as ToolbarR

class AltairToolbarMiddleware(
    private val browserStore: BrowserStore,
    private val sessionUseCases: SessionUseCases,
    private val scope: CoroutineScope,
) : Middleware<BrowserToolbarState, BrowserToolbarAction> {

    override fun invoke(
        store: Store<BrowserToolbarState, BrowserToolbarAction>,
        next: (BrowserToolbarAction) -> Unit,
        action: BrowserToolbarAction,
    ) {
        when (action) {
            is Init -> {
                next(action)
                observeBrowserState(store)
            }
            is CommitUrl -> {
                next(action)
                val url = normalizeUrl(action.text)
                sessionUseCases.loadUrl(url)
                store.dispatch(ExitEditMode)
            }
            is BrowserToolbarEvent -> {
                val currentUrl = browserStore.state.selectedTab?.content?.url.orEmpty()
                store.dispatch(SearchQueryUpdated(BrowserToolbarQuery(currentUrl), isQueryPrefilled = true))
                store.dispatch(EnterEditMode(isPrivate = false))
                next(action)
            }
            else -> next(action)
        }
    }

    private fun observeBrowserState(
        store: Store<BrowserToolbarState, BrowserToolbarAction>,
    ) {
        scope.launch {
            browserStore.flow()
                .map { it.selectedTab?.content }
                .distinctUntilChanged()
                .collect { content ->
                    if (content == null) return@collect
                    store.dispatch(
                        PageOriginUpdated(
                            PageOrigin(
                                hint = ToolbarR.string.mozac_browser_toolbar_search_hint,
                                title = content.title.ifEmpty { null },
                                url = content.url,
                                onClick = object : BrowserToolbarEvent {},
                            ),
                        ),
                    )
                    store.dispatch(
                        UpdateProgressBarConfig(
                            if (content.loading) {
                                ProgressBarConfig(progress = content.progress)
                            } else {
                                null
                            },
                        ),
                    )
                }
        }
    }
}
