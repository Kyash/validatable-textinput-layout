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

@RunWith(ParameterizedRobolectricTestRunner::class)
class NoSpecialCharacterValidatorTest(
        private val text: String?,
        private val result: Boolean,
        private val errorMessage: String?
) {
    companion object {
        private const val ERROR_MESSAGE = "This contains special characters"

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data(): List<Array<out Any?>> =
                listOf(
                        // CJK Unified Ideographs Extension B
                        arrayOf("\uD844\uDE3D", false, ERROR_MESSAGE),
                        // Folded Hands
                        arrayOf("\uD83D\uDE4F", false, ERROR_MESSAGE),
                        // Grinning Face
                        arrayOf("\uD83D\uDE00", false, ERROR_MESSAGE),
                        // Telephone
                        arrayOf("\u260E\uFE0F", false, ERROR_MESSAGE),
                        // Telephone
                        arrayOf("\u260E\uFE0E", false, ERROR_MESSAGE),
                        // Man Astronaut
                        arrayOf("\uD83D\uDC68\u200D\uD83D\uDE80", false, ERROR_MESSAGE),
                        // Skin Tone
                        arrayOf("\uD83C\uDFFD", false, ERROR_MESSAGE),
                        // Keycap Digit 1
                        arrayOf("1\uFE0F\u20E3", false, ERROR_MESSAGE),
                        arrayOf(null, true, null),
                        arrayOf("   ", true, null),
                        arrayOf("ABCD", true, null),
                        arrayOf("あいうえお", true, null),
                        arrayOf("Aaあ文1!@#$%^&*()_=-+\";:'{}|\\[]<>?,.", true, null),
                        // CJK Unified Ideographs Extension A
                        arrayOf("\u4DB5", true, null)
                )
    }

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var validator: VtlValidator

    private val context: Context = RuntimeEnvironment.application

    @Before
    fun setUp() {
        validator = NoSpecialCharacterValidator(ERROR_MESSAGE)
    }

    @Test
    fun validate() {
        assertEquals(result, validator.validate(text))
    }

    @Test
    fun validateAsCompletable() {
        if (errorMessage == null) {
            validator.validateAsCompletable(context, text).test().assertNoErrors().assertComplete()
        } else {
            validator.validateAsCompletable(context, text).test().assertError { it ->
                it.message == errorMessage
            }
        }
    }
}
