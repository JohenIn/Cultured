package com.android.exampke.cultured

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import android.content.Context
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

suspend fun getDailyArtwork(context: Context): Artwork? {
    val prefs = context.getSharedPreferences("daily_artwork", Context.MODE_PRIVATE)
    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val savedDate = prefs.getString("date", null)
    val savedId = prefs.getString("artworkId", null)
    val db = Firebase.firestore

    return if (savedDate == todayDate && savedId != null) {
        // 이미 저장된 오늘의 작품 ID가 있으면 해당 작품을 불러옴.
        val doc = db.collection("artworks").document(savedId).get().await()
        doc.toObject(Artwork::class.java)
    } else {
        // 오늘의 작품이 저장되어 있지 않으면, 모든 작품을 불러와서 랜덤 선택.
        val snapshot = db.collection("artworks").get().await()
        val docs = snapshot.documents
        if (docs.isNotEmpty()) {
            val randomDoc = docs.random()
            // 선택된 작품의 문서 ID와 오늘 날짜를 SharedPreferences에 저장
            prefs.edit()
                .putString("artworkId", randomDoc.id)
                .putString("date", todayDate)
                .apply()
            randomDoc.toObject(Artwork::class.java)
        } else {
            null
        }
    }
}
