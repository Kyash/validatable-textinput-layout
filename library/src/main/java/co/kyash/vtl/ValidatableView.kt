package co.kyash.vtl

import co.kyash.vtl.validators.VtlValidator
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface ValidatableView {

    val validationFlowables: ArrayList<Flowable<Any>>

    fun validate(): Boolean

    fun validateAsCompletable(): Completable

    fun register(validator: VtlValidator): ValidatableView

    fun register(validators: List<VtlValidator>): ValidatableView

    fun clearValidators()
}
