package com.ashraf.farming.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashraf.farming.repo.HomeRepo
import com.ashraf.farming.util.Resources
import com.ashraf.farming.datamodel.homeak.homeSelfResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: HomeRepo
) : ViewModel() {

    val plants: StateFlow<Resources<homeSelfResponse>> = repo.plants
    val searchPlants: StateFlow<Resources<homeSelfResponse>> = repo.searchPlants

    fun getPlants() {
        viewModelScope.launch {
            repo.getPlants()
        }
    }

    fun searchPlants(query: String) {
        viewModelScope.launch {
            repo.searchPlants(query)
        }
    }

    fun resetPlantState() = repo.resetPlantState()
    fun resetSearchState() = repo.resetSearchState()
}
