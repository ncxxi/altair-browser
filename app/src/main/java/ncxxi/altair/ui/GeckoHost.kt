package ncxxi.altair.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import mozilla.components.browser.engine.gecko.GeckoEngineView
import mozilla.components.feature.session.SessionFeature
import ncxxi.altair.AltairApplication

@Composable
fun GeckoHost(modifier: Modifier = Modifier) {
    val components = AltairApplication.get().components
    var sessionFeature by remember { mutableStateOf<SessionFeature?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            sessionFeature?.stop()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            GeckoEngineView(context).also { engineView ->
                SessionFeature(
                    store = components.store,
                    goBackUseCase = components.sessionUseCases.goBack,
                    engineView = engineView,
                ).also { feature ->
                    feature.start()
                    sessionFeature = feature
                }
            }
        },
    )
}
