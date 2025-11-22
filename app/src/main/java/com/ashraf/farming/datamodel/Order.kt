package com.ashraf.farming.datamodel

data class Order(
    var orderId: String = "",   // <-- MUST be var
    val sellerId: String = "",
    val buyerId: String = "",
    val orderStatus: String = "",
    val product: CartItem = CartItem(),
    val address: Address = Address()
)

