package co.kyash.vtl.validators

import android.content.Context
import io.reactivex.rxjava3.core.Completable

interface VtlValidator {

    /**
     * @param context Context
     * @param text The text which the user inputs
     * @return Completable which contains an error message
     */
    fun validateAsCompletable(context: Context, text: String?): Completable

    /**
     * @param text The text which the user inputs
     * @return result : error is false
     */
    fun validate(text: String?): Boolean

    /**
     * @return errorMessage
     */
    fun getErrorMessage(): String?

}
