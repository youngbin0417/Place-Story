package com.example.diaryprogram.data

import com.google.android.gms.maps.model.LatLng

data class MarkerData(
    val location: LatLng,
    val diaryId : Long  // 추후 수정
)

fun makeMarker(diaryList: List<DiaryResponseDto>): List<MarkerData> {
    // Convert each diary entry to a MarkerData object
    return diaryList.map { diary ->
        MarkerData(
            location = LatLng(diary.latitude, diary.longitude),
            diaryId = diary.diaryId // Extract the diaryId for the marker
        )
    }
}