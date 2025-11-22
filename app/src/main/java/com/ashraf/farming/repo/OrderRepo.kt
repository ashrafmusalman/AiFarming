package com.ashraf.farming.repo

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ashraf.farming.datamodel.Order
import com.ashraf.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderRepo(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {

    private val _fetchorder = MutableStateFlow<Resources<List<Order>>>(Resources.Unspecified())
    val fetchOrder = _fetchorder.asStateFlow()

    private val _fetchYourSelledOrder =
        MutableStateFlow<Resources<List<Order>>>(Resources.Unspecified())
    val fetchYourSelledOrder = _fetchYourSelledOrder.asStateFlow()

    private val _updateOrderStatus = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val updateOrderStatus = _updateOrderStatus.asStateFlow()

    private val _countOrderPlaced = MutableStateFlow<Resources<Int>>(Resources.Unspecified())
    val countOrderPlaced = _countOrderPlaced.asStateFlow()

    private val _countOrderReceived = MutableStateFlow<Resources<Int>>(Resources.Unspecified())
    val countOrderReceived = _countOrderReceived.asStateFlow()


    // -----------------------------
    // FETCH ORDERS PLACED BY USER
    // -----------------------------
    fun fetchOrder() {
        _fetchorder.value = Resources.Loading()

        firestore.collection("FarmingProductOrder")
            .whereEqualTo("buyerId", firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { snapshot ->

                val orders = snapshot.documents.map { doc ->
                    val order = doc.toObject(Order::class.java)!!
                    order.orderId = doc.id     // ⭐ Assign Firestore document ID
                    order
                }

                _fetchorder.value = Resources.Success(orders)
            }
            .addOnFailureListener {
                _fetchorder.value = Resources.Error(it.localizedMessage)
            }
    }


    // -----------------------------
    // FETCH ORDERS RECEIVED (Seller Side)
    // -----------------------------
    fun fetchOrderReceived() {
        _fetchYourSelledOrder.value = Resources.Loading()

        firestore.collection("FarmingProductOrder")
            .whereEqualTo("sellerId", firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { snapshot ->

                val orders = snapshot.documents.map { doc ->
                    val order = doc.toObject(Order::class.java)!!
                    order.orderId = doc.id     // ⭐ Assign Firestore document ID
                    order
                }

                _fetchYourSelledOrder.value = Resources.Success(orders)
            }
            .addOnFailureListener {
                _fetchYourSelledOrder.value = Resources.Error(it.localizedMessage)
            }
    }


    // -----------------------------
    // UPDATE ORDER STATUS
    // -----------------------------
    fun updateOrderStatus(order: Order, status: String) {
        _updateOrderStatus.value = Resources.Loading()

        firestore.collection("FarmingProductOrder")
            .document(order.orderId)   // ⭐ Correct document reference
            .update("orderStatus", status)
            .addOnSuccessListener {
                _updateOrderStatus.value = Resources.Success("Order updated!")
            }
            .addOnFailureListener {
                _updateOrderStatus.value = Resources.Error(it.localizedMessage)
            }
    }


    // -----------------------------
    // COUNT ORDERS PLACED
    // -----------------------------
    fun countOrderPlaced() {
        firestore.collection("FarmingProductOrder")
            .whereEqualTo("buyerId", firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                _countOrderPlaced.value = Resources.Success(it.size())
            }
            .addOnFailureListener {
                Log.e("OrderRepo", "Error fetching purchase count: ${it.localizedMessage}")
            }
    }


    // -----------------------------
    // COUNT ORDERS RECEIVED
    // -----------------------------
    fun countOrderReceived() {
        firestore.collection("FarmingProductOrder")
            .whereEqualTo("sellerId", firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                _countOrderReceived.value = Resources.Success(it.size())
            }
            .addOnFailureListener {
                _countOrderReceived.value = Resources.Error(it.localizedMessage)
            }
    }

}
