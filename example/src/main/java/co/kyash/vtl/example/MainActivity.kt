package co.kyash.vtl.example

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import co.kyash.vtl.ValidatableView
import co.kyash.vtl.example.api.MaterialDesignColorsApi
import co.kyash.vtl.example.databinding.ActivityMainBinding
import co.kyash.vtl.validators.AsciiOnlyValidator
import co.kyash.vtl.validators.EmailValidator
import co.kyash.vtl.validators.NumberOnlyValidator
import co.kyash.vtl.validators.RequiredValidator
import com.crashlytics.android.Crashlytics
import com.squareup.moshi.Moshi
import io.fabric.sdk.android.Fabric
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val validatableViewsForTriggerTextChanged: ArrayList<ValidatableView> = ArrayList()

    private val validatableViewsForTriggerFocusChanged: ArrayList<ValidatableView> = ArrayList()

    private val compositeDisposable = CompositeDisposable()

    private val api = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com")
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(MaterialDesignColorsApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())

        initValidators()

        binding.submit.setOnClickListener(this::onSubmitClick)
        binding.submit2.setOnClickListener(this::onSubmit2Click)
    }

    private fun initValidators() {
        validatableViewsForTriggerTextChanged.addAll(arrayOf(
                binding.firstName.register(RequiredValidator(getString(R.string.validation_error_required))),
                binding.lastName.register(RequiredValidator(getString(R.string.validation_error_required))),
                binding.email.register(EmailValidator(getString(R.string.validation_error_email))),
                binding.numberOnly.register(NumberOnlyValidator(getString(R.string.validation_error_number_only))),
                binding.asciiOnly.register(AsciiOnlyValidator(getString(R.string.validation_error_ascii_only)))
        ))

        validatableViewsForTriggerFocusChanged.addAll(arrayOf(
                binding.email2.register(EmailValidator(getString(R.string.validation_error_email)))
        ))
    }

    private fun onSubmitClick(@Suppress("UNUSED_PARAMETER") view: View) {
        val validations: List<Completable> = validatableViewsForTriggerTextChanged.map { it.validateAsCompletable() }
        validate(validations)
    }

    private fun onSubmit2Click(@Suppress("UNUSED_PARAMETER") view: View) {
        val validations: List<Completable> = validatableViewsForTriggerFocusChanged.map { it.validateAsCompletable() }
        validate(validations)
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
