package co.kyash.vtl.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.kyash.vtl.sample.R.layout
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(layout.activity_main)
    }
}
