package com.example.tusstudygroupfinder_project.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tusstudygroupfinder_project.IgViewModel

@Composable
fun PublicGroupsScreen(navController: NavController, vm: IgViewModel) {
    var publicGroups by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    LaunchedEffect(Unit) {
        vm.fetchPublicGroups { groups ->
            publicGroups = groups
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

                Spacer(modifier = Modifier.height(16.dp))

                if (publicGroups.isEmpty()) {
                    Text("No public groups available.", color = Color.White)
                } else {
                    publicGroups.forEach { group ->
                        val groupName = group["name"] as? String ?: "Unknown"
                        val groupId = group["id"] as? String ?: ""

                        Button(
                            onClick = {
                                val inviteeId = vm.auth.currentUser?.uid ?: return@Button
                                // Invite the user to the group and refresh the list
                                vm.inviteUserToGroup(groupId, inviteeId) { success ->
                                    if (success) {
                                        publicGroups = publicGroups.filter { it["id"] != groupId }
                                        // Optionally refresh the "View My Groups" in HomeScreen
                                        vm.fetchUserGroups {}
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