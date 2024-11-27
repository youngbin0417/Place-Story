package com.example.diaryprogram.data
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class DiaryStatusAdapter : TypeAdapter<DiaryStatus>() {
    override fun write(out: JsonWriter, value: DiaryStatus) {
        out.value(value.name) // Enum의 name을 JSON에 기록
    }

    override fun read(input: JsonReader): DiaryStatus {
        return DiaryStatus.valueOf(input.nextString())
    }
}
