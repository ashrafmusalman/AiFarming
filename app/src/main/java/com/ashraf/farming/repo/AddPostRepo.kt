package com.ashraf.farming.repo

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.ashraf.farming.datamodel.Post
import com.ashraf.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddPostRepo(
    val firebaseAuth: FirebaseAuth,
    val firebaseStorage: FirebaseStorage,
    val firestore: FirebaseFirestore
) {

    private val _postStauts = MutableStateFlow<Resources<Post>>(Resources.Unspecified())
    val postStatus = _postStauts.asStateFlow()

    private val _fetchPostStatus = MutableStateFlow<Resources<List<Post>    >>(Resources.Unspecified())
    val fetchPost = _fetchPostStatus.asStateFlow()

    fun createPost(imageUrl: String, title: String, description: String) {
        val uid = firebaseAuth.currentUser!!.uid
        _postStauts.value = Resources.Loading()

        // Clean filename (VERY important!)
        val fileName = uid + "_" + System.currentTimeMillis()

        val storageReference = firebaseStorage.reference
            .child("PostImage")
            .child(fileName.toString())

        storageReference.putFile(Uri.parse(imageUrl))
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl
                    .addOnSuccessListener { downloadUrl ->
                        val post = Post(
                            id = uid + System.currentTimeMillis().toString(),
                            userName = firebaseAuth.currentUser!!.displayName ?: "",
                            userProfile = firebaseAuth.currentUser!!.photoUrl?.toString() ?: "",
                            timeStamp = System.currentTimeMillis(),
                            title = title,
                            description = description,
                            image = downloadUrl.toString() // VALID URL NOW!
                        )

                        firestore.collection("Post")
                            .add(post)
                            .addOnSuccessListener {
                                _postStauts.value = Resources.Success(post)
                            }
                            .addOnFailureListener { error ->
                                _postStauts.value = Resources.Error(error.localizedMessage)
                            }
                    }
                    .addOnFailureListener { e ->
                        _postStauts.value = Resources.Error(e.localizedMessage)
                    }
            }
            .addOnFailureListener { e ->
                _postStauts.value = Resources.Error(e.localizedMessage)
            }
    }


    fun resetPostStatus() {
        _postStauts.value = Resources.Unspecified()
    }

    fun fetchPosts() {
        _fetchPostStatus.value = Resources.Loading()
        firebaseAuth.currentUser!!.uid
        firestore.collection("Post")
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                _fetchPostStatus.value = Resources.Success(it.toObjects(Post::class.java))
                Log.d("Data", it.toObjects(Post::class.java).toString())
            }
            .addOnFailureListener {
                _fetchPostStatus.value = Resources.Error(it.localizedMessage)
            }

    }
}