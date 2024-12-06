package com.example.diaryprogram.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base64

object utils {
    fun base64ToImage(base64String: String): Bitmap? {
        return try {
            val decodedString = Base64.decodeBase64(base64String)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Composable
    fun DisplayImage(base64String: String, size: Dp) {
        val bitmap = base64ToImage(base64String)
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Diary Image",
                modifier = Modifier
                    .size(size),
                contentScale = ContentScale.Fit // 이미지 크기 조정 방식

            )
        }
    }
}