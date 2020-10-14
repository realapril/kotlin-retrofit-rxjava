package com.example.kotlinretrofittutorial.api

import io.reactivex.Maybe
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface DynamicAPI{

    @GET("/")
    fun getAnyInfo():
            Maybe<String>

}