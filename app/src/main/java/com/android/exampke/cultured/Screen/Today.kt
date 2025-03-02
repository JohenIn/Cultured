package com.android.exampke.cultured.Screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.getDailyArtwork
import com.android.exampke.cultured.ui.theme.ArtworkDetails
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


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

fun getAdaptiveAdSize(context: Context): AdSize {
    val displayMetrics = context.resources.displayMetrics
    // 화면 너비 픽셀 값 구하기
    val adWidthPixels = displayMetrics.widthPixels
    // density를 고려하여 dp 값 계산
    val adWidth = (adWidthPixels / displayMetrics.density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
}

@Composable
fun AdsSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // AndroidView를 사용해 기존 View(AdView)를 Compose에 삽입
    AndroidView(
        modifier = modifier
            .fillMaxWidth(0.95f)
            .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
            .border(0.2.dp, Color.LightGray, RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
            .height(60.dp),
        factory = { ctx ->
            // AdView 생성
            val adView = AdView(ctx).apply {
                adUnitId = "ca-app-pub-3940256099942544/9214589741" // 실제 광고 단위 ID로 교체
                setAdSize(getAdaptiveAdSize(ctx))
                adListener = object : AdListener() {
                    override fun onAdClicked() {
                        // 광고 클릭 시 동작
                    }
                    override fun onAdClosed() {
                        // 광고 닫힐 때 동작
                    }
                    override fun onAdFailedToLoad(adError: com.google.android.gms.ads.LoadAdError) {
                        // 광고 로딩 실패 시 동작
                    }
                    override fun onAdImpression() {
                        // 광고 임프레션 시 동작
                    }
                    override fun onAdLoaded() {
                        // 광고 로드 완료 시 동작
                    }
                    override fun onAdOpened() {
                        // 광고 열릴 때 동작
                    }
                }
            }
            // 광고 요청 생성 및 로드
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
            adView
        }
    )
}