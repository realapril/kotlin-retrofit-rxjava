package com.example.kotlinretrofittutorial.api

import com.example.kotlinretrofittutorial.models.Models
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface WikiApiService {

    @GET("api.php")
    fun hitCountCheck(@Query("action") action: String,
                      @Query("format") format: String,
                      @Query("list") list: String,
                      @Query("srsearch") srsearch: String):
            Observable<Models.Result>

    //Gets result from https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=<SomeKeywords>

    companion object {
        var gson = GsonBuilder()
                .setLenient()
                .create()
        fun create(): WikiApiService {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())

                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("https://en.wikipedia.org/w/")
                    .build()
            return retrofit.create(WikiApiService::class.java)
        }
    }


}

