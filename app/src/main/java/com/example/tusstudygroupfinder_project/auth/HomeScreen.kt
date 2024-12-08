package com.example.tusstudygroupfinder_project.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tusstudygroupfinder_project.IgViewModel
import com.example.tusstudygroupfinder_project.DestinationScreen
import com.example.tusstudygroupfinder_project.auth.CreateGroupScreen
import com.example.tusstudygroupfinder_project.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun HomeScreen(navController: NavController, vm: IgViewModel) {
    var userGroups by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    var scheduledSessions by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    // Call loadUserDetails when the HomeScreen is displayed
    LaunchedEffect(Unit) {
        vm.loadUserDetails()
        vm.fetchUserGroups { groups ->
            userGroups = groups // Update the list of groups
        }
        vm.fetchMyGroups { groups ->
            userGroups = groups // Update the list of groups
        }
        vm.fetchScheduledSessions { sessions ->
            scheduledSessions = sessions
        }
    }
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
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "MIDWEST",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Welcome Back, ${vm.userName}!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF7E6E44))
            ) {


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // View My Groups Button with Dropdown
                        Box {
                            Button(
                                onClick = { expanded = !expanded },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .shadow(0.dp, shape = RoundedCornerShape(8.dp)),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text(
                                    text = "View My Groups",
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            }
                            var color by remember { mutableStateOf(Color.White) }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.DarkGray)
                            ) {
                                userGroups.forEach { group ->
                                    DropdownMenuItem(
                                        onClick = {
                                            expanded = false
                                            // Optional: Navigate or handle click
                                        }
                                    ) {
                                        Text(
                                            text = group["name"] as? String ?: "Unknown Group",
                                            color = Color.Black

                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))



                        Button(
                            onClick = {
//                            navController.navigate(DestinationScreen.TimeTable.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .shadow(0.dp, shape = RoundedCornerShape(8.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text(
                                text = "Scheduled Sessions",
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        // Display sessions
                        Column {
                            scheduledSessions.forEach { session ->
                                val groupName = session["groupName"] as? String ?: "Unknown Group"
                                val course = session["course"] as? String ?: "Unknown Course"
                                val title = session["title"] as? String ?: "No Title"
                                val date = session["date"] as? String ?: "Unknown Date"
                                val time = session["time"] as? String ?: "Unknown Time"
                                val location = session["location"] as? String ?: "No Location"

                                Text(
                                    text = "$groupName - $course : $title\n$date $time, $location",
                                    color = Color.White,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        Button(
                            onClick = {
                                navController.navigate(DestinationScreen.GroupScreen.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .shadow(0.dp, shape = RoundedCornerShape(8.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text(
                                text = "New Study Group",
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = {
                            navController.navigate(DestinationScreen.SelectGroupSession.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .shadow(0.dp, shape = RoundedCornerShape(8.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text(
                                text = "New Session",
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(215.dp))

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
                                        contentDescription = "User",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "User",
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

                        Spacer(modifier = Modifier.height(30.dp))

                        // Back Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .align(Alignment.CenterHorizontally)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFFFD700), Color(0xFFFFD700))
                                    )
                                )
                        ) {
                            Button(
                                onClick = {
                                    vm.logout()
                                    navController.navigate(DestinationScreen.Main.route)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    Color.Transparent
                                ),
                                modifier = Modifier
                                    .width(300.dp)
                                    .align(Alignment.Center)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color(0xff554800), Color(0xff554800))
                                        )
                                    )
                            ) {
                                Text(
                                    text = "Logout",
                                    color = Color.Black,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}