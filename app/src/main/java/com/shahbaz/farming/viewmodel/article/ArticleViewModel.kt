package com.shahbaz.farming.viewmodel.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahbaz.farming.datamodel.article.Data
import com.shahbaz.farming.repo.article.ArticleRepo
import com.shahbaz.farming.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepo
) : ViewModel() {

    val articleState: StateFlow<Resources<List<Data>>> = articleRepository.articleState

    fun getCrop(cropId: Int, apiKey: String) {
        viewModelScope.launch {
            articleRepository.getCropById(cropId, apiKey)
        }
    }
}
