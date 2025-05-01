package com.android.exampke.cultured.Screen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.repository.getDailyArtworkFromList
import com.android.exampke.cultured.repository.rememberArtworks
import com.android.exampke.cultured.ui.theme.ArtworkDetails
import com.google.android.gms.ads.AdSize
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Composable
fun TodayScreen(navController: NavController) {
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var todayArtwork by remember { mutableStateOf<Artwork?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val db = Firebase.firestore

    LaunchedEffect(Unit) {
        // 1) SharedPreferences에서 오늘 선택된 ID 조회
        val prefs = context.getSharedPreferences("daily_artwork", Context.MODE_PRIVATE)
        val savedId = prefs.getString("artworkId", null)

        todayArtwork = if (savedId != null) {
            // 2) *오직 캐시 전용*으로 문서 조회
            try {
                val snapshot = db.collection("artworks")
                    .document(savedId)
                    .get(Source.CACHE)
                    .await()
                snapshot.toObject(Artwork::class.java)
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }
        isLoading = false
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        todayArtwork?.let {
            ArtworkDetails(screenHeight, it, navController)
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("불러올 작품이 없습니다.", color = Color.Gray)
        }
    }
}


fun getAdaptiveAdSize(context: Context): AdSize {
    val displayMetrics = context.resources.displayMetrics
    // 화면 너비 픽셀 값 구하기
    val adWidthPixels = displayMetrics.widthPixels
    // density를 고려하여 dp 값 계산
    val adWidth = (adWidthPixels / displayMetrics.density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
}