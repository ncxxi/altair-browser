package ncxxi.altair.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import mozilla.components.browser.state.action.TabListAction
import mozilla.components.browser.state.state.createTab
import mozilla.components.compose.browser.toolbar.BrowserToolbar
import mozilla.components.compose.browser.toolbar.store.BrowserToolbarStore
import ncxxi.altair.AltairApplication

@Composable
fun BrowserScreen() {
    val components = AltairApplication.get().components
    val scope = rememberCoroutineScope()

    val toolbarStore = remember {
        BrowserToolbarStore(
            middleware = listOf(
                AltairToolbarMiddleware(
                    browserStore = components.store,
                    sessionUseCases = components.sessionUseCases,
                    scope = scope,
                ),
            ),
        )
    }

    LaunchedEffect(Unit) {
        if (components.store.state.tabs.isEmpty()) {
            val tab = createTab(url = "https://www.mozilla.org")
            components.store.dispatch(TabListAction.AddTabAction(tab))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BrowserToolbar(store = toolbarStore)
        GeckoHost(modifier = Modifier.fillMaxSize())
    }
}
