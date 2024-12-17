package com.example.tusstudygroupfinder_project.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tusstudygroupfinder_project.IgViewModel


/**
 * TUS Study Group Finder Project
 * File: PublicGroupsScreen.kt
 * Description: Displays a list of public study groups filtered by course. Allows users to join public groups
 *              directly from the screen.
 * Author: Kristers Rakstins - K00273773
 */

@Composable
fun PublicGroupsScreen(navController: NavController, vm: IgViewModel) {
    var publicGroups by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    val context = LocalContext.current // Access the current context for the toast
    var filteredGroups by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var selectedCourse by remember { mutableStateOf<String?>(null) }
    val courses = listOf("Internet Systems Development", "Software Development", "Data Science") // List of Courses

    LaunchedEffect(Unit) {
        vm.fetchPublicGroups { groups ->
            publicGroups = groups
            filteredGroups = groups // Initially show all groups
        }
    }


    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF7E6E44))
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
                    Text(
                        text = "${vm.userName}!",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Public Groups", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)

                // Dropdown for selecting a course
                Text("Filter by Course:", fontSize = 16.sp, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    var expanded by remember { mutableStateOf(false) }
                    Text(
                        text = selectedCourse ?: "Select a Course",
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(vertical = 8.dp)
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        courses.forEach { course ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCourse = course
                                    filteredGroups = if (course.isNotEmpty()) {
                                        publicGroups.filter { it["course"] == course }
                                    } else {
                                        publicGroups
                                    }
                                    expanded = false
                                }
                            ) {
                                Text(course)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display filtered groups
                if (filteredGroups.isEmpty()) {
                    Text("No public groups available for the selected course.", color = Color.White)
                } else {
                    filteredGroups.forEach { group ->
                        val groupName = group["name"] as? String ?: "Unknown"
                        val groupId = group["id"] as? String ?: ""

                        Button(
                            onClick = {
                                val inviteeId = vm.auth.currentUser?.uid ?: return@Button
                                vm.inviteUserToGroup(groupId, inviteeId) { success ->
                                    if (success) {
                                        publicGroups = publicGroups.filter { it["id"] != groupId }
                                        filteredGroups = filteredGroups.filter { it["id"] != groupId }
                                        Toast.makeText(context, "Successfully Joined $groupName", Toast.LENGTH_SHORT).show()
                                        // Optionally refresh the "View My Groups" in HomeScreen
                                        vm.fetchUserGroups {}
                                    } else {
                                        Toast.makeText(context, "Failed To Join $groupName", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text(groupName, fontSize = 18.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigateUp() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Go Back", color = Color.White)
                }
            }
        }
    }
}