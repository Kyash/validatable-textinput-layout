package co.kyash.vtl

import android.content.Intent
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import co.kyash.vtl.example.MainActivity
import org.junit.Rule
import org.junit.Test

@LargeTest
class MainActivityTest {

    @get:Rule
    private val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    @Test
    fun launch() {
        // when
        activityTestRule.launchActivity(Intent())

        // then
        Intents.intended(IntentMatchers.hasComponent(MainActivity::class.java.name))
    }

}