package co.kyash.vtl.validators

import android.content.Context
import android.text.TextUtils
import co.kyash.vtl.VtlValidationFailureException
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class RequiredValidator(
        private val errorMessage: String
) : VtlValidator {

    override fun validateAsCompletable(context: Context, text: String?): Completable {
        return Completable.fromRunnable {
            if (!validate(text)) {
                throw VtlValidationFailureException(errorMessage)
            }
        }.subscribeOn(Schedulers.computation())
    }

    override fun validate(text: String?): Boolean {
        return !TextUtils.isEmpty(text)
    }

    override fun getErrorMessage(): String {
        return errorMessage
    }

}