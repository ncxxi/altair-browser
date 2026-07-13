package ncxxi.altair.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ncxxi.altair.AltairApplication
import ncxxi.altair.browser.GeckoEngine

@Composable
fun BrowserScreen() {
    val engine = remember {
        GeckoEngine.create(AltairApplication.get().geckoRuntime)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Toolbar(
            engine = engine,
            modifier = Modifier.fillMaxWidth(),
        )
        GeckoHost(
            engine = engine,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
