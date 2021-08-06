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
class KatakanaOnlyValidatorTest(
        private val text: String?,
        private val result: Boolean,
        private val errorMessage: String?
) {

    companion object {
        private val ERROR_MESSAGE = "This contains non-hiragana characters"

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data(): List<Array<out Any?>> {
            return listOf(
                    arrayOf("a", false, ERROR_MESSAGE),
                    arrayOf("あ", false, ERROR_MESSAGE),
                    arrayOf("阿", false, ERROR_MESSAGE),
                    arrayOf("ア阿", false, ERROR_MESSAGE),
                    arrayOf(null, true, null),
                    arrayOf("", true, null),
                    arrayOf("ァアィイゥウェエォオカガキギクグケゲコゴ" +
                            "サザシジスズセゼソゾタダチヂッツヅテデトド" +
                            "ナニヌネノハバパヒビピフブプヘベペホボポマミムメモ" +
                            "ャヤュユョヨラリルレロヮワヰヱヲンヴヵヶ—―ー", true, null)
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
        subject = KatakanaOnlyValidator(ERROR_MESSAGE)
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
