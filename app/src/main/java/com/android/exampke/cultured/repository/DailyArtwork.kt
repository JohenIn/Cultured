package com.android.exampke.cultured.repository

import android.content.Context
import com.android.exampke.cultured.Artwork
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

suspend fun getDailyArtworkFromList(artworks: List<Artwork>, context: Context): Artwork? {
    val prefs = context.getSharedPreferences("daily_artwork", Context.MODE_PRIVATE)
    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val savedDate = prefs.getString("date", null)
    val savedId = prefs.getString("artworkId", null)

    return if (savedDate == todayDate && savedId != null) {
        // 이미 저장된 오늘의 artwork가 목록에 존재하면 그대로 반환
        artworks.find { it.document == savedId } ?: run {
            // 만약 캐시된 목록에 없다면 새로 랜덤 선택
            val randomArtwork = artworks.randomOrNull()
            randomArtwork?.also {
                prefs.edit().putString("artworkId", it.document).putString("date", todayDate)
                    .apply()
            }
        }
    } else {
        // 오늘의 artwork가 저장되어 있지 않으면, 전체 목록에서 랜덤 선택
        val randomArtwork = artworks.randomOrNull()
        randomArtwork?.also {
            prefs.edit().putString("artworkId", it.document).putString("date", todayDate).apply()
        }
    }
}
