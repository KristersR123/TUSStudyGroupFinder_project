package com.example.tusstudygroupfinder_project

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


// got help from -> about login/register. W L PROJECT - FIREBASE LOGIN/REGISTER: https://www.youtube.com/watch?v=ti6Ci0s4SD8&ab_channel=WLPROJECT



@HiltViewModel
class IgViewModel @Inject constructor(
    val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val groupDao: GroupDao,
    private val sessionDao: SessionDao
) : ViewModel() {

    private val database = FirebaseDatabase.getInstance("https://tusstudygroupfinder-default-rtdb.europe-west1.firebasedatabase.app/")


    // Tracks if a user is signed in (used for UI updates)
    val signedIn = mutableStateOf(false)
    // Indicates if a process (e.g., login/signup) is ongoing (used for loading indicators)
    val inProgress = mutableStateOf(false)
    // Stores popup notifications for user feedback
    val popupNotification = mutableStateOf<Event<String>?>(null)


    // Handles user signup and stores additional information in Firestore
    fun onSignup(email: String, pass: String, username: String) {
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
                        storeUserInFirestore(userId, email, pass, username)

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


    // Handles user login with email and password
    fun login(email: String, pass: String) {
        inProgress.value = true // Start loading indicator

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    signedIn.value = true
                    handleException(it.exception, "login successful")

                    // Clear outdated session data
                    signedIn.value = true
                } else {
                    handleException(it.exception, "login failed") // Handle login errors
                }
                inProgress.value = false
            }
        Log.d("FirebaseAuth", "Current user: ${auth.currentUser?.uid}")
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
        username: String
    ) {
        try {
            val user = hashMapOf(
                "email" to email,
                "password" to pass,
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
        Log.d("Signup", "Storing user: Email: $email, Username: $username")

    }

    // Mutable state variables for user contact information
    var userName by mutableStateOf("")
    var userEmail by mutableStateOf("")
    var userMessage by mutableStateOf("")


fun createGroup(
    groupName: String,
    course: String,
    isPublic: Boolean,
    onComplete: (Boolean, String?) -> Unit
) {
    val userId = auth.currentUser?.uid ?: return onComplete(false, null)
    val groupId = database.reference.child("groups").push().key ?: return onComplete(false, null)

    val groupData = mapOf(
        "name" to groupName,
        "course" to course,
        "creator" to userId,
        "isPublic" to isPublic
    )

    val membershipData = mapOf(
        userId to mapOf("status" to "joined")
    )

    val rolesData = mapOf(
        userId to "creator" // Add creator to roles
    )

    Log.d("Firebase", "Attempting to create group: $groupData")

    database.reference.child("groups").child(groupId).setValue(groupData)
        .addOnSuccessListener {
            Log.d("Firebase", "Group created successfully: $groupId")

            // Add memberships
            database.reference.child("groups").child(groupId).child("memberships")
                .updateChildren(membershipData)
                .addOnSuccessListener {
                    Log.d("Firebase", "Membership created successfully for group: $groupId")

                    // Add roles
                    database.reference.child("groups").child(groupId).child("roles")
                        .updateChildren(rolesData)
                        .addOnSuccessListener {
                            Log.d("Firebase", "Roles created successfully for group: $groupId")
                            onComplete(true, groupId)
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firebase", "Error adding roles: ${e.message}")
                            onComplete(false, null)
                        }
                }
                .addOnFailureListener {
                    Log.e("Firebase", "Error adding membership: ${it.message}")
                    onComplete(false, null)
                }
        }
        .addOnFailureListener {
            Log.e("Firebase", "Error creating group: ${it.message}")
            onComplete(false, null)
        }
}

    var userSearchResults = mutableStateOf<List<User>>(listOf())

    // Search function to gather users via username from firestore
    fun searchUsers(searchText: String, onResult: (List<User>) -> Unit) {
        if (searchText.isBlank()) {
            onResult(emptyList())
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
                    document.toObject(User::class.java)?.apply { userId = document.id }
                }

                onResult(users)
            } catch (e: Exception) {
                Log.e("SearchUsers", "Error searching users", e)
                onResult(emptyList())
            } finally {
                inProgress.value = false
            }
        }
    }

   // function to invite users to groups membership, invited users status is invited.
   fun inviteUserToGroup(groupId: String, inviteeId: String, onComplete: (Boolean) -> Unit) {
       val membershipData = mapOf("status" to "invited")
       val roleData = mapOf(inviteeId to "member")

       database.reference.child("groups").child(groupId).child("memberships").child(inviteeId)
           .setValue(membershipData)
           .addOnSuccessListener {
               database.reference.child("groups").child(groupId).child("roles").child(inviteeId)
                   .setValue(roleData)
                   .addOnSuccessListener {
                       Log.d("InviteUserToGroup", "User $inviteeId invited to group $groupId")
                       onComplete(true)
                   }
                   .addOnFailureListener { e ->
                       Log.e("InviteUserToGroup", "Error updating role: ${e.message}")
                       onComplete(false)
                   }
           }
           .addOnFailureListener { e ->
               Log.e("InviteUserToGroup", "Error inviting user: ${e.message}")
               onComplete(false)
           }
   }


// Function to fetch user's groups
fun fetchUserGroups(onResult: (List<Map<String, Any>>) -> Unit) {
    val userId = auth.currentUser?.uid ?: return

    database.reference.child("groups").get()
        .addOnSuccessListener { dataSnapshot ->
            val groups = dataSnapshot.children.mapNotNull { groupSnapshot ->
                val groupData = groupSnapshot.value as? Map<String, Any>
                val memberships = groupSnapshot.child("memberships").value as? Map<String, Map<String, String>>
                val userStatus = memberships?.get(userId)?.get("status")

                if (userStatus in listOf("joined", "invited") || groupData?.get("creator") == userId) {
                    groupData?.toMutableMap()?.apply {
                        put("id", groupSnapshot.key ?: "")
                        put("userStatus", userStatus ?: "none")
                    }
                } else {
                    null
                }
            }
            onResult(groups)
        }
        .addOnFailureListener { e ->
            Log.e("FetchUserGroups", "Error fetching groups", e)
            onResult(emptyList())
        }
}

    fun fetchMyJoinedOrCreatedGroups(onResult: (List<Map<String, Any>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        database.reference.child("groups").get()
            .addOnSuccessListener { dataSnapshot ->
                val groups = dataSnapshot.children.mapNotNull { groupSnapshot ->
                    val groupData = groupSnapshot.value as? Map<String, Any>
                    val memberships = groupSnapshot.child("memberships").value as? Map<String, Map<String, String>>

                    // Include groups where the user is "joined" or the creator
                    if (groupData?.get("creator") == userId || memberships?.get(userId)?.get("status") == "joined") {
                        groupData?.plus("id" to (groupSnapshot.key ?: ""))
                    } else {
                        null
                    }
                }
                Log.d("FetchMyGroups", "Fetched groups: $groups")
                onResult(groups)
            }
            .addOnFailureListener { e ->
                Log.e("FetchMyGroups", "Error fetching groups", e)
                onResult(emptyList())
            }
    }

    fun fetchPublicGroups(onResult: (List<Map<String, Any>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        database.reference.child("groups").get()
            .addOnSuccessListener { dataSnapshot ->
                val publicGroups = dataSnapshot.children.mapNotNull { groupSnapshot ->
                    val groupData = groupSnapshot.value as? Map<String, Any> ?: return@mapNotNull null
                    val isPublic = groupData["isPublic"] as? Boolean ?: false
                    val creator = groupData["creator"] as? String
                    val memberships = groupSnapshot.child("memberships").value as? Map<String, Map<String, String>>
                    val userStatus = memberships?.get(userId)?.get("status")

                    // Only include public groups where the user is NOT the creator, joined, or invited
                    if (isPublic && creator != userId && userStatus !in listOf("joined", "invited")) {
                        groupData.plus("id" to (groupSnapshot.key ?: ""))
                    } else {
                        null
                    }
                }
                onResult(publicGroups)
            }
            .addOnFailureListener { e ->
                Log.e("FetchPublicGroups", "Error fetching public groups", e)
                onResult(emptyList())
            }
    }


    // Function to create a session in a group
    fun createSession(groupId: String, sessionData: Map<String, Any>, onComplete: (Boolean) -> Unit) {
        val groupRef = database.reference.child("groups").child(groupId)

        // Fetch group data
        groupRef.get().addOnSuccessListener { groupSnapshot ->
            if (!groupSnapshot.exists()) {
                Log.e("CreateSession", "Group does not exist. Group ID: $groupId")
                onComplete(false)
                return@addOnSuccessListener
            }

            // Fetch user role
            val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
            val userRole = groupSnapshot.child("roles").child(userId).value as? String

            if (userRole in listOf("creator", "member")) {
                Log.d("CreateSession", "User is authorized to create a session.")

                // Create session
                val sessionRef = groupRef.child("sessions").push()
                sessionRef.setValue(sessionData).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("CreateSession", "Session created successfully.")
                        onComplete(true)
                    } else {
                        Log.e("CreateSession", "Failed to create session: ${task.exception}")
                        onComplete(false)
                    }
                }
            } else {
                Log.e("PermissionError", "User does not have the required role to create a session.")
                onComplete(false)
            }
        }.addOnFailureListener { e ->
            Log.e("CreateSession", "Error fetching group data: ${e.message}")
            onComplete(false)
        }
    }

    fun deleteGroup(groupId: String, onComplete: (Boolean) -> Unit) {
        val groupRef = database.reference.child("groups").child(groupId)

        // Remove the group from the database
        groupRef.removeValue()
            .addOnSuccessListener {
                Log.d("DeleteGroup", "Group deleted successfully.")
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                Log.e("DeleteGroup", "Failed to delete group: ${exception.message}")
                onComplete(false)
            }
    }

    fun fetchGroupDetails(groupId: String, onComplete: (Map<String, Any>?) -> Unit) {
        database.reference.child("groups").child(groupId).get()
            .addOnSuccessListener { snapshot ->
                val groupDetails = snapshot.value as? Map<String, Any>
                onComplete(groupDetails)
            }
            .addOnFailureListener { exception ->
                Log.e("FetchGroupDetails", "Failed to fetch group details: ${exception.message}")
                onComplete(null)
            }
    }

    fun leaveGroup(groupId: String, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onComplete(false)

        viewModelScope.launch {
            try {
                // Remove user from the group's memberships in Firebase
                database.reference.child("groups").child(groupId).child("memberships").child(userId).removeValue().await()


                Log.d("LeaveGroup", "User $userId successfully left group $groupId")
                onComplete(true)
            } catch (e: Exception) {
                Log.e("LeaveGroup", "Error leaving group: ${e.message}")
                onComplete(false)
            }
        }
    }



    val allSessions = mutableStateOf<List<Map<String, Any>>>(emptyList())
    val isLoading = mutableStateOf(false)

    // Function to fetch sessions for a group
    fun fetchAllSessionsForUserGroups() {
        isLoading.value = true
        val userId = auth.currentUser?.uid ?: return

        database.reference.child("groups")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val sessionsList = mutableListOf<Map<String, Any>>()
                    snapshot.children.forEach { groupSnapshot ->
                        val memberships = groupSnapshot.child("memberships").value as? Map<String, Map<String, String>>
                        val userStatus = memberships?.get(userId)?.get("status")

                        if (userStatus == "joined" || userStatus == "invited" || groupSnapshot.child("creator").value == userId) {
                            val groupName = groupSnapshot.child("name").value as? String ?: "Unknown Group"
                            groupSnapshot.child("sessions").children.forEach { sessionSnapshot ->
                                val sessionData = sessionSnapshot.value as? Map<String, Any> ?: return@forEach
                                val sessionWithGroup = sessionData.toMutableMap()
                                sessionWithGroup["groupName"] = groupName
                                sessionsList.add(sessionWithGroup)
                            }
                        }
                    }
                    allSessions.value = sessionsList
                    isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FetchAllSessions", "Error fetching all sessions: ${error.message}")
                    allSessions.value = emptyList()
                    isLoading.value = false
                }
            })
    }

    // Data class for a user
    data class User(
        var userId: String = "",
        var username: String = "",
        var email: String = ""
    )

    fun sendInquiry(name: String, email: String, message: String, onComplete: (Boolean) -> Unit) {
        val inquiry = mapOf(
            "name" to name,
            "email" to email,
            "message" to message,
            "timestamp" to System.currentTimeMillis()
        )

        fireStore.collection("inquiries")
            .add(inquiry)
            .addOnSuccessListener {
                Log.d("ContactUs", "Inquiry submitted successfully.")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e("ContactUs", "Failed to submit inquiry: ${e.message}")
                onComplete(false)
            }
    }


    fun cacheGroups(groups: List<GroupEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            groupDao.insertGroups(groups)
        }
    }

    fun cacheSessions(sessions: List<SessionEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            sessionDao.insertSessions(sessions)
        }
    }

    fun getCachedGroups(onComplete: (List<GroupEntity>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val groups = groupDao.getGroups()
            onComplete(groups)
        }
    }

//    fun getCachedSessions(groupId: String, onComplete: (List<SessionEntity>) -> Unit) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val sessions = sessionDao.getSessionsForGroup(groupId)
//            onComplete(sessions)
//        }
//    }
}



