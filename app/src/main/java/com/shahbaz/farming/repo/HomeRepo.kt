package com.shahbaz.farming.repo

import com.shahbaz.farming.datamodel.homeak.homeSelfResponse
import com.shahbaz.farming.retrofit.TrefleApi
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class HomeRepo @Inject constructor(
    private val trefleApi: TrefleApi
) {

    private val _plants = MutableStateFlow<Resources<homeSelfResponse>>(Resources.Unspecified())
    val plants = _plants.asStateFlow()

    private val _searchPlants = MutableStateFlow<Resources<homeSelfResponse>>(Resources.Unspecified())
    val searchPlants = _searchPlants.asStateFlow()


    suspend fun getPlants() {
        _plants.value = Resources.Loading()

        try {
            val response = trefleApi.getPlants()

            if (response.isSuccessful && response.body() != null) {
                _plants.value = Resources.Success(response.body()!!)
            } else {
                _plants.value = Resources.Error(response.message())
            }

        } catch (e: Exception) {
            _plants.value = Resources.Error(e.localizedMessage ?: "Unknown error")
        }
    }


    suspend fun searchPlants(query: String) {
        _searchPlants.value = Resources.Loading()

        try {
            val response = trefleApi.searchPlants(query)

            if (response.isSuccessful && response.body() != null) {
                _searchPlants.value = Resources.Success(response.body()!!)
            } else {
                _searchPlants.value = Resources.Error(response.message())
            }

        } catch (e: Exception) {
            _searchPlants.value = Resources.Error(e.localizedMessage ?: "Unknown error")
        }
    }


    fun resetPlantState() {
        _plants.value = Resources.Unspecified()
    }

    fun resetSearchState() {
        _searchPlants.value = Resources.Unspecified()
    }
}
