package com.example.diaryprogram.data

import com.google.android.gms.maps.model.LatLng

data class MarkerData(
    val location: LatLng,
    val diary : String  // 추후 수정
)

fun makeMarker(){

}