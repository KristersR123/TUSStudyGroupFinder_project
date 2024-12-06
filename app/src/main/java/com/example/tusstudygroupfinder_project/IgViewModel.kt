package com.example.tusstudygroupfinder_project

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


// got help from -> about login/register. W L PROJECT - FIREBASE LOGIN/REGISTER: https://www.youtube.com/watch?v=ti6Ci0s4SD8&ab_channel=WLPROJECT



@HiltViewModel
class IgViewModel @Inject constructor(
    val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : ViewModel() {

    // Mutable state variables using Jetpack Compose's mutableStateOf
    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val popupNotification =
        mutableStateOf<Event<String>?>(null)


    // Function to handle user signup
    fun onSignup(email: String, pass: String, course: String, username: String) {
        viewModelScope.launch {
            inProgress.value = true

            try {
                // Create a new user using Firebase authentication
                val result = auth.createUserWithEmailAndPassword(email, pass).await()
                if (result.user != null) {
                    // Signup successful, set signedIn to true
                    signedIn.value = true
                    handleException(null, "signup successful")

                    // Store user information in Firestore
                    val userId = result.user?.uid
                    if (userId != null) {
                        storeUserInFirestore(userId, email, pass, course, username)

                        // Call the function to store the timetable
//                        storeTimetableInFirestore(userId, course)
                    }
                } else {
                    // User creation failed
                    handleException(null, "signup failed")
                }
            } catch (e: Exception) {
                // Exception occurred during the signup process
                handleException(e, "signup failed")
            } finally {
                // Set inProgress to false after signup attempt
                inProgress.value = false
            }
        }
    }


    // Function to handle user login
    fun login(email: String, pass: String) {
        inProgress.value = true

        // Sign in using Firebase authentication
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Login successful, set signedIn to true
                    signedIn.value = true
                    handleException(it.exception, "login successful")
                } else {
                    // Login failed
                    handleException(it.exception, "login failed")
                }
                // Set inProgress to false after login attempt
                inProgress.value = false
            }
    }

    // Function to handle exceptions and display error messages
    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage: $errorMsg"
        popupNotification.value = Event(message)
    }

    // Function to handle user logout
    fun logout() {
        auth.signOut()
        signedIn.value = false
    }


    // Function to load user details
    fun loadUserDetails() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            viewModelScope.launch {
                try {
                    val docSnapshot = fireStore.collection("users").document(userId).get().await()
                    userName = docSnapshot.getString("username") ?: ""
                } catch (e: Exception) {
                    // Handle exceptions, e.g., show an error message
                }
            }
        }
    }


    // Function to store user information in Firestore
    private suspend fun storeUserInFirestore(
        userId: String,
        email: String,
        pass: String,
        course: String,
        username: String
    ) {
        try {
            val user = hashMapOf(
                "email" to email,
                "password" to pass,
                "course" to course, // Add the course information
                "username" to username
            )

            // Set user information in Firestore under the "users" collection
            fireStore.collection("users")
                .document(userId)
                .set(user)
                .await()

            // Call the function to store the timetable
//            storeTimetableInFirestore(userId, course)

        } catch (e: Exception) {
            handleException(e, "Failed to store user information (included password) in Firestore")
        }
        Log.d("Signup", "Storing user: Email: $email, Username: $username, Course: $course")

    }

    // Mutable state variables for user contact information
    var userName by mutableStateOf("")
    var userEmail by mutableStateOf("")
    var userMessage by mutableStateOf("")


    // Create a group and store it in Firestore
    fun createGroup(
        groupName: String,
        course: String,
        isPublic: Boolean,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return onComplete(false, null)

        val groupInfo = hashMapOf(
            "name" to groupName,
            "course" to course,
            "public" to isPublic,
            "creator" to userId
        )

        // Add the group to the "groups" collection
        fireStore.collection("groups")
            .add(groupInfo)
            .addOnSuccessListener { documentReference ->
                Log.d("Firebase", "Group created with ID: ${documentReference.id}")

                // Immediately add a membership entry for the group creator
                val membershipInfo = hashMapOf(
                    "userId" to userId,
                    "status" to "joined"  // Assuming the creator is automatically joined
                )
                fireStore.collection("groups").document(documentReference.id)
                    .collection("memberships")
                    .document(userId) // Ensure the document ID is the userId
                    .set(membershipInfo)
                    .addOnSuccessListener {
                        Log.d(
                            "Firebase",
                            "Membership entry created for user $userId in group ${documentReference.id}"
                        )
                        onComplete(true, documentReference.id)
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error adding membership", e)
                        onComplete(false, null) // Handle the error appropriately
                    }
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error adding group", e)
                onComplete(false, null)
            }
    }

    var userSearchResults = mutableStateOf<List<User>>(listOf())

    // Search function to gather users via username from firestore
    fun searchUsers(searchText: String) {
        if (searchText.isBlank()) {
            userSearchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            inProgress.value = true
            try {
                val querySnapshot = fireStore.collection("users")
                    .whereEqualTo("username", searchText)
                    .get()
                    .await()

                val users = querySnapshot.documents.mapNotNull { document ->
                    try {
                        document.toObject(User::class.java)?.apply { userId = document.id }
                    } catch (e: Exception) {
                        Log.e("SearchUsers", "Error converting document to User", e)
                        null
                    }
                }
                userSearchResults.value = users
            } catch (e: Exception) {
                Log.e("SearchUsers", "Failed to search users", e)
                userSearchResults.value = emptyList()  // Ensure the UI is updated even on error.
            } finally {
                inProgress.value = false
            }
        }
    }

   // function to invite users to groups membership, invited users status is invited.
    fun inviteUserToGroup(groupId: String, userId: String) {
        val membershipInfo = hashMapOf("userId" to userId, "status" to "invited")
        fireStore.collection("groups").document(groupId)
            .collection("memberships")
            .add(membershipInfo)
            .addOnSuccessListener {
                Log.d(
                    "InviteUser",
                    "Successfully invited user $userId to group $groupId"
                )
            }
            .addOnFailureListener { e -> Log.e("InviteUser", "Error inviting user to group", e) }
    }

    // Function to fetch the groups created by the logged-in user
    fun fetchUserGroups(onResult: (List<Map<String, Any>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        fireStore.collection("groups")
            .whereEqualTo("creator", userId) // Fetch groups created by the user
            .get()
            .addOnSuccessListener { querySnapshot ->
                val groups = querySnapshot.documents.map { it.data ?: emptyMap() }
                onResult(groups) // Return the group data
            }
            .addOnFailureListener { e ->
                Log.e("FetchGroups", "Error fetching groups", e)
                onResult(emptyList()) // Return an empty list in case of failure
            }
    }


    // Data class for a user
    data class User(
        var userId: String = "",
        var username: String = "",
        var email: String = ""
    )
}


    // Function to store contact information in Firestore
//    suspend fun storeContactInFirestore(name: String, email: String, message: String) {
//        try {
//            val contact = hashMapOf(
//                "name" to name,
//                "email" to email,
//                "message" to message
//            )
//
//            // Store contact information in Firestore under the "contacts" collection
//            fireStore.collection("contacts")
//                .add(contact)
//                .await()
//
//        } catch (e: Exception) {
//            handleException(e, "Failed to store contact information in Firestore")
//        }
//    }

