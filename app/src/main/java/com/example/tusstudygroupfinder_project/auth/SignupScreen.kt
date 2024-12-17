package com.example.tusstudygroupfinder_project.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tusstudygroupfinder_project.DestinationScreen
import com.example.tusstudygroupfinder_project.IgViewModel
import com.example.tusstudygroupfinder_project.R

/**
 * TUS Study Group Finder Project
 * File: SignupScreen.kt
 * Description: Provides the UI for user registration, with input validation for email, username, and password.
 * Author: Kristers Rakstins - K00273773
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController, vm: IgViewModel) {
    val empty by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cpassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var cpasswordVisibility by remember { mutableStateOf(false) }
    var errorE by remember { mutableStateOf(false) }
    var errorU by remember { mutableStateOf(false) }
    var errorP by remember { mutableStateOf(false) }
    var errorCP by remember { mutableStateOf(false) }
    var errorC by remember { mutableStateOf(false) }
    var plength by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF7E6E44)).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (errorU) {
            Text(
                text = "Enter a username",
                color = Color.Red,
                modifier = Modifier.padding(end = 100.dp)
            )
        }
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username") },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                containerColor = Color(0x30FFFFFF),
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.Red,
                focusedTrailingIconColor = Color.White,
                unfocusedTrailingIconColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (errorE) {
            Text(
                text = "Enter email",
                color = Color.Red,
                modifier = Modifier.padding(end = 100.dp)
            )
        }
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (email.isNotEmpty()) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = null,
                        Modifier.clickable { email = empty }
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier.width(300.dp).height(60.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                containerColor = Color(0x30FFFFFF),
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.Red,
                focusedTrailingIconColor = Color.White,
                unfocusedTrailingIconColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (errorP) {
            Text(
                text = "Enter Password",
                color = Color.Red,
                modifier = Modifier.padding(end = 100.dp)
            )
        }
        if (plength) {
            Text(
                text = "Password must be 6 characters",
                color = Color.Red,
                modifier = Modifier.padding(end = 100.dp)
            )
        }
        TextField(
            value = password,
            onValueChange = {
                password = it
                plength = it.length < 6
            },
            label = {
                Text(text = "Password")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_lock_24),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (password.isNotEmpty()) {
                    val visibilityIcon = if (passwordVisibility) {
                        painterResource(id = R.drawable.baseline_visibility_24)
                    } else {
                        painterResource(id = R.drawable.baseline_visibility_off_24)
                    }
                    Icon(
                        painter = visibilityIcon,
                        contentDescription = if (passwordVisibility) "Hide Password" else "Show Password",
                        Modifier.clickable {
                            passwordVisibility = !passwordVisibility
                        }
                    )
                }
            },
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                containerColor = Color(0x30FFFFFF),
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.Red,
                focusedTrailingIconColor = Color.White,
                unfocusedTrailingIconColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (errorCP) {
            Text(
                text = "Password Doesn't Match",
                color = Color.Red,
                modifier = Modifier.padding(end = 100.dp)
            )
        }
        if (errorC) {
            Text(
                text = "Please Confirm Password",
                color = Color.Red,
                modifier = Modifier.padding(end = 100.dp)
            )
        }
        TextField(
            value = cpassword,
            onValueChange = {
                cpassword = it
            },
            label = {
                Text(text = "Confirm Password")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_lock_24),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (cpassword.isNotEmpty()) {
                    val visibilityIcon = if (cpasswordVisibility) {
                        painterResource(id = R.drawable.baseline_visibility_24)
                    } else {
                        painterResource(id = R.drawable.baseline_visibility_off_24)
                    }
                    Icon(
                        painter = visibilityIcon,
                        contentDescription = if (cpasswordVisibility) "Hide Password" else "Show Password",
                        Modifier.clickable {
                            cpasswordVisibility = !cpasswordVisibility
                        }
                    )
                }
            },
            visualTransformation = if (cpasswordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                containerColor = Color(0x30FFFFFF),
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.Red,
                focusedTrailingIconColor = Color.White,
                unfocusedTrailingIconColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFFFD700), Color(0xFFFFD700))
                    )
                )
        ) {
            Button(
                onClick = {
                    var isValid = true

                    // Clear previous errors
                    errorE = false
                    errorP = false
                    errorCP = false
                    errorC = false
                    errorU = false

                    if (username.isEmpty()) {
                        errorU = true
                        isValid = false
                    }
                    if (email.isEmpty()) {
                        errorE = true
                        isValid = false
                    }
                    if (password.isEmpty()) {
                        errorP = true
                        isValid = false
                    }
                    if (password.length < 6) {
                        plength = true
                        isValid = false
                    }
                    if (cpassword.isEmpty()) {
                        errorC = true
                        isValid = false
                    }
                    if (password != cpassword) {
                        errorCP = true
                        isValid = false
                    }


                    if (isValid) {
                        vm.onSignup(email, password, username)
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                modifier = Modifier
                    .width(200.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xff554800), Color(0xff554800))
                        )
                    )
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (vm.signedIn.value) {
                navController.navigate(DestinationScreen.Home.route)
                vm.signedIn.value = false  // Reset the signedIn state after navigation
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

           Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFFD700), Color(0xFFFFD700))
                        )
                    )
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent
                    ),
                    modifier = Modifier
                        .width(300.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xff554800), Color(0xff554800))
                            )
                        )
                ) {
                    Text(
                        text = "Back",
                        color = Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }


@Composable
fun DropdownMenuItem(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .background(color = Color.White) // You can customize the background color
    ) {
        content()
    }
}