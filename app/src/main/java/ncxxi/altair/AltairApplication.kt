package ncxxi.altair

import android.app.Application
import org.mozilla.geckoview.GeckoRuntime

class AltairApplication : Application() {

    val geckoRuntime: GeckoRuntime by lazy {
        GeckoRuntime.create(this)
    }

    companion object {
        @Volatile
        private var instance: AltairApplication? = null

        fun get(): AltairApplication =
            instance ?: synchronized(this) {
                instance ?: error("AltairApplication not yet created")
            }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
