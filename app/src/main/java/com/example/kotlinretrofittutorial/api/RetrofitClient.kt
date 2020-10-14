package com.example.kotlinretrofittutorial.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    private var BASE_URL = ""

    fun setURL(baseString: String) {
        BASE_URL = "http://" + baseString + ":5000"
    }

    fun setURL(baseString: String, port: String) {
        BASE_URL = "http://" + baseString + ":" + port
    }

    fun setFullURL(baseString: String) {
        BASE_URL = baseString
    }

    //mapping
    fun INS_API(obj: String): Any {
        val retrofit = RetrofitBuilder(BASE_URL).build()
        return when (obj) {
            "DummyAPI" -> {
                retrofit.create(DummyAPI::class.java)
            }
            "WikiAPI"->{
                retrofit.create(WikiAPI::class.java)
            }
            else -> 1
        }
    }

    //========old way
    private fun createClientAuth(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClientBuilder.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))//로그 전부 찍음
        return okHttpClientBuilder.build()
    }

    val Instance_Dynamic: DynamicAPI by lazy {
        var gson = GsonBuilder()
                .setLenient()
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(createClientAuth())
                .build()
        retrofit.create(DynamicAPI::class.java)
    }
    //============

//    val Instance_Wiki: WikiAPI by lazy{
//        var gson = GsonBuilder()
//                .setLenient()
//                .create()
//        val retrofit = Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(okHttpClient)
//                .build()
//        retrofit.create(WikiAPI::class.java)
//    }
}