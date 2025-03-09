package com.android.exampke.cultured.Screen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.repository.getDailyArtworkFromList
import com.android.exampke.cultured.repository.rememberArtworks
import com.android.exampke.cultured.ui.theme.ArtworkDetails
import com.google.android.gms.ads.AdSize

@Composable
fun TodayScreen(navController: NavController) {
    val context = LocalContext.current
    val artworks by rememberArtworks()
    var artwork by remember { mutableStateOf<Artwork?>(null) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // 전체 artworks 목록이 준비되면 오늘의 artwork 선택 (재조회 없이 캐시된 데이터 사용)
    LaunchedEffect(artworks) {
        if (artworks.isNotEmpty()) {
            artwork = getDailyArtworkFromList(artworks, context)
        }
    }

    if (artwork == null) {
        // 로딩 중
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // 데이터가 불러와졌다면 화면 구성
        ArtworkDetails(screenHeight, artwork, navController)
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