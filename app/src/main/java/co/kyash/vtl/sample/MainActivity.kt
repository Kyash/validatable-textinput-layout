package co.kyash.vtl.sample

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import co.kyash.vtl.ValidatableView
import co.kyash.vtl.sample.databinding.ActivityMainBinding
import co.kyash.vtl.validators.RequiredValidator
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val validatableViews: ArrayList<ValidatableView> = ArrayList()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())

        initValidators()

        binding.submit.setOnClickListener(this::onSubmitClick)
    }

    private fun initValidators() {
        binding.firstName.register(RequiredValidator(
                getString(R.string.validation_error_required, getString(R.string.first_name))
        ))
        binding.lastName.register(RequiredValidator(
                getString(R.string.validation_error_required, getString(R.string.last_name))
        ))

        validatableViews.add(binding.firstName)
        validatableViews.add(binding.lastName)
    }

    private fun onSubmitClick(view: View) {
        compositeDisposable.clear()

        compositeDisposable.add(
                validate()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d("MainActivity", "Validation cleared.")
                            Toast.makeText(this, R.string.validation_success, Toast.LENGTH_LONG).show()
                        }, { throwable ->
                            Log.e("MainActivity", "Validation error occurred.", throwable)
                            Toast.makeText(this, R.string.validation_error_occurred, Toast.LENGTH_LONG).show()
                        })
        )
    }

    private fun validate(): Completable {
        val validations: List<Completable> = validatableViews.map { it.validate() }

        return Completable.mergeDelayError(validations)
                .subscribeOn(Schedulers.computation())
    }
}
