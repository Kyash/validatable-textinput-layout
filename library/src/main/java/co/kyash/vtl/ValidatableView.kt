package co.kyash.vtl

import co.kyash.vtl.validators.VtlValidator
import io.reactivex.Completable

interface ValidatableView {

    fun validate(): Completable

    fun register(validator: VtlValidator)

    fun register(validators: List<VtlValidator>)

    fun clearValidators()
}