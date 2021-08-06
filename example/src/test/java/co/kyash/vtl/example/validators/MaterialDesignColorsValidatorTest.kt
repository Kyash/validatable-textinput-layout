package co.kyash.vtl.example.validators

import android.content.Context
import co.kyash.vtl.example.api.MaterialDesignColorsApi
import co.kyash.vtl.example.testing.RxImmediateSchedulerRule
import co.kyash.vtl.validators.VtlValidator
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@Suppress("unused")
@RunWith(ParameterizedRobolectricTestRunner::class)
class MaterialDesignColorsValidatorTest(
    private val text: String?,
    private val errorMessage: String?
) {

    companion object {
        private const val ERROR_MESSAGE = "This is not Material design color"

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data(): List<Array<out Any?>> {
            return listOf(
                arrayOf("Gold", ERROR_MESSAGE),
                arrayOf("Blue Red", ERROR_MESSAGE),
                arrayOf("Blue ", null),
                arrayOf(" Blue", null),
                arrayOf("Blue", null),
                arrayOf("Red", null)
            )
        }
    }

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var subject: VtlValidator

    private val context: Context = RuntimeEnvironment.application

    private val api: MaterialDesignColorsApi = mock {
        on { all() } doReturn Single.just(listOf("red, pink, blue"))
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        subject = MaterialDesignColorsValidator(api, context)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun validate() {
        subject.validate(text)
    }

    @Ignore
    @Test
    fun validateAsCompletable() {
        if (errorMessage == null) {
            subject.validateAsCompletable(context, text).test().assertNoErrors().assertComplete()
        } else {
            subject.validateAsCompletable(context, text).test().assertError {
                it.message == errorMessage
            }
        }
    }

}
