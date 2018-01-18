package co.kyash.vtl.example.api

import io.reactivex.Single
import retrofit2.http.GET

interface MaterialDesignColorsApi {

    @GET("/Kyash/validatable-textinput-layout/master/json/colors.json")
    fun all(): Single<List<String>>

}