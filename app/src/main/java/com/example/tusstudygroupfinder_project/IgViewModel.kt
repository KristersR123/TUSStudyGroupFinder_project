package com.example.tusstudygroupfinder_project

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
}
