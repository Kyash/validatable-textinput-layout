package co.kyash.vtl

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import co.kyash.vtl.validators.VtlValidator
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ValidatableTextInputLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr), ValidatableView {

    companion object {
        private val TAG = "VTL"
        private val FOCUS_CHANGED = 1
        private val TEXT_CHANGED = 1 shl 1
    }

    private var shouldValidateOnFocusChanged: Boolean = false
    private var shouldValidateOnTextChanged: Boolean = false
    private var shouldValidateOnTextChangedOnce: Boolean = false
    private var validationInterval: Long = 300

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.ValidatableTextInputLayout, 0, 0)

        val trigger = a.getInt(R.styleable.ValidatableTextInputLayout_trigger, FOCUS_CHANGED)
        shouldValidateOnFocusChanged = trigger and FOCUS_CHANGED != 0
        shouldValidateOnTextChanged = trigger and TEXT_CHANGED != 0

        val enableFloatingLabel = a.getBoolean(R.styleable.ValidatableTextInputLayout_enable_floating_label, true)
        // http://stackoverflow.com/a/35819605
        if (!enableFloatingLabel) hint = " "

        val validationInterval = a.getInteger(R.styleable.ValidatableTextInputLayout_interval, 300)

        a.recycle()
    }

    private val textProcessor: FlowableProcessor<String> = PublishProcessor.create()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val validators: ArrayList<VtlValidator> = ArrayList()

    private val textInputAwareValidationFlowables: ArrayList<Flowable<Any>> = ArrayList()

    private val mainHandler: Handler = HandlerProvider.createMainHandler()

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //
        }

        override fun afterTextChanged(s: Editable) {
            textProcessor.onNext(s.toString())
        }
    }

    private val onCustomFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            if (shouldValidateOnTextChanged || shouldValidateOnTextChangedOnce) {
                shouldValidateOnTextChangedOnce = false
                compositeDisposable.clear()
                compositeDisposable.add(
                        Flowable.zip(textInputAwareValidationFlowables) { Any() }
                                .doOnError({ this.showErrorMessage(it) })
                                .retry() // non-terminated stream
                                .subscribeOn(Schedulers.computation())
                                .subscribe({ clearErrorMessage() }, {})
                )
            }
        } else {
            if (shouldValidateOnFocusChanged) {
                compositeDisposable.clear()
                compositeDisposable.add(
                        validate().subscribe(Functions.EMPTY_ACTION, Consumer<Throwable> {})
                )
            }
        }
    }

    override fun onDetachedFromWindow() {
        compositeDisposable.clear()
        super.onDetachedFromWindow()
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        initListeners()
    }

    private fun initListeners() {
        val editText = editText ?: return

        // If the text has error, validation should run on realtime.
        if (shouldValidateOnTextChanged || shouldValidateOnTextChangedOnce) {
            shouldValidateOnTextChangedOnce = TextUtils.isEmpty(error)
            editText.removeTextChangedListener(textWatcher)
            editText.addTextChangedListener(textWatcher)
        }

        if (shouldValidateOnFocusChanged || shouldValidateOnTextChanged) {
            editText.onFocusChangeListener = onCustomFocusChangeListener
        }
    }

    private fun clearErrorMessage() {
        mainHandler.post {
            error = null
            isErrorEnabled = false
        }
    }

    override fun validate(): Completable {
        val validations: List<Completable> = validators.map {
            it.validate(context, getText())
        }

        return Completable.mergeDelayError(validations)
                .doOnComplete { clearErrorMessage() }
                .doOnError { showErrorMessage(it) }
                .subscribeOn(Schedulers.computation())
    }

    fun getText(): String {
        return if (editText != null) editText!!.text.toString() else ""
    }

    fun setText(text: String?) {
        if (editText != null) editText!!.setText(text)
    }

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        if (editText != null) {
            editText!!.setOnClickListener(onClickListener)
        }
    }

    override fun register(validator: VtlValidator): ValidatableView {
        register(arrayListOf(validator))
        return this
    }

    override fun register(validators: List<VtlValidator>): ValidatableView {
        clearValidators()

        this.validators.addAll(validators)

        this.validators.mapTo(textInputAwareValidationFlowables) {
            textProcessor.onBackpressureDrop()
                    .throttleLast(validationInterval, TimeUnit.MILLISECONDS)
                    // hack to emit an event to `onNext` when completable is completed.
                    .flatMap<Any> { x ->
                        it.validate(context, x)
                                .toSingleDefault(Any())
                                .toObservable()
                                .toFlowable(BackpressureStrategy.BUFFER)
                    }
        }
        return this
    }

    override fun clearValidators() {
        compositeDisposable.clear()
        validators.clear()
    }

    override fun setErrorEnabled(enabled: Boolean) {
        super.setErrorEnabled(enabled)
        toggleErrorView(enabled)
    }

    // http://stackoverflow.com/questions/33230621/textinputlayout-seterrorenabled-doesnt-create-new-textview-object
    private fun toggleErrorView(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        if (childCount == 2) getChildAt(1).visibility = visibility
    }

    private fun showErrorMessage(throwable: Throwable) {
        Log.e(TAG, "Validation error", throwable)

        val errorMessage = VtlValidationFailureException.getErrorMessage(throwable)

        if (errorMessage != null) {
            mainHandler.post {
                error = errorMessage
                isErrorEnabled = true
            }
        }
    }

    private class HandlerProvider {
        companion object {
            private var mainHandler: Handler? = null

            fun createMainHandler(): Handler {
                return if (mainHandler == null) {
                    Handler(Looper.getMainLooper())
                } else {
                    mainHandler!!
                }
            }
        }
    }

}