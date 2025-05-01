package com.android.exampke.cultured.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.android.exampke.cultured.Artwork
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Composable
fun rememberArtworks(): State<List<Artwork>> {
    val artworksState = remember { mutableStateOf<List<Artwork>>(emptyList()) }
    val db = Firebase.firestore

    // 1) 캐시에서 한 번만 읽기
    LaunchedEffect(Unit) {
        try {
            val cacheSnapshot = db.collection("artworks")
                .get(Source.CACHE)
                .await()
            artworksState.value = cacheSnapshot.documents
                .mapNotNull { it.toObject(Artwork::class.java) }
        } catch (_: Exception) {
            // 캐시에 데이터가 없거나 캐시 읽기 실패 시 무시
        }
    }

    // 2) 그 다음 스냅샷 리스너로 실시간 동기화
    DisposableEffect(Unit) {
        val registration = db.collection("artworks")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                artworksState.value = snapshot.documents
                    .mapNotNull { it.toObject(Artwork::class.java) }
            }
        onDispose { registration.remove() }
    }

    return artworksState
}

suspend fun getDocumentOfflineFirst(
    docPath: DocumentReference
): DocumentSnapshot? {
    return try {
        // 1) 기본 모드: 서버 연결이 가능하면 서버에서, 아니면 캐시에서
        docPath.get().await()
    } catch (e: FirebaseFirestoreException) {
        if (e.code == FirebaseFirestoreException.Code.UNAVAILABLE) {
            // 2) 오프라인(UNAVAILABLE) 에러가 나면 캐시 전용으로 재시도
            docPath.get(Source.CACHE).await()
        } else {
            throw e  // 다른 에러는 그대로 던지기
        }
    }
}