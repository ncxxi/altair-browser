package ncxxi.altair.browser

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSession.ContentDelegate
import org.mozilla.geckoview.GeckoSession.NavigationDelegate
import org.mozilla.geckoview.GeckoSession.PermissionDelegate
import org.mozilla.geckoview.GeckoSession.ProgressDelegate

class GeckoEngine private constructor(
    val session: GeckoSession,
) {

    companion object {
        fun create(runtime: GeckoRuntime): GeckoEngine {
            val session = GeckoSession()
            session.open(runtime)
            return GeckoEngine(session)
        }
    }

    private val _state = MutableStateFlow(GeckoState())
    val state: StateFlow<GeckoState> = _state.asStateFlow()

    init {
        session.navigationDelegate = object : NavigationDelegate {
            override fun onLocationChange(
                session: GeckoSession,
                url: String?,
                permissions: MutableList<PermissionDelegate.ContentPermission>,
                hasChangedFlag: Boolean,
            ) {
                _state.update { it.copy(url = url ?: "about:blank") }
            }

            override fun onCanGoBack(
                session: GeckoSession,
                canGoBack: Boolean,
            ) {
                _state.update { it.copy(canGoBack = canGoBack) }
            }

            override fun onCanGoForward(
                session: GeckoSession,
                canGoForward: Boolean,
            ) {
                _state.update { it.copy(canGoForward = canGoForward) }
            }
        }

        session.progressDelegate = object : ProgressDelegate {
            override fun onPageStart(
                session: GeckoSession,
                url: String,
            ) {
                _state.update { it.copy(isLoading = true, progress = 0) }
            }

            override fun onPageStop(
                session: GeckoSession,
                success: Boolean,
            ) {
                _state.update {
                    it.copy(isLoading = false, progress = if (success) 100 else it.progress)
                }
            }

            override fun onProgressChange(
                session: GeckoSession,
                progress: Int,
            ) {
                _state.update { it.copy(progress = progress) }
            }
        }

        session.contentDelegate = object : ContentDelegate {
            override fun onTitleChange(
                session: GeckoSession,
                title: String?,
            ) {
                _state.update { it.copy(title = title ?: "") }
            }
        }
    }

    fun load(url: String) {
        session.loadUri(url)
    }

    fun goBack() {
        if (_state.value.canGoBack) session.goBack()
    }

    fun goForward() {
        if (_state.value.canGoForward) session.goForward()
    }

    fun reload() {
        session.reload()
    }

    fun stop() {
        session.stop()
    }

    fun release() {
        session.close()
    }
}
