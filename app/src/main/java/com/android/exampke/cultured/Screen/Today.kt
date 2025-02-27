package com.android.exampke.cultured.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.R
import com.android.exampke.cultured.fetchArtwork


@Composable
fun TodayScreen() {
    var artwork by remember { mutableStateOf<Artwork?>(null) }

    // Firestore에서 데이터를 불러옴
    LaunchedEffect(Unit) {
        fetchArtwork { result ->
            artwork = result
        }
    }

    if (artwork == null) {
        // 로딩 중
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // 데이터가 불러와졌다면 화면 구성
        Column(modifier = Modifier.fillMaxSize()) {
            // 이미지 영역 (AsyncImage로 Firestore의 imageUrl 사용)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
            ) {
                AsyncImage(
                    model = artwork!!.imageUrl,
                    contentDescription = "Image of the art",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            // 작품 정보 영역
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.62f)
                        .background(Color.White)
                ) {
                    Text(text = artwork!!.artist.name, fontSize = 16.sp)
                    Text(
                        text = "${artwork!!.artist.nationality}, ${artwork!!.artist.birthYear} - ${artwork!!.artist.deathYear}",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(text = artwork!!.title, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = artwork!!.productionYear, fontSize = 14.sp)
                    }
                    Text(text = artwork!!.material, fontSize = 14.sp)
                }
                Column(modifier = Modifier.weight(0.38f)) {
                    Text(text = artwork!!.artType, fontSize = 16.sp)
                    Text(text = artwork!!.medium, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(text = artwork!!.location.museum, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = artwork!!.location.city, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = artwork!!.location.country, fontSize = 14.sp)
                    }
                    Row {
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
                }
            }
            // 구분선
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(1.dp)
                    .background(Color.LightGray)
            )
            // 설명 영역 (스크롤 가능)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp)
                    .weight(1f)
            ) {
                Text(
                    text = artwork!!.description,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
            // 광고 영역 (예시)
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