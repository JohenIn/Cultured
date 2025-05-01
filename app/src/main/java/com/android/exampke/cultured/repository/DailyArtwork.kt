package com.android.exampke.cultured.repository

import android.content.Context
import com.android.exampke.cultured.Artwork
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

suspend fun DocumentReference.getOfflineFirst(): DocumentSnapshot? {
    return try {
        // 서버 연동 우선 시도: 네트워크가 있으면 서버, 없으면 캐시
        this.get().await()
    } catch (e: FirebaseFirestoreException) {
        if (e.code == FirebaseFirestoreException.Code.UNAVAILABLE) {
            // 오프라인일 때 캐시 전용으로 재시도
            this.get(Source.CACHE).await()
        } else {
            throw e
        }
    }
}

// 1) Firebase 호출 시 SOURCE.CACHE 만 사용하도록 헬퍼 작성
suspend fun fetchArtworkFromCache(
    db: FirebaseFirestore,
    documentId: String
): Artwork? {
    return try {
        val snap = db.collection("artworks")
            .document(documentId)
            .get(Source.CACHE)   // *오직* 캐시 전용 읽기
            .await()
        snap.toObject(Artwork::class.java)
    } catch (_: Exception) {
        null
    }
}

suspend fun getDailyArtworkFromList(
    artworks: List<Artwork>,
    context: Context
): Artwork? {
    val prefs = context
        .getSharedPreferences("daily_artwork", Context.MODE_PRIVATE)
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        .format(Date())
    val savedDate = prefs.getString("date", null)
    val savedId = prefs.getString("artworkId", null)

    return if (savedDate == today && savedId != null) {
        // 1) 이미 선택된 오늘의 ID가 있으면, 그냥 리스트에서 찾아 리턴
        artworks.find { it.document == savedId }
        // 2) 만약 리스트에 없으면 (예: 캐시엔 남아있지만 메모리엔 없으면) 랜덤으로 고르고 저장
            ?: run {
                artworks.randomOrNull()?.also { art ->
                    prefs.edit()
                        .putString("artworkId", art.document)
                        .putString("date", today)
                        .apply()
                }
            }
    } else {
        // 오늘의 작품이 아직 없으면, 램 리스트에서 랜덤 선택 후 저장
        artworks.randomOrNull()?.also { art ->
            prefs.edit()
                .putString("artworkId", art.document)
                .putString("date", today)
                .apply()
        }
    }
}
