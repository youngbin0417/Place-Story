package com.example.diaryprogram.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.diaryprogram.screen.MapScreen
import com.google.android.gms.maps.model.LatLng

@Composable
fun MainPage(navHostController: NavHostController) {
    Column {

    }
}

@Composable
fun MapButton(onClick: () -> Unit,
              modifier: Modifier = Modifier,) {
    Button(onClick = onClick,
        modifier = Modifier) {
        val location = LatLng(37.5638, 126.9844)
        MapScreen(location)
    }
}