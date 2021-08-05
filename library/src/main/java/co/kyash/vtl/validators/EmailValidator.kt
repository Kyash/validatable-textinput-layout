package co.kyash.vtl.validators

import android.content.Context
import android.text.TextUtils
import co.kyash.vtl.VtlValidationFailureException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.regex.Pattern

/**
 * Validation error when the text is invalid email address
 */
class EmailValidator(
        private val errorMessage: String
) : VtlValidator {

    companion object {
        private val PATTERN = Pattern.compile("\\A[\\p{ASCII}&&\\S]+@[\\p{ASCII}&&\\S]+\\z")
    }

    override fun validateAsCompletable(context: Context, text: String?): Completable {
        return Completable.fromRunnable {
            if (!validate(text)) {
                throw VtlValidationFailureException(errorMessage)
            }
        }.subscribeOn(Schedulers.computation())
    }

    override fun validate(text: String?): Boolean {
        return TextUtils.isEmpty(text) || PATTERN.matcher(text).matches()
    }

    override fun getErrorMessage(): String {
        return errorMessage
    }

}
