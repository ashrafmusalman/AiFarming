package com.ashraf.farming.viewmodel.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashraf.farming.datamodel.article.Data
import com.ashraf.farming.repo.article.ArticleRepo
import com.ashraf.farming.util.Resources
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
