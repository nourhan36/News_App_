package com.example.newsapp.newsFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.ViewMessage
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.newsResponse.Article
import com.example.newsapp.api.model.newsResponse.NewsResponse
import com.example.newsapp.api.model.sourcesResponse.Source
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    val isLoadingVisible = MutableLiveData<Boolean>()
    val message = MutableLiveData<ViewMessage>()
    val newsLiveData = MutableLiveData<List<Article?>?>()

    fun loadNews(source: Source) {
        isLoadingVisible.value = true
        /*if(source!=null && source?.id!=null) {
            ApiManager.getServices()
                .getNews(sources = source?.id ?: "")
        }*/
        // instead of this -> use let
        source.id?.let { sourceId ->
            ApiManager.getServices()
                .getNews(sources = sourceId)
                .enqueue(object : Callback<NewsResponse> {
                    override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                        isLoadingVisible.value = false
                        message.value = ViewMessage(
                            message = t.message ?: "Something went wrong",
                        )
                    }

                    override fun onResponse(
                        call: Call<NewsResponse>,
                        response: Response<NewsResponse>
                    ) {
                        isLoadingVisible.value = false
                        if (response.isSuccessful) {
                            newsLiveData.value = response.body()?.articles
                            return
                        }
                        val responseJson = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(
                            responseJson,
                            NewsResponse::class.java
                        )
                        message.value = ViewMessage(
                            message = errorResponse.message ?: "Something went wrong"
                        )
                    }
                })
        }
    }

    fun loadArticles(query: String) {
        ApiManager.getServices()
            .getSearchArticles(query = query)
            .enqueue(object : Callback<NewsResponse> {
                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    isLoadingVisible.value = false
                    message.value = ViewMessage(
                        message = t.message ?: "Something went wrong",
                    )
                }

                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    isLoadingVisible.value = false
                    if (response.isSuccessful) {
                        newsLiveData.value = response.body()?.articles
                        return
                    }
                    val responseJson = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(
                        responseJson,
                        NewsResponse::class.java
                    )
                    message.value = ViewMessage(
                        message = errorResponse.message ?: "Something went wrong"
                    )
                }
            })
    }

}