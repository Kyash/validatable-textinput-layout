package co.kyash.vtl.validators

import android.content.Context
import co.kyash.vtl.VtlValidationFailureException
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

/**
 * Validation error when the text contains special characters.
 */
class NoSpecialCharacterValidator(
        private val errorMessage: String
) : VtlValidator {
    companion object {
        private const val VS15 = '\uFE0E'
        private const val VS16 = '\uFE0F'

        private const val ZERO_WIDTH_JOINER = '\u200D'
        private const val ENCLOSING_KEYCAP = '\u20E3'
    }

    override fun validateAsCompletable(context: Context, text: String?): Completable =
            Completable.fromRunnable {
                if (!validate(text)) {
                    throw VtlValidationFailureException(errorMessage)
                }
            }.subscribeOn(Schedulers.computation())

    override fun validate(text: String?): Boolean {
        if (text.isNullOrBlank()) {
            return true
        }
        return text.none {
            val type = Character.getType(it).toByte()
            it == VS15
                    || it == VS16
                    || it == ZERO_WIDTH_JOINER
                    || it == ENCLOSING_KEYCAP
                    || type == Character.SURROGATE
                    || type == Character.OTHER_SYMBOL
        }
    }

    override fun getErrorMessage(): String? = errorMessage
}
