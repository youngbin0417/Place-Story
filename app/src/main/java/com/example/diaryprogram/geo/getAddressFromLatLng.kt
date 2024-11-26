package com.example.diaryprogram.geo

import android.content.Context
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context)
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                // "서울시 광진구 화양동" 형태로 반환
                val city = address.adminArea ?: "" // 서울특별시
                val district = address.subAdminArea ?: "" // 광진구
                val dong = address.subLocality ?: "" // 화양동
                "$city $district $dong"
            } else {
                "주소를 찾을 수 없습니다."
            }
        } catch (e: Exception) {
            "주소 변환 중 오류 발생"
        }
    }
}
