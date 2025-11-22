package com.ashraf.farming.viewmodel

import androidx.lifecycle.ViewModel
import com.ashraf.farming.datamodel.CartItem
import com.ashraf.farming.repo.Detailsfragmentrepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailFragmentViewmodel @Inject constructor(
    private val detailsfragmentrepo: Detailsfragmentrepo
) : ViewModel() {

    val addProductToCart = detailsfragmentrepo.addProductToCard

    fun addProductToCart(cartItem: CartItem) {
        detailsfragmentrepo.addProductToCart(cartItem)
    }
}