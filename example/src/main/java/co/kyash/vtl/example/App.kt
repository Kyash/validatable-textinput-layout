package co.kyash.vtl.example

import android.app.Application
import com.facebook.stetho.Stetho

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpStetho()
    }

    private fun setUpStetho() {
        Stetho.initializeWithDefaults(this)
    }
}