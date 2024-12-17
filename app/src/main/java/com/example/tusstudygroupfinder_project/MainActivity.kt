package com.example.tusstudygroupfinder_project

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tusstudygroupfinder_project.auth.Contact
import com.example.tusstudygroupfinder_project.auth.CreateGroupScreen
import com.example.tusstudygroupfinder_project.auth.CreateSessionScreen
import com.example.tusstudygroupfinder_project.auth.GroupDetailsScreen
import com.example.tusstudygroupfinder_project.auth.HomeScreen
import com.example.tusstudygroupfinder_project.auth.LoginScreen
import com.example.tusstudygroupfinder_project.auth.MainScreen
import com.example.tusstudygroupfinder_project.auth.PublicGroupsScreen
import com.example.tusstudygroupfinder_project.auth.SelectGroupScreen
import com.example.tusstudygroupfinder_project.auth.SignupScreen
import com.example.tusstudygroupfinder_project.main.NotificationMessage
import com.example.tusstudygroupfinder_project.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.String

/**
 * TUS Study Group Finder Project
 * File: MainActivity.kt
 * Description: Entry point for the application. Handles navigation and initializes the UI
 *              using Jetpack Compose and Dagger Hilt for dependency injection.
 * Author: Kristers Rakstins - K00273773
 * Copied From Last Year's Project (Tus Campus Connect)
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.statusBarColor = getColor(R.color.black)
            window.navigationBarColor = getColor(R.color.black)
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthenticationApp()
                }
            }

        }

    }
}


// Sealed class representing different destination screens with their respective routes
sealed class DestinationScreen(val route: String) {
    object Main: DestinationScreen("main")
    object Signup: DestinationScreen("signup")
    object Login: DestinationScreen("login")
    object Home: DestinationScreen("home/{groupId}")
    object Event: DestinationScreen("event")
    object TimeTable: DestinationScreen("timetable")
    object GroupScreen: DestinationScreen("groupscreen")
    object InviteMembers: DestinationScreen("InviteMembersScreen/{groupId}")
    object SelectGroupSession: DestinationScreen("selectgroup")
    object CreateSession : DestinationScreen("CreateSessionScreen/{groupId}")
    object GroupDetailsScreen: DestinationScreen("groupDetails/{groupId}")
    object PublicGroupsScreen: DestinationScreen("publicGroups")
    object Contact: DestinationScreen("contactUs")
}

// Composable function for the main authentication app
@Composable
fun AuthenticationApp() {
    // Obtain instances of view models using Hilt
    val vm = hiltViewModel<IgViewModel>()
//    val viewm = hiltViewModel<EventViewModel>()

    // Create a NavController to manage navigation within the app
    val navController = rememberNavController()

    // Initialised Room Database Test
    TestRoomDatabase(vm)

    // Display notifications using the NotificationMessage composable
    NotificationMessage(vm)

    // Set up the navigation graph using NavHost
    NavHost(
        navController = navController,
        startDestination = DestinationScreen.Main.route
    ) {
        // Define composable functions for each destination screen
        composable(DestinationScreen.Main.route) {
            MainScreen(navController, vm)
        }
        composable(DestinationScreen.Signup.route) {
            SignupScreen(navController, vm)
        }
        composable(DestinationScreen.Login.route) {
            LoginScreen(navController, vm)
        }

        composable("home/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            HomeScreen(navController, vm, groupId)
        }
        composable(DestinationScreen.GroupScreen.route) {
            CreateGroupScreen(navController, vm)
        }
        composable(DestinationScreen.SelectGroupSession.route){
            SelectGroupScreen(navController, vm)
        }
        //  CreateSession with a dynamic groupId parameter
        composable("CreateSessionScreen/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: return@composable
            CreateSessionScreen(navController, vm, groupId)
        }
        composable("groupDetails/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            GroupDetailsScreen(navController = navController, vm = vm, groupId = groupId)
        }
        composable("publicGroups") {
            PublicGroupsScreen(navController = navController, vm = vm)
        }
        composable("contactUs") {
            Contact(navController = navController, vm = vm)
        }

//        composable(DestinationScreen.Contact.route){
//            ContactUsScreen(navController, vm)
//        }

    }
}

@Composable
fun TestRoomDatabase(vm: IgViewModel) {
    // Test Room Database by caching and fetching groups
    val testGroup = GroupEntity(
        groupId = "2",
        groupName = "Test",
        course = "Test Course",
        isPublic = true
    )
    // Cache group data into Room
    vm.cacheGroups(listOf(testGroup))

    vm.getCachedGroups { groups ->
        Log.d("RoomTest", "Groups fetched: $groups")
    }

    val testSession = SessionEntity(
        groupId = "2",
        title = "OOP",
        date = "2024-12-15",
        time = "17:30",
        location = "somewhere",
        description = "hello",
        sessionId = "2"
    )
    // Cache session data into Room
    vm.cacheSessions(listOf(testSession))

}

