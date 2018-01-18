package co.kyash.vtl.example.testing

import android.content.Context
import co.kyash.vtl.example.api.MaterialDesignColorsApi
import com.squareup.moshi.Moshi
import ir.mirrajabi.okhttpjsonmock.interceptors.OkHttpMockInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


object MockServer {

    fun api(context: Context): MaterialDesignColorsApi {
        val mOkHttpClient = OkHttpClient.Builder()
                .addInterceptor(OkHttpMockInterceptor(context, 0))
                .build()

        return Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com")
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(mOkHttpClient)
                .build()
                .create(MaterialDesignColorsApi::class.java)
    }

}