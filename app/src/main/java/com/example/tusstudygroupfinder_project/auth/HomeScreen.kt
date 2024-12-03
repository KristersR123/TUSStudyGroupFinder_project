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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.tusstudygroupfinder_project.R


@Composable
fun HomeScreen(navController: NavController, vm: IgViewModel) {
    // Call loadUserDetails when the HomeScreen is displayed
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
                        text = "TUS",
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


                    Button(
                        onClick = {
//                            navController.navigate(DestinationScreen.**.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(37.dp)
                            .shadow(1.dp, shape = RoundedCornerShape(4.dp)),
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(
                                red = 0.02916666679084301f,
                                green = 0.028315972536802292f,
                                blue = 0.028315972536802292f,
                                alpha = 1f
                            )
                        )
                    ) {
                Text(
                    text = "Your Study Groups",
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    textDecoration = TextDecoration.None,
                    letterSpacing = 0.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(1f),
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal,
                )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
//                            navController.navigate(DestinationScreen.TimeTable.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(37.dp)
                            .shadow(1.dp, shape = RoundedCornerShape(4.dp)),
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(
                                red = 0.02916666679084301f,
                                green = 0.028315972536802292f,
                                blue = 0.028315972536802292f,
                                alpha = 1f
                            )
                        )
                    ) {
                Text(
                    text = "Scheduled Sessions",
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    textDecoration = TextDecoration.None,
                    letterSpacing = 0.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(1f),
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Normal,
                )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            navController.navigate(DestinationScreen.Contact.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(37.dp)
                            .shadow(1.dp, shape = RoundedCornerShape(4.dp)),
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(
                                red = 0.02916666679084301f,
                                green = 0.028315972536802292f,
                                blue = 0.028315972536802292f,
                                alpha = 1f
                            )
                        )
                    ) {
                        Text(
                            text = "Contact Us",
                            textAlign = TextAlign.Center,
                            fontSize = 25.sp,
                            textDecoration = TextDecoration.None,
                            letterSpacing = 0.sp,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(1f),
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal,
                        )
                    }

                    Spacer(modifier = Modifier.height(350.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(Color.Black)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth() .
                                height(60.dp)
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Home Button
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
//                                        .clickable { navController.navigate(DestinationScreen.**.route) }
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