package com.gabia.mbaproject.infrastructure.remote.api

import com.google.gson.GsonBuilder
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

//    https://mba-api.herokuapp.com/swagger-ui/index.html
    companion object {
        val BaseURL = "https://mba-api.herokuapp.com/"

//        val gson = GsonBuilder()
//            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//            .create()

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BaseURL)
                .client(httpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun httpClient(): OkHttpClient {
            val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
            val clientBuilder = OkHttpClient.Builder()
            clientBuilder.protocols(listOf(Protocol.HTTP_1_1))
            clientBuilder.connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
            clientBuilder.callTimeout(20, TimeUnit.SECONDS)
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
            return clientBuilder.build()
        }
    }
}