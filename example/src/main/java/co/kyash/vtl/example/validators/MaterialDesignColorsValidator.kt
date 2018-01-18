package co.kyash.vtl.example.validators

import android.content.Context
import co.kyash.vtl.VtlValidationFailureException
import co.kyash.vtl.example.R
import co.kyash.vtl.example.api.MaterialDesignColorsApi
import co.kyash.vtl.validators.VtlValidator
import io.reactivex.Completable
import io.reactivex.Single

class MaterialDesignColorsValidator(
        private val api: MaterialDesignColorsApi,
        private val context: Context
) : VtlValidator {

    override fun validateAsCompletable(context: Context, text: String?): Completable {
        return api.all()
                .onErrorResumeNext { Single.error(VtlValidationFailureException(context.getString(R.string.validation_error_server))) }
                .flatMapCompletable { list ->
                    if (text?.trim() != null) {
                        list.filter { it == text.trim().toLowerCase() }
                                .forEach { return@flatMapCompletable Completable.complete() }
                    }
                    return@flatMapCompletable Completable.error(VtlValidationFailureException(getErrorMessage()))
                }
    }

    override fun validate(text: String?): Boolean {
        throw UnsupportedOperationException("sync method is not arrowed because this validation uses async API response.")
    }

    override fun getErrorMessage(): String {
        return context.getString(R.string.validation_error_colors)
    }

}