package com.example.kotlinretrofittutorial.api

import com.example.kotlinretrofittutorial.models.Models
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiAPI{

    @GET("api.php")
    fun hitCountCheck(@Query("action") action: String,
                      @Query("format") format: String,
                      @Query("list") list: String,
                      @Query("srsearch") srsearch: String):
            Observable<Models.Result>

    @GET("api2.php")
    fun hitCountWithResponseCode(@Query("action") action: String,
                                 @Query("format") format: String,
                                 @Query("list") list: String,
                                 @Query("srsearch") srsearch: String):
            Single<Response<Models.Result>>
}