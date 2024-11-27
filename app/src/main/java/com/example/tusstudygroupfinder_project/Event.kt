package com.example.tusstudygroupfinder_project


// This is a generic class that holds an event content of type T.
// It's designed to be used for one-time events in an Android app.

// The "out"' "modifier indicates that T is used only as an output (produces values).
open class Event<out T>(private val content: T) {

    // A flag to track whether the event has been handled.
    // It starts as false, indicating the event hasn't been used yet.
    var hasBeenHandled = false
        private set

    // Function to get the content if it hasn't been handled yet.
    // If it has been handled, it returns null. Otherwise, it returns the content
    // and marks the event as handled to prevent multiple uses.
    fun getContentOrNull(): T? {
        return if (hasBeenHandled) {
            // Event has already been handled, return null.
            null
        } else {
            // Event hasn't been handled, mark it as handled and return the content.
            hasBeenHandled = true
            content
        }
    }
}