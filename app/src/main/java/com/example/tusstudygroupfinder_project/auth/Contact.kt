package com.example.tusstudygroupfinder_project.auth

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tusstudygroupfinder_project.IgViewModel


/**
 * TUS Study Group Finder Project
 * File: Contact.kt
 * Description: Provides a form for users to send inquiries or messages. Validates user input (name, email, message)
 *              and sends the data to Firebase Firestore for storage.
 * Author: Kristers Rakstins - K00273773
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contact(navController: NavController, vm: IgViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.loadUserDetails()
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
                        text = "Contact Us",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Contact Us",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = name.isEmpty()
                    },
                    label = { Text("Your Name", color = Color.White) },
                    isError = nameError,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
                if (nameError) {
                    Text("Name is required", color = Color.Red, fontSize = 12.sp)
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    },
                    label = { Text("Your Email", color = Color.White) },
                    isError = emailError,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                if (emailError) {
                    Text("Enter a valid email address", color = Color.Red, fontSize = 12.sp)
                }

                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        message = it
                        messageError = message.isEmpty()
                    },
                    label = { Text("Your Message", color = Color.White) },
                    isError = messageError,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(bottom = 16.dp)
                )

                if (messageError) {
                    Text("Message is required", color = Color.Red, fontSize = 12.sp)
                }

                // Submit Button
                Button(
                    onClick = {
                        nameError = name.isEmpty()
                        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        messageError = message.isEmpty()

                        if (!nameError && !emailError && !messageError) {
                            vm.sendInquiry(name, email, message) { success ->
                                if (success) {
                                    Toast.makeText(
                                        context,
                                        "Message sent successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigateUp() // Navigate back
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to send message.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send", color = Color.White)
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