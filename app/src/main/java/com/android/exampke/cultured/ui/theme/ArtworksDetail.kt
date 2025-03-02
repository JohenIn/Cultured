package com.android.exampke.cultured.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.Screen.AdsSection
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

@Composable
fun ArtworkDetails(
    screenHeight: Dp,
    artwork: Artwork?,
    showAds: Boolean = true,
    navController: NavController
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    var isFavorited by remember { mutableStateOf(false) }
    var favoriteCount by remember { mutableStateOf(0L) }
    val context = LocalContext.current

    // 초기 즐겨찾기 상태 및 개수를 Firestore에서 가져옵니다.
    LaunchedEffect(artwork) {
        if (artwork != null) {
            val db = Firebase.firestore

            // 전체 즐겨찾기 수는 항상 불러옴
            val countSnapshot = db.collection("favorites")
                .whereEqualTo("artworkId", artwork.document)
                .get()
                .await()
            favoriteCount = countSnapshot.size().toLong()

            // 현재 사용자가 로그인한 경우에만 개인 즐겨찾기 상태를 불러옴
            if (currentUser != null) {
                val favDocId = "${artwork.document}_${currentUser.uid}"
                val favDoc = db.collection("favorites").document(favDocId).get().await()
                isFavorited = favDoc.exists()
            }
        }
    }

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
                Text(text = favoriteCount.toString(), fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            if (currentUser == null) {
                                navController.navigate("login")
                            } else {
                                toggleFavorite(artwork!!, currentUser.uid) { newState, newCount ->
                                    isFavorited = newState
                                    favoriteCount = newCount
                                }
                            }
                        }
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

fun toggleFavorite(
    artwork: Artwork,
    userId: String,
    onResult: (isFavorited: Boolean, favoriteCount: Long) -> Unit
) {
    val db = Firebase.firestore
    val favDocId = "${artwork.document}_$userId"
    val favRef = db.collection("favorites").document(favDocId)

    favRef.get().addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot.exists()) {
            // 이미 즐겨찾기 상태이면 삭제 처리
            favRef.delete().addOnSuccessListener {
                // 삭제 후 해당 artwork의 즐겨찾기 개수 업데이트
                db.collection("favorites")
                    .whereEqualTo("artworkId", artwork.document)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        onResult(false, snapshot.size().toLong())
                    }
            }
        } else {
            // 즐겨찾기가 아닌 경우, 추가 처리
            val data = mapOf(
                "artworkId" to artwork.document,
                "userId" to userId,
                "timestamp" to FieldValue.serverTimestamp()
            )
            favRef.set(data).addOnSuccessListener {
                // 추가 후 즐겨찾기 개수 업데이트
                db.collection("favorites")
                    .whereEqualTo("artworkId", artwork.document)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        onResult(true, snapshot.size().toLong())
                    }
            }
        }
    }.addOnFailureListener { exception ->
        // 오류 발생 시 기본 상태 반환(변경하지 않음)
        onResult(false, 0)
    }
}