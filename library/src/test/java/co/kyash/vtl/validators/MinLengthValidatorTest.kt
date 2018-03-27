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
class MinLengthValidatorTest(
        private val text: String?,
        private val trim: Boolean,
        private val result: Boolean,
        private val errorMessage: String?
) {

    companion object {
        private val MIN_LENGTH = 5
        private val ERROR_MESSAGE = "This field has error"

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data(): List<Array<out Any?>> {
            return listOf(
                    // Failure
                    arrayOf(null, true, false, ERROR_MESSAGE),
                    arrayOf("", true, false, ERROR_MESSAGE),
                    arrayOf("     ", true, false, ERROR_MESSAGE),
                    arrayOf("abcd", true, false, ERROR_MESSAGE),

                    // Success
                    arrayOf("     ", false, true, null),
                    arrayOf("abcde", true, true, null),
                    arrayOf("abcdef", true, true, null)
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
        subject = MinLengthValidator(ERROR_MESSAGE, MIN_LENGTH, trim)
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
            subject.validateAsCompletable(context, text).test().assertErrorMessage(errorMessage)
        }
    }

}