package com.android.exampke.cultured.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.android.exampke.cultured.Artwork
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

@Composable
fun ArtworkDetails(
    screenHeight: Dp,
    artwork: Artwork?,
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

    // 이미지 사이즈 관련 상태
    val maxImageSize = screenHeight / 3
    val minImageSize = 0.dp
    var currentImageSize by remember { mutableStateOf(maxImageSize) }
    var imageScale by remember { mutableFloatStateOf(1f) }

    // 스크롤 상태를 rememberScrollState로 관리 (이 값을 NestedScrollConnection에서 사용)
    val scrollState = rememberScrollState()

    // 스크롤에 따른 이미지 크기 조절
    val nestedScrollConnection = remember(scrollState, currentImageSize) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y

                // 콘텐츠가 스크롤되지 않을 때 (maxValue==0) 음의 delta는 차단하고, 양의 delta는 허용합니다.
                if (scrollState.maxValue == 0 && delta < 0f) {
                    return Offset.Zero
                }

                // 이미지가 이미 최소 크기에 있고 음의 delta가 발생하거나,
                // 최대 크기에 있고 양의 delta가 발생하면 차단합니다.
                if ((currentImageSize == minImageSize && delta < 0f) ||
                    (currentImageSize == maxImageSize && delta > 0f)) {
                    return Offset.Zero
                }

                val previousSize = currentImageSize
                currentImageSize = (currentImageSize + delta.dp).coerceIn(minImageSize, maxImageSize)
                val consumed = currentImageSize - previousSize
                imageScale = currentImageSize / maxImageSize
                return Offset(0f, consumed.value)
            }
        }
    }

    // 전체 화면에 scroll 및 nestedScroll 적용
    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .verticalScroll(scrollState)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 이미지 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(currentImageSize)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                        },
                    contentScale = ContentScale.FillHeight
                )
            }
            // 작품 정보 영역
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
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
                                    toggleFavorite(artwork, currentUser.uid) { newState, newCount ->
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
            }
            // 구분선
            HorizontalDivider(
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            // 설명 영역 (전체 스크롤 영역에 포함됨)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 20.dp)
            ) {
                Text(
                    text = artwork!!.description,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
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

@Composable
fun ArtworksCard(
    navController: NavController,
    artwork: Artwork
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clip(RoundedCornerShape(10.dp))
            .padding(horizontal = 20.dp)
            .border(
                width = 0.5.dp,
                color = Color(0xFFD9D9D9),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                navController.navigate("artworkInformation/${artwork.document}")
            }
    ) {
        // 이미지 영역: 전체 너비의 50%를 차지, 높이는 텍스트 영역에 맞춰짐
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .drawBehind {
                    val strokeWidth = 0.5.dp.toPx()
                    // 오른쪽 경계에 선 그리기
                    drawLine(
                        color = Color(0xFFD9D9D9),
                        start = Offset(x = size.width + strokeWidth, y = 0f),
                        end = Offset(
                            x = size.width + strokeWidth,
                            y = size.height
                        ),
                        strokeWidth = strokeWidth
                    )
                }
        ) {
            AsyncImage(
                model = artwork.imageUrl,
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .clip(
                        RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 10.dp
                        )
                    ),
                contentScale = ContentScale.Fit // 원본 비율을 유지하며 최대한 꽉 채움
            )
        }
        // 텍스트 영역: 나머지 50%를 차지하며, 텍스트가 줄바꿈될 경우 Row의 높이가 이에 맞게 증가됨
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 15.dp, top = 15.dp)
        ) {
            Text(
                text = artwork.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Text(
                text = artwork.artist_name,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = artwork.artType,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = artwork.medium,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "${artwork.location_museum}, ${artwork.location_city}, ${artwork.location_country}",
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}
