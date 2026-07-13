package ncxxi.altair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ncxxi.altair.ui.BrowserScreen
import ncxxi.altair.ui.theme.AltairTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AltairTheme {
                BrowserScreen()
            }
        }
    }
}
