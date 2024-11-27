package com.example.tusstudygroupfinder_project

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tusstudygroupfinder_project.auth.LoginScreen
import com.example.tusstudygroupfinder_project.auth.MainScreen
import com.example.tusstudygroupfinder_project.auth.SignupScreen
import com.example.tusstudygroupfinder_project.auth.HomeScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.tusstudygroupfinder_project.main.NotificationMessage
import com.example.tusstudygroupfinder_project.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

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
    object Home: DestinationScreen("home")
    object Event: DestinationScreen("event")
    object TimeTable: DestinationScreen("timetable")
    object Contact: DestinationScreen("contact")
}

// Composable function for the main authentication app
@Composable
fun AuthenticationApp() {
    // Obtain instances of view models using Hilt
    val vm = hiltViewModel<IgViewModel>()
//    val viewm = hiltViewModel<EventViewModel>()

    // Create a NavController to manage navigation within the app
    val navController = rememberNavController()

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
        composable(DestinationScreen.Home.route) {
            HomeScreen(navController, vm)
        }

//        composable(DestinationScreen.Contact.route){
//            ContactUsScreen(navController, vm)
//        }


    }
}
