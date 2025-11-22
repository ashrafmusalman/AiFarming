package com.ashraf.farming.viewmodel

import androidx.lifecycle.ViewModel
import com.ashraf.farming.datamodel.Order
import com.ashraf.farming.repo.OrderRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewmodel @Inject constructor(
    private val orderRepo: OrderRepo,
) : ViewModel() {
    val fetchOrder = orderRepo.fetchOrder

    val fetchYourSelledOrder = orderRepo.fetchYourSelledOrder

    val updateOrderStatus = orderRepo.updateOrderStatus

    val orderPlaced = orderRepo.countOrderPlaced
    val orderReceived = orderRepo.countOrderReceived


    fun fetchOrder() {
        orderRepo.fetchOrder()
    }

    fun fetchOrderReceived() {
        orderRepo.fetchOrderReceived()
    }

    fun updateOrderStatus(order: Order, status: String) {
        orderRepo.updateOrderStatus(order, status)
    }

    fun getOrderPlacedCount() {
        orderRepo.countOrderPlaced()
    }

    fun getOrderReceived() {
        orderRepo.countOrderReceived()
    }


}

