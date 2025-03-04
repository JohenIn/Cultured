package com.android.exampke.cultured.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.android.exampke.cultured.Artwork
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun rememberArtworks(): State<List<Artwork>> {
    val artworksState = remember { mutableStateOf<List<Artwork>>(emptyList()) }
    val db = Firebase.firestore

    DisposableEffect(Unit) {
        // "artworks" 컬렉션의 스냅샷 리스너 등록
        val registration = db.collection("artworks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // 에러 처리 (필요한 경우 로깅 또는 에러 상태 업데이트)
                    return@addSnapshotListener
                }
                snapshot?.let {
                    // 각 문서를 Artwork 객체로 변환하여 state에 저장
                    artworksState.value = it.documents.mapNotNull { doc ->
                        doc.toObject(Artwork::class.java)
                    }
                }
            }
        onDispose {
            registration.remove() // Composable이 사라질 때 리스너 해제
        }
    }
    return artworksState
}

@Composable
fun rememberUniqueThemes(): State<List<String>> {
    val db = Firebase.firestore
    val themesState = remember { mutableStateOf<List<String>>(emptyList()) }

    DisposableEffect(Unit) {
        val registration = db.collection("artworks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // 에러 처리
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val themes = it.documents.mapNotNull { doc ->
                        doc.getString("theme")
                    }.distinct()
                    themesState.value = themes
                }
            }
        onDispose {
            registration.remove()
        }
    }
    return themesState
}
