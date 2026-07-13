package ncxxi.altair

import android.app.Application

class AltairApplication : Application() {

    val components by lazy { Components(this) }

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
