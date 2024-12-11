package com.example.tusstudygroupfinder_project.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tusstudygroupfinder_project.IgViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(navController: NavController, vm: IgViewModel, groupId: String) {
    var groupDetails by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var searchText by remember { mutableStateOf("") }
    var userSearchResults by remember { mutableStateOf<List<IgViewModel.User>>(emptyList()) }

    LaunchedEffect(groupId) {
        vm.fetchGroupDetails(groupId) { details ->
            groupDetails = details
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7E6E44))
    ) {
        if (isLoading) {
            Text(
                text = "Loading...",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            groupDetails?.let { group ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Group Details",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Name: ${group["name"]}", color = Color.White)
                    Text(text = "Course: ${group["course"]}", color = Color.White)
                    Text(text = "Creator: ${group["creator"]}", color = Color.White)

                    // Search for Users
                    Column {
                        Text(
                            text = "Invite Members",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            label = { Text("Search Username", color = Color.White) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.White,
                                cursorColor = Color.White,
                                focusedLabelColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.Gray
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                vm.searchUsers(searchText) { results ->
                                    userSearchResults = results
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Search", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display Search Results
                        userSearchResults.forEach { user ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(Color.Gray)
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${user.username} (${user.email})",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Button(
                                    onClick = {
                                        vm.inviteUserToGroup(groupId, user.userId) { success ->
                                            if (success) {
                                                println("User ${user.username} invited successfully to group $groupId")
                                            } else {
                                                println("Failed to invite user ${user.username} to group $groupId")
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                                ) {
                                    Text("Invite", color = Color.White)
                                }
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
}