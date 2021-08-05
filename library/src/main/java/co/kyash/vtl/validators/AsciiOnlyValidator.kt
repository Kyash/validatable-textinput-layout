package co.kyash.vtl.validators

import android.content.Context
import android.text.TextUtils
import co.kyash.vtl.VtlValidationFailureException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.regex.Pattern

/**
 * Validation error when the text contains non-ascii characters
 */
class AsciiOnlyValidator(
        private val errorMessage: String
) : VtlValidator {

    companion object {
        private val PATTERN = Pattern.compile("\\p{ASCII}+\$")
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
