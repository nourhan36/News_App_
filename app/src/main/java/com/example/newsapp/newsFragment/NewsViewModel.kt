package com.example.newsapp.newsFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.ViewMessage
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.newsResponse.Article
import com.example.newsapp.api.model.newsResponse.NewsResponse
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.api.model.sourcesResponse.SourcesResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NewsViewModel : ViewModel() {

    val isLoadingVisible = MutableLiveData<Boolean>()
    val message = MutableLiveData<ViewMessage>()
    val newsLiveData = MutableLiveData<List<Article?>?>()

    fun loadNews(source: Source) {
        isLoadingVisible.value = true
        source.id?.let { sourceId ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = ApiManager.getServices().getNews(sources = sourceId)
                    newsLiveData.postValue(response.articles)
                } catch (ex: HttpException) {
                    val responseJson = ex.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(
                        responseJson,
                        NewsResponse::class.java
                    )
                    message.postValue(
                        ViewMessage(
                            message = errorResponse.message ?: "Something went wrong"
                        )
                    )
                } catch (ex: Exception) {
                    message.postValue(
                        ViewMessage(
                            message = ex.message ?: "Something went wrong"
                        )
                    )
                } finally {
                    isLoadingVisible.postValue(false)
                }
            }
        }

//        /*if(source!=null && source?.id!=null) {
//            ApiManager.getServices()
//                .getNews(sources = source?.id ?: "")
//        }*/
//        // instead of this -> use let
//        source.id?.let { sourceId ->
//            ApiManager.getServices()
//                .getNews(sources = sourceId)
//                .enqueue(object : Callback<NewsResponse> {
//                    override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
//                        isLoadingVisible.value = false
//                        message.value = ViewMessage(
//                            message = t.message ?: "Something went wrong",
//                        )
//                    }
//
//                    override fun onResponse(
//                        call: Call<NewsResponse>,
//                        response: Response<NewsResponse>
//                    ) {
//                        isLoadingVisible.value = false
//                        if (response.isSuccessful) {
//                            newsLiveData.value = response.body()?.articles
//                            return
//                        }
//                        val responseJson = response.errorBody()?.string()
//                        val errorResponse = Gson().fromJson(
//                            responseJson,
//                            NewsResponse::class.java
//                        )
//                        message.value = ViewMessage(
//                            message = errorResponse.message ?: "Something went wrong"
//                        )
//                    }
//                })
//        }
    }

    fun loadArticles(query: String) {
        isLoadingVisible.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiManager.getServices().getSearchArticles(query = query)
                newsLiveData.postValue(response.articles)
            } catch (ex: HttpException) {
                val responseJson = ex.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(
                    responseJson,
                    SourcesResponse::class.java
                )
                message.postValue(
                    ViewMessage(
                        message = errorResponse.message ?: "Something went wrong"
                    )
                )
            } catch (ex: Exception) {
                message.postValue(
                    ViewMessage(
                        message = ex.message ?: "Something went wrong"
                    )
                )
            } finally {
                isLoadingVisible.postValue(false)
            }

        }

//        ApiManager.getServices()
//            .getSearchArticles(query = query)
//            .enqueue(object : Callback<NewsResponse> {
//                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
//                    isLoadingVisible.value = false
//                    message.value = ViewMessage(
//                        message = t.message ?: "Something went wrong",
//                    )
//                }
//
//                override fun onResponse(
//                    call: Call<NewsResponse>,
//                    response: Response<NewsResponse>
//                ) {
//                    isLoadingVisible.value = false
//                    if (response.isSuccessful) {
//                        newsLiveData.value = response.body()?.articles
//                        return
//                    }
//                    val responseJson = response.errorBody()?.string()
//                    val errorResponse = Gson().fromJson(
//                        responseJson,
//                        NewsResponse::class.java
//                    )
//                    message.value = ViewMessage(
//                        message = errorResponse.message ?: "Something went wrong"
//                    )
//                }
//            })
    }

}