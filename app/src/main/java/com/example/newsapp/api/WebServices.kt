package com.example.newsapp.api

import com.example.newsapp.api.model.newsResponse.NewsResponse
import com.example.newsapp.api.model.sourcesResponse.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("v2/top-headlines/sources") // later we define Base URL
    suspend fun getNewsSources(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = Constants.apiKey,
    ): SourcesResponse //Template Parameter, Map JSON to Class

    @GET("v2/everything")
    suspend fun getNews(
        @Query("apiKey") apiKey: String = Constants.apiKey,
        @Query("sources") sources: String,
    ): NewsResponse

    @GET("v2/everything")
    fun getSearchArticles(
        @Query("apiKey") apiKey: String = Constants.apiKey,
        @Query("q") query: String,
    ): NewsResponse

}