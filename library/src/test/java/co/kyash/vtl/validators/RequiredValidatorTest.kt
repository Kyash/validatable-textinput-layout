package co.kyash.vtl.validators

import android.content.Context
import co.kyash.vtl.testing.RxImmediateSchedulerRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@Suppress("unused")
@RunWith(RobolectricTestRunner::class)
class RequiredValidatorTest {

    companion object {
        private val ERROR_MESSAGE = "This field is required"
    }

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var subject: VtlValidator

    private val context: Context = RuntimeEnvironment.application

    @Before
    @Throws(Exception::class)
    fun setUp() {
        subject = RequiredValidator(ERROR_MESSAGE)
    }

    @Test
    fun validate() {
        assertFalse(subject.validate(null))

        assertFalse(subject.validate(""))

        assertEquals(true, subject.validate("konifar"))
    }

    @Test
    fun validateAsCompletable() {
        subject.validateAsCompletable(context, null).test().assertErrorMessage(ERROR_MESSAGE)

        subject.validateAsCompletable(context, "").test().assertErrorMessage(ERROR_MESSAGE)

        subject.validateAsCompletable(context, "konifar").test().assertNoErrors().assertComplete()
    }

}