package com.example.tusstudygroupfinder_project.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tusstudygroupfinder_project.DestinationScreen
import com.example.tusstudygroupfinder_project.IgViewModel
import com.example.tusstudygroupfinder_project.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSessionScreen(navController: NavController, vm: IgViewModel, groupId: String ) {
    var sessionTitle by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current // Access the current context for the toast
    // Error states
    var sessionTitleError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }

    Log.d("CreateSessionScreen", "Received groupId: $groupId")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7E6E44))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "TUS Study Group Finder",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "MIDWEST",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Create Session",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input fields
            OutlinedTextField(
                value = sessionTitle,
                onValueChange = {
                    sessionTitle = it
                    sessionTitleError = it.isEmpty()
                },
                label = { Text("Session Title", color = Color.White) },
                isError = sessionTitleError,
                modifier = Modifier.fillMaxWidth()
            )
            if (sessionTitleError) {
                Text("Session Title is required", color = Color.Red, fontSize = 12.sp)
            }

            OutlinedTextField(
                value = date,
                onValueChange = {
                    date = it
                    dateError = it.isEmpty()
                },
                label = { Text("Date (YYYY-MM-DD)", color = Color.White) },
                isError = dateError,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            if (dateError) {
                Text("Date is required", color = Color.Red, fontSize = 12.sp)
            }

            OutlinedTextField(
                value = time,
                onValueChange = {
                    time = it
                    timeError = it.isEmpty()
                },
                label = { Text("Time (HH:MM)", color = Color.White) },
                isError = timeError,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            if (timeError) {
                Text("Time is required", color = Color.Red, fontSize = 12.sp)
            }

            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                    locationError = it.isEmpty()
                },
                label = { Text("Location or Link", color = Color.White) },
                isError = locationError,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            if (locationError) {
                Text("Location is required", color = Color.Red, fontSize = 12.sp)
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)", color = Color.White) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    // Validate fields
                    sessionTitleError = sessionTitle.isEmpty()
                    dateError = date.isEmpty()
                    timeError = time.isEmpty()
                    locationError = location.isEmpty()

                    if (!sessionTitleError && !dateError && !timeError && !locationError) {
                        val sessionData = mapOf(
                            "sessionTitle" to sessionTitle,
                            "date" to date,
                            "time" to time,
                            "location" to location,
                            "description" to description,
                            "createdAt" to System.currentTimeMillis()
                        )
                        vm.createSession(
                            groupId = groupId, // Pass the current groupId
                            sessionData = sessionData
                        ) { success ->
                            if (success) {
                                navController.navigate("home/$groupId") // Navigate back to the group home screen
                                Toast.makeText(context, "Successfully Created A Session", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed Creating A Session", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please fill out all required fields", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Session", color = Color.White)
            }


            // Go Back Button
            Button(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Go Back",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(432.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.Black)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth().height(60.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Home Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { navController.navigate(DestinationScreen.Home.route) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Home",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Home",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    // User Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
//                                        .clickable { navController.navigate(DestinationScreen.**.route) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = "Public Groups",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "Groups",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    // Settings Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
//                                        .clickable { navController.navigate(DestinationScreen.**.route) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = "Settings",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Settings",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
