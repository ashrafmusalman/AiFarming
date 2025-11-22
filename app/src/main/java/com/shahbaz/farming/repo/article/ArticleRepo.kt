package com.shahbaz.farming.repo.article

import android.util.Log
import com.shahbaz.farming.datamodel.article.Data
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.retrofit.ArticleApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleRepo(
    private val articleApi: ArticleApi
) {

    private val _articleState = MutableStateFlow<Resources<List<Data>>>(Resources.Unspecified())
    val articleState = _articleState.asStateFlow()

    fun getCropById(cropId: Int, apiKey: String) {
        _articleState.value = Resources.Loading()

        articleApi.getCropById(cropId, apiKey).enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Wrap single crop into a list for RecyclerView
                        _articleState.value = Resources.Success(listOf(it))
                    } ?: run {
                        _articleState.value = Resources.Error("No data available")
                    }
                } else {
                    val err = "HTTP ${response.code()} ${response.message()}"
                    Log.e("ArticleRepo", err)
                    _articleState.value = Resources.Error(err)
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Log.e("ArticleRepo", "Network failure", t)
                _articleState.value = Resources.Error(t.message ?: "Unknown error")
            }
        })
    }
}
