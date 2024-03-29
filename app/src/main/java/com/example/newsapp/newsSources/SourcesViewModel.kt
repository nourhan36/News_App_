package com.example.newsapp.newsSources

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.ViewMessage
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.sourcesResponse.Source
import com.example.newsapp.api.model.sourcesResponse.SourcesResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SourcesViewModel : ViewModel() {

    val isLoadingVisible = MutableLiveData<Boolean>()
    val message = MutableLiveData<ViewMessage>()
    val sourcesLiveData = MutableLiveData<List<Source?>?>()

    fun getNewsSources(category: String) {
        category.let { selectedCategory ->
            ApiManager
                .getServices()
                .getNewsSources(category = selectedCategory)
                .enqueue(object : Callback<SourcesResponse> {
                    override fun onFailure(
                        call: Call<SourcesResponse>,
                        t: Throwable
                    ) {
                        isLoadingVisible.value = false
                        message.value = ViewMessage(
                            message = t.message ?: "Something went wrong",
                            posActionName = "Try Again",
                        )
                    }

                    override fun onResponse(
                        call: Call<SourcesResponse>,
                        response: Response<SourcesResponse>
                    ) {
                        isLoadingVisible.value = false
                        if (response.isSuccessful) {
                            sourcesLiveData.value = response.body()?.sources
                            return
                        }
                        val responseJson = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(
                            responseJson,
                            SourcesResponse::class.java
                        )
                        message.value = ViewMessage(
                            message = errorResponse.message ?: "Something went wrong"
                        )
                    }
                })
        }
    }

}