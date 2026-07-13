package ncxxi.altair

import android.content.Context
import mozilla.components.browser.engine.gecko.GeckoEngine
import mozilla.components.browser.state.engine.EngineMiddleware
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.concept.engine.DefaultSettings
import mozilla.components.feature.session.SessionUseCases
import org.mozilla.geckoview.GeckoRuntime

class Components(context: Context) {

    private val runtime: GeckoRuntime by lazy {
        GeckoRuntime.create(context)
    }

    val engine: GeckoEngine by lazy {
        GeckoEngine(context, runtime, DefaultSettings())
    }

    val store: BrowserStore by lazy {
        BrowserStore(
            middleware = EngineMiddleware.create(engine),
        )
    }

    val sessionUseCases by lazy { SessionUseCases(store) }
}
