package co.kyash.vtl.validators

import android.content.Context
import android.text.TextUtils
import co.kyash.vtl.VtlValidationFailureException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.regex.Pattern

/**
 * Validation error when the text is not written by Alphabet
 */
class AlphabetOnlyValidator(
        private val errorMessage: String
) : VtlValidator {

    companion object {
        private val PATTERN = Pattern.compile("^[a-zA-Z]+\$")
    }

    override fun validateAsCompletable(context: Context, text: String?): Completable {
        return Completable.fromRunnable {
            if (!validate(text)) {
                throw VtlValidationFailureException(errorMessage)
            }
        }.subscribeOn(Schedulers.computation())
    }

    override fun validate(text: String?): Boolean {
        val trimText = text?.replace(" ", "")?.replace("　", "")?.trim()
        return TextUtils.isEmpty(trimText) || PATTERN.matcher(trimText).matches()
    }

    override fun getErrorMessage(): String {
        return errorMessage
    }

}
