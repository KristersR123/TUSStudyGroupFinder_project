package com.example.tusstudygroupfinder_project.main

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.tusstudygroupfinder_project.IgViewModel


// Composable function for displaying notification messages
@Composable
fun NotificationMessage(vm: IgViewModel) {
    // Retrieve the current notification state from the view model
    val notifState = vm.popupNotification.value

    // Extract the message content from the notification state, if available
    val notifMessage = notifState?.getContentOrNull()

    // Check if a valid notification message exists
    if (notifMessage != null) {
        // Display a short Toast message with the notification content
        Toast.makeText(LocalContext.current, notifMessage, Toast.LENGTH_SHORT).show()
    }
}