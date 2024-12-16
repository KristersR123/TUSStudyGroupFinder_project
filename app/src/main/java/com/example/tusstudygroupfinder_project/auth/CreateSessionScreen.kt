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
    var selectedRoom by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current // Access the current context for the toast
    // Error states
    var sessionTitleError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf(false) }

    val roomOptions = listOf("3A01", "1B20", "1A15") // Hardcoded some rooms

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
                    dateError = !Regex("\\d{4}-\\d{2}-\\d{2}").matches(it)
                },
                label = { Text("Date (YYYY-MM-DD)", color = Color.White) },
                isError = dateError,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            if (dateError) {
                Text("Enter a valid date in YYYY-MM-DD format", color = Color.Red, fontSize = 12.sp)
            }

            OutlinedTextField(
                value = time,
                onValueChange = {
                    time = it
                    timeError = !Regex("\\d{2}:\\d{2}").matches(it)
                },
                label = { Text("Time (HH:MM)", color = Color.White) },
                isError = timeError,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            if (timeError) {
                Text("Enter a valid time in HH:MM format", color = Color.Red, fontSize = 12.sp)
            }

            Text(
                text = "Location",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            // Room options
            Column {
                roomOptions.forEach { room ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedRoom = if (selectedRoom == room) null else room },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = room,
                            color = if (selectedRoom == room) Color.Green else Color.White,
                            fontWeight = if (selectedRoom == room) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }


            // Custom hyperlink input
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Custom Location (Link)", color = Color.White) },
                enabled = selectedRoom == null,
                isError = locationError,
                modifier = Modifier.fillMaxWidth().background(if (selectedRoom != null) Color.Gray else Color.Transparent)
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
                    sessionTitleError = sessionTitle.isEmpty()
                    dateError = !Regex("\\d{4}-\\d{2}-\\d{2}").matches(date)
                    timeError = !Regex("\\d{2}:\\d{2}").matches(time)
                    locationError = location.isEmpty() && selectedRoom == null

                    if (!sessionTitleError && !dateError && !timeError && !locationError) {
                        val finalLocation = selectedRoom ?: location
                        val sessionData = mapOf(
                            "sessionTitle" to sessionTitle,
                            "date" to date,
                            "time" to time,
                            "location" to finalLocation,
                            "description" to description
                        )
                        vm.createSession(groupId, sessionData) { success ->
                            if (success) {
                                navController.navigate("home/$groupId")
                                Toast.makeText(context, "Session created successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to create session", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please correct the highlighted errors", Toast.LENGTH_SHORT).show()
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
