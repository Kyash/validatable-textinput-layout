package co.kyash.vtl.example.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface MaterialDesignColorsApi {

    @GET("/Kyash/validatable-textinput-layout/master/json/colors.json")
    fun all(): Single<List<String>>

}
