package com.android.exampke.cultured.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.getDailyArtwork
import com.android.exampke.cultured.ui.theme.ArtworkDetails


@Composable
fun TodayScreen(navController: NavController) {
    val context = LocalContext.current
    var artwork by remember { mutableStateOf<Artwork?>(null) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // 오늘의 작품을 비동기로 불러옴
    LaunchedEffect(Unit) {
        artwork = getDailyArtwork(context)
    }

    if (artwork == null) {
        // 로딩 중
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // 데이터가 불러와졌다면 화면 구성
        ArtworkDetails(screenHeight, artwork, false, navController)
    }
}

@Composable
fun AdsSection(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.95f)
            .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
            .background(Color(0xFFE9D9D9))
            .height(60.dp)
    ) {
        Text("광고삽입예정", modifier = Modifier.align(Alignment.Center))
    }
}