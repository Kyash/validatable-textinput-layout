package co.kyash.vtl.validators

import android.content.Context
import co.kyash.vtl.testing.RxImmediateSchedulerRule
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@Suppress("unused")
@RunWith(ParameterizedRobolectricTestRunner::class)
class RequiredValidatorTest(
        private val text: String?,
        private val trim: Boolean,
        private val result: Boolean,
        private val errorMessage: String?
) {

    companion object {
        private val ERROR_MESSAGE = "This field is required"

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data(): List<Array<out Any?>> {
            return listOf(
                    // Failure
                    arrayOf(null, true, false, ERROR_MESSAGE),
                    arrayOf("", true, false, ERROR_MESSAGE),
                    arrayOf(" ", true, false, ERROR_MESSAGE),
                    arrayOf("　", true, false, ERROR_MESSAGE),

                    // Success
                    arrayOf(" ", false, true, null),
                    arrayOf("　", false, true, null),
                    arrayOf("konifar", true, true, null)
            )
        }
    }

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var subject: VtlValidator

    private val context: Context = RuntimeEnvironment.application

    @Before
    @Throws(Exception::class)
    fun setUp() {
        subject = RequiredValidator(ERROR_MESSAGE, trim)
    }

    @Test
    fun validate() {
        assertEquals(result, subject.validate(text))
    }

    @Test
    fun validateAsCompletable() {
        if (errorMessage == null) {
            subject.validateAsCompletable(context, text).test().assertNoErrors().assertComplete()
        } else {
            subject.validateAsCompletable(context, text).test().assertError { it ->
                it.message == errorMessage
            }
        }
    }

}
