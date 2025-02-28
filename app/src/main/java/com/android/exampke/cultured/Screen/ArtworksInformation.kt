package com.android.exampke.cultured.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.android.exampke.cultured.Artwork
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

@Composable
fun ArtworkInformationScreen(artworkId: String, navController: NavController) {
    var artwork by remember { mutableStateOf<Artwork?>(null) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(artworkId) {
        artwork = fetchArtworkById(artworkId)
    }
    // artworkId를 이용해 Firestore에서 상세 정보를 불러오거나,
    // 이미 로컬에 있는 데이터를 활용하여 화면에 표시할 수 있습니다.
    // 간단한 예시:
    Text(text = "Detailed info for artwork: $artworkId")
if (artwork == null) {
    CircularProgressIndicator()
} else {
    ///
    Column(modifier = Modifier.fillMaxSize()) {
        PageTitle(artwork!!.title)
        // 이미지 영역 (AsyncImage로 Firestore의 imageUrl 사용)
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .heightIn(max = screenHeight / 3)
                .clip(RoundedCornerShape(10.dp))
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
        ) {
            AsyncImage(
                model = artwork!!.imageUrl,
                contentDescription = "Image of the art",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit // 이미지 비율에 맞춰 채우기
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
                Text(text = artwork!!.artist_name, fontSize = 16.sp)
                Text(
                    text = "${artwork!!.artist_nationality}, ${artwork!!.artist_birthYear} - ${artwork!!.artist_deathYear}",
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
                    Text(text = artwork!!.location_museum, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = artwork!!.location_city, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = artwork!!.location_country, fontSize = 14.sp)
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
//            AdsSection(modifier = Modifier.align(Alignment.CenterHorizontally))
    }

    ///
}

}

suspend fun fetchArtworkById(artworkId: String): Artwork? {
    val db = Firebase.firestore
    val doc = db.collection("artworks").document(artworkId).get().await()
    return doc.toObject(Artwork::class.java)
}