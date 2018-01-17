package co.kyash.vtl.validators

import android.content.Context
import io.reactivex.Completable

interface VtlValidator {

    /**
     * @param context Context
     * @param text The text which the user inputs
     * @return Completable which contains an error message
     */
    fun validate(context: Context, text: String): Completable

}