package com.example.kotlinretrofittutorial.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient{

    private var BASE_URL = "https://en.wikipedia.org/w/"

    fun setFullURL(baseString: String){
        BASE_URL = baseString
    }

    private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                        //.addHeader("Authorization", AUTH)
                        .method(original.method(), original.body())

                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()


    val Instance_Wiki: WikiAPI by lazy{
        var gson = GsonBuilder()
                .setLenient()
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        retrofit.create(WikiAPI::class.java)
    }


    private lateinit var Instance_Dynamic : DynamicAPI
    fun Instance_Dynamic() : DynamicAPI{
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build()
        Instance_Dynamic = retrofit.create(DynamicAPI::class.java)
        return Instance_Dynamic
    }
}