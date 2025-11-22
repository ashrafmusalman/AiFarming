package com.ashraf.farming.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ashraf.farming.datamodel.User
import com.ashraf.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeFragmentRepositiory(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) {

    private val _userDetailsStatus = MutableStateFlow<Resources<User>>(Resources.Unspecified())
    val userDetailsStatus = _userDetailsStatus.asStateFlow()

    private val _updateProfilePicture = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val updateProfile = _updateProfilePicture.asStateFlow()

    private val _updateCoverPicture = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val updateCoverPicture = _updateCoverPicture.asStateFlow()

    fun signOut() {
        firebaseAuth.signOut()
    }

    // ------------------------------
    //   FIXED: Get Current User
    // ------------------------------
    fun getCurrentUserDetail() {

        val user = firebaseAuth.currentUser
        if (user == null) {
            _userDetailsStatus.value = Resources.Error("User not logged in")
            return
        }

        _userDetailsStatus.value = Resources.Loading()

        firestore.collection("FarmerUser")
            .document(user.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val userData = snapshot.toObject(User::class.java)
                if (userData != null) {
                    _userDetailsStatus.value = Resources.Success(userData)
                } else {
                    _userDetailsStatus.value = Resources.Error("No user data found")
                }
            }
            .addOnFailureListener {
                _userDetailsStatus.value = Resources.Error(it.localizedMessage)
            }
    }

    // ------------------------------
    //   FIXED: Update Profile Picture
    // ------------------------------
    fun updateProfilePicture(selectedImageUrl: String) {

        val user = firebaseAuth.currentUser
        if (user == null) {
            _updateProfilePicture.value = Resources.Error("User not logged in")
            return
        }

        _updateProfilePicture.value = Resources.Loading()

        val reference = firebaseStorage.reference
            .child("FarmerProfile")
            .child(user.uid)

        reference.putFile(Uri.parse(selectedImageUrl))
            .addOnSuccessListener { task ->

                task.storage.downloadUrl.addOnSuccessListener { imageUri ->

                    val imageUrl = imageUri.toString()

                    // Update Firestore
                    firestore.collection("FarmerUser")
                        .document(user.uid)
                        .update("profileUrl", imageUrl)
                        .addOnSuccessListener {

                            // Update FirebaseAuth profile
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setPhotoUri(Uri.parse(imageUrl))
                                .build()

                            user.updateProfile(profileUpdates)
                                .addOnSuccessListener {
                                    _updateProfilePicture.value = Resources.Success(imageUrl)
                                }
                                .addOnFailureListener {
                                    _updateProfilePicture.value = Resources.Error(it.localizedMessage)
                                }

                        }
                        .addOnFailureListener {
                            _updateProfilePicture.value = Resources.Error(it.localizedMessage)
                        }
                }
            }
            .addOnFailureListener {
                _updateProfilePicture.value = Resources.Error(it.localizedMessage)
            }
    }

    // ------------------------------
    //   FIXED: Upload Cover Photo
    // ------------------------------
    fun uploadCoverPhoto(coverImageUrl: String) {

        val user = firebaseAuth.currentUser
        if (user == null) {
            _updateCoverPicture.value = Resources.Error("User not logged in")
            return
        }

        _updateCoverPicture.value = Resources.Loading()

        val reference = firebaseStorage.reference
            .child("FarmerCoverPhoto")
            .child(user.uid)

        reference.putFile(Uri.parse(coverImageUrl))
            .addOnSuccessListener { task ->
                task.storage.downloadUrl.addOnSuccessListener { imageUri ->
                    val imageUrl = imageUri.toString()

                    firestore.collection("FarmerUser")
                        .document(user.uid)
                        .update("coverPhotoUrl", imageUrl)
                        .addOnSuccessListener {
                            _updateCoverPicture.value = Resources.Success(imageUrl)
                        }
                        .addOnFailureListener {
                            _updateCoverPicture.value = Resources.Error(it.localizedMessage)
                        }
                }
            }
            .addOnFailureListener {
                _updateCoverPicture.value = Resources.Error(it.localizedMessage)
            }
    }
}
