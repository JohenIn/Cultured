package com.android.exampke.cultured.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.getDailyArtwork


@Composable
fun TodayScreen() {
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
        ArtworkDetails(screenHeight, artwork, false)
    }
}

@Composable
fun ArtworkDetails(
    screenHeight: Dp,
    artwork: Artwork?,
    showAds: Boolean = true
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // 이미지 영역 (AsyncImage로 Firestore의 imageUrl 사용)
        Box(
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                .heightIn(max = screenHeight / 3)
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            AsyncImage(
                model = artwork!!.imageUrl,
                contentDescription = "Image of the art",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit // 이미지 비율에 맞춰 채우기
            )
        }
        // 작품 정보 영역
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(IntrinsicSize.Max),
        ) {

            Text(
                text = artwork!!.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = artwork!!.artist_name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${artwork!!.artist_birthYear}-${artwork!!.artist_deathYear}, ${artwork!!.artist_nationality})",
                    fontSize = 16.sp
                )
            }
            Text(text = artwork!!.material, fontSize = 16.sp)
            Text(text = artwork!!.productionYear, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(8.dp))


            Text("Department", fontSize = 16.sp, color = Color.Gray)
            Text(text = artwork!!.artType, fontSize = 16.sp)
            Text("Medium", fontSize = 16.sp, color = Color.Gray)
            Text(text = artwork!!.medium, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Location", fontSize = 16.sp, color = Color.Gray)
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = artwork!!.location_museum,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = ", ${artwork!!.location_city}, ${artwork!!.location_country}",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "1,456", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )


            }

        }     // 구분선
        HorizontalDivider(
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 15.dp)
        )

        // 설명 영역 (스크롤 가능)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
                .weight(1f)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = artwork!!.description,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        if (showAds) {
            AdsSection(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
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