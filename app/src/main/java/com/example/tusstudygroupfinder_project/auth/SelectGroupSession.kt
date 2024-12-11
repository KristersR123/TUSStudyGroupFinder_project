package com.example.tusstudygroupfinder_project.auth

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tusstudygroupfinder_project.DestinationScreen
import com.example.tusstudygroupfinder_project.IgViewModel
import com.example.tusstudygroupfinder_project.R

@Composable
fun SelectGroupScreen(navController: NavController, vm: IgViewModel) {
    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var selectedGroupName by remember { mutableStateOf<String>("Select a Group") }
    var userGroups by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var groupSessions by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }

    LaunchedEffect(Unit) {
        vm.fetchMyJoinedOrCreatedGroups { groups ->
            userGroups = groups.map { group ->
                group.toMutableMap().apply {
                    if (!this.containsKey("id")) {
                        this["id"] = (this["name"] as? String)?.hashCode().toString()
                    }
                }
            }
            Log.d("UserGroups", "Fetched userGroups: $userGroups")
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
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = "Pick Your Created Group",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Dropdown Menu for Groups
                DropdownMenuContent(
                    groups = userGroups,
                    selectedGroup = selectedGroupName,
                    onGroupSelected = { groupId, groupName ->
                        selectedGroup = groupId
                        selectedGroupName = groupName

                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Go Button
            Button(
                onClick = {
                    val group = userGroups.find { it["id"] == selectedGroup }
                    val roles = group?.get("roles")

                    // Handle both flat and nested roles structures
                    val currentUserRole = vm.auth.currentUser?.uid?.let { userId ->
                        when (roles) {
                            is Map<*, *> -> {
                                // Check if roles is a flat Map<String, String>
                                val flatRole = roles[userId] as? String
                                if (flatRole != null) {
                                    flatRole
                                } else {
                                    // Check if roles is a nested Map<String, Map<String, String>>
                                    val nestedRoleMap = roles[userId] as? Map<*, *>
                                    nestedRoleMap?.values?.firstOrNull() as? String
                                }
                            }
                            else -> null
                        }
                    }

                    Log.d("CreateSession", "Roles: $roles, CurrentUserRole: $currentUserRole")
                    if (currentUserRole in listOf("creator", "member")) {
                        navController.navigate("CreateSessionScreen/$selectedGroup")
                    } else {
                        Log.e("PermissionError", "Only creators or members can create sessions")
                        // Optionally show a Toast/Snackbar
                    }
                },
                enabled = selectedGroup != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Go",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

                Spacer(modifier = Modifier.height(24.dp))

            // Delete Group Button
            Button(
                onClick = {
                    selectedGroup?.let { groupId ->
                        vm.deleteGroup(groupId) { success ->
                            if (success) {
                                userGroups = userGroups.filterNot { it["id"] == groupId }
                                navController.navigate(DestinationScreen.Home.route) // Navigate to home after deletion
                            }
                        }
                    }
                },
                enabled = selectedGroup != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Delete Group",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

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
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuContent(
    groups: List<Map<String, Any>>,
    selectedGroup: String,
    onGroupSelected: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedGroup,
            onValueChange = { },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            groups.forEach { group ->
                val groupName = group["name"] as? String ?: "Unnamed Group"
                val groupId = group["id"] as? String ?: ""
                DropdownMenuItem(
                    onClick = {
                        if (groupId.isNotEmpty()) {
                            onGroupSelected(groupId, groupName)
                        } else {
                            Log.e("DropdownMenu", "Invalid groupId")
                        }
                        expanded = false
                    }
                ) {
                    Text(text = groupName, color = Color.Black)
                }
            }
        }
    }
}