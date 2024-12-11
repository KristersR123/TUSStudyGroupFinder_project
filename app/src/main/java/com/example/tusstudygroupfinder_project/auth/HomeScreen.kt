package com.example.tusstudygroupfinder_project.auth

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tusstudygroupfinder_project.DestinationScreen
import com.example.tusstudygroupfinder_project.IgViewModel
import com.example.tusstudygroupfinder_project.R


@Composable
fun HomeScreen(navController: NavController, vm: IgViewModel, groupId: String) {
    var userGroups by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    val sessions by vm.allSessions
    val isLoading by remember { mutableStateOf(vm.isLoading.value) }
    val scrollState = rememberScrollState()

    // Call loadUserDetails when the HomeScreen is displayed
    LaunchedEffect(Unit) {
        vm.loadUserDetails()
        vm.fetchUserGroups { groups ->
            userGroups = groups
        }
//        vm.fetchSessionsForGroup(groupId) { fetchedSessions ->
//            sessions = fetchedSessions
//            isLoading = false
//        }
        vm.fetchAllSessionsForUserGroups()
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
                    .verticalScroll(scrollState) // Makes the entire screen scrollable
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
                                    .height(50.dp),
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
                                            val groupId = group["id"] as? String ?: return@DropdownMenuItem
                                            navController.navigate("groupDetails/$groupId") // Navigate to GroupDetailsScreen
                                        }
                                    ) {
                                        val groupName = group["name"] as? String ?: "Unknown"
                                        val groupStatus = group["userStatus"] as? String ?: "none"
                                        Text(text = "$groupName ($groupStatus)")
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))


                        if (isLoading) {
                            Text(text = "Loading...", color = Color.White)
                        } else if (sessions.isEmpty()) {
                            Text(text = "No sessions found.", color = Color.White)
                        } else {
                            sessions.forEach { session ->
                                val title = session["sessionTitle"] as? String ?: "No Title"
                                val date = session["date"] as? String ?: "No Date"
                                val time = session["time"] as? String ?: "No Time"
                                val location = session["location"] as? String ?: "No Location"
                                val description = session["description"] as? String ?: "No Description"

                                Text(text = "Title: $title", color = Color.White)
                                Text(text = "Date: $date", color = Color.White)
                                Text(text = "Time: $time", color = Color.White)
                                Text(text = "Location: $location", color = Color.White)
                                Text(text = "Description: $description", color = Color.White)
                                Spacer(modifier = Modifier.height(30.dp))
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

                        Spacer(modifier = Modifier.height(125.dp))

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
                                        .clickable { navController.navigate("publicGroups") }
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
