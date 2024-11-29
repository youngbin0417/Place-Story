package com.example.diaryprogram.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var userId by mutableStateOf("")
        private set

    fun setUsername(newUsername: String) {
        userId = newUsername
    }
}
