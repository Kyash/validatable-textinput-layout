package co.kyash.vtl.example

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import co.kyash.vtl.ValidatableView
import co.kyash.vtl.example.api.MaterialDesignColorsApi
import co.kyash.vtl.example.databinding.ActivityMainBinding
import co.kyash.vtl.example.validators.MaterialDesignColorsValidator
import co.kyash.vtl.validators.AsciiOnlyValidator
import co.kyash.vtl.validators.EmailValidator
import co.kyash.vtl.validators.NumberOnlyValidator
import co.kyash.vtl.validators.RequiredValidator
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val validatableViewsForTriggerTextChanged: ArrayList<ValidatableView> = ArrayList()

    private val validatableViewsForTriggerFocusChanged: ArrayList<ValidatableView> = ArrayList()

    private val validatableViewsForButtonEnable: ArrayList<ValidatableView> = ArrayList()

    private val compositeDisposable = CompositeDisposable()

    private val api = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com")
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build())
        .build()
        .create(MaterialDesignColorsApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initValidators()

        binding.submit.setOnClickListener(this::onSubmitClick)
        binding.submit2.setOnClickListener(this::onSubmit2Click)
        binding.submit3.setOnClickListener(this::onSubmit3Click)
    }

    private fun initValidators() {
        // Example 1
        validatableViewsForTriggerTextChanged.addAll(arrayOf(
            binding.firstName.register(RequiredValidator(getString(R.string.validation_error_required))),
            binding.lastName.register(RequiredValidator(getString(R.string.validation_error_required))),
            binding.email.register(EmailValidator(getString(R.string.validation_error_email))),
            binding.numberOnly.register(NumberOnlyValidator(getString(R.string.validation_error_number_only))),
            binding.asciiOnly.register(AsciiOnlyValidator(getString(R.string.validation_error_ascii_only)))
        ))

        // Example 2
        validatableViewsForTriggerFocusChanged.addAll(arrayOf(
            binding.email2.register(EmailValidator(getString(R.string.validation_error_email)))
        ))

        // Example 3
        binding.colors.register(MaterialDesignColorsValidator(api, this))

        // Example 4
        validatableViewsForButtonEnable.addAll(arrayOf(
            binding.firstName2.register(RequiredValidator(getString(R.string.validation_error_required))),
            binding.lastName2.register(RequiredValidator(getString(R.string.validation_error_required)))
        ))
        val validations: List<Flowable<Any>> = validatableViewsForButtonEnable.flatMap { it.validationFlowables }
        Flowable.zip(validations) { Any() }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError({ binding.submit3.isEnabled = false })
            .retry() // non-terminated stream
            .subscribe({ binding.submit3.isEnabled = true }, { })
    }


    private fun onSubmitClick(@Suppress("UNUSED_PARAMETER") view: View) {
        val validations: List<Completable> = validatableViewsForTriggerTextChanged.map { it.validateAsCompletable() }
        validate(validations)
    }

    private fun onSubmit2Click(@Suppress("UNUSED_PARAMETER") view: View) {
        val validations: List<Completable> = validatableViewsForTriggerFocusChanged.map { it.validateAsCompletable() }
        validate(validations)
    }

    private fun onSubmit3Click(@Suppress("UNUSED_PARAMETER") view: View) {
        Toast.makeText(this, R.string.validation_success, Toast.LENGTH_SHORT).show()
    }

    private fun validate(validations: List<Completable>) {
        compositeDisposable.clear()

        compositeDisposable.add(
            Completable.mergeDelayError(validations)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("MainActivity", "Validation cleared.")
                    Toast.makeText(this, R.string.validation_success, Toast.LENGTH_SHORT).show()
                }, { throwable ->
                    Log.e("MainActivity", "Validation error occurred.", throwable)
                    Toast.makeText(this, R.string.validation_error_occurred, Toast.LENGTH_SHORT).show()
                })
        )
    }

}
