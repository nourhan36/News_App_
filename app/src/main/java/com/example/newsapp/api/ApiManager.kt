package com.example.newsapp.api

import android.util.Log
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    private lateinit var retrofit: Retrofit // Retrofit instance for API calls

    // Initializing API manager
    init {
        // Creating a logging interceptor for HTTP requests and responses
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                // Logging HTTP request and response messages
                Log.e("api->", message)
            }
        })
        logging.setLevel(HttpLoggingInterceptor.Level.BODY) // Setting logging level to include headers, body, and metadata

        // Building OkHttpClient with the logging interceptor
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // Building Retrofit instance with base URL and Gson converter factory for JSON serialization and deserialization
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Method to obtain services interface for API calls
    fun getServices(): WebServices {
        return retrofit.create(WebServices::class.java)
    }

}