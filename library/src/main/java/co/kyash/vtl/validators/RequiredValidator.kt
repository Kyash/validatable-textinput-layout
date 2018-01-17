package co.kyash.vtl.validators

import android.content.Context
import android.text.TextUtils
import co.kyash.vtl.VtlValidationFailureException
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class RequiredValidator(
        private val errorMessage: String
) : VtlValidator {

    override fun validate(context: Context, text: String): Completable {
        return Completable.fromRunnable {
            if (TextUtils.isEmpty(text)) {
                throw VtlValidationFailureException(errorMessage)
            }
        }.subscribeOn(Schedulers.computation())
    }

}