package ncxxi.altair.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import ncxxi.altair.browser.GeckoEngine
import org.mozilla.geckoview.GeckoView

@Composable
fun GeckoHost(
    engine: GeckoEngine,
    modifier: Modifier = Modifier,
) {
    DisposableEffect(engine) {
        onDispose { engine.release() }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            GeckoView(context).also { view ->
                view.setSession(engine.session)
            }
        },
    )
}
