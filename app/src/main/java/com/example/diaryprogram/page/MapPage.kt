package com.example.diaryprogram.page

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.diaryprogram.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
// 프론트 완료
@Composable
fun MapPage(navHostController: NavHostController, initialPosition: LatLng) {
    val context = LocalContext.current
    var zoom by remember { mutableFloatStateOf(20f) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, zoom)
    } // 현재 위치를 초기값으로

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,

        ) {
            // marker 추가할것

        }

        IconButton(
            onClick = { navHostController.navigate("main") },
            modifier = Modifier
                .width(50.dp)
                .height(100.dp)
                .align(Alignment.TopStart)
                .padding(top = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back Button"
            )
        }
    }
}

fun getBitmapDescriptor(context: Context, drawableResId: Int, size: Int): BitmapDescriptor? {
    val drawable = context.getDrawable(drawableResId) ?: return null
    val bitmap = drawableToBitmap(drawable, size)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

// Drawable을 Bitmap으로 변환하며 크기를 조정
fun drawableToBitmap(drawable: Drawable, size: Int): Bitmap {
    val originalBitmap = if (drawable is BitmapDrawable) {
        drawable.bitmap
    } else {
        // Drawable을 Bitmap으로 변환
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    }

    // 크기 조정
    return Bitmap.createScaledBitmap(
        originalBitmap,
        size, // 너비
        size, // 높이
        false // 필터링 여부
    )
}