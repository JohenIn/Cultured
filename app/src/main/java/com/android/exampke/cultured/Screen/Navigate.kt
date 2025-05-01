package com.android.exampke.cultured.Screen

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.repository.rememberArtworks
import com.google.common.reflect.TypeToken
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigateScreen(navController: NavController) {
    // 1) 전체 artworks 구독
    val artworks by rememberArtworks()

    // 2) 테마별 대표 이미지 구독
    val themeImageMap by rememberThemeRepresentativeImages()
    val themes = themeImageMap.keys.toList()

    // 3) 검색 상태
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        PageTitle("Navigate")

        // 4) SearchBar
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = { /* 아이콘 눌렀을 때도 동일하게 active 유지 */ },
            active = active,
            onActiveChange = { active = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            placeholder = { Text("Artist, Title, Art Type") },
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF727272))
            },
            colors = SearchBarDefaults.colors(
                containerColor = Color(0xFFEAEAEA),
            )
        ) {
            // 5) content 슬롯에만 검색 결과를 그림
            if (query.isNotBlank()) {
                val results = artworks.filter { art ->
                    art.title.contains(query, ignoreCase = true) ||
                            art.artist_name.contains(query, ignoreCase = true) ||
                            art.artType.contains(query, ignoreCase = true)
                }
                // 결과가 없으면 빈 영역(공백)
                if (results.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)  // 필요에 따라 max 높이 지정
                    ) {
                        items(results) { art ->
                            SearchResultItem(art, navController)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // 6) 검색이 활성화되지 않았을 때만 테마 박스 표시
        if (!active) {
            val scroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scroll)
                    .verticalColumnScrollbar(scroll)
            ) {
                themes.forEach { theme ->
                    ThemeBox(
                        theme = theme,
                        imageUrl = themeImageMap[theme],
                        navController = navController
                    )
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun PageTitle(title: String, fontSize: TextUnit = 48.sp) {
    Text(
        title,
        fontSize = fontSize,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp)
    )
}

@Composable
private fun SearchResultItem(art: Artwork, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("artworkInformation/${art.document}") }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = art.imageUrl,
            contentDescription = art.title,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Column {
            Text(art.title, fontWeight = FontWeight.Bold)
            Text(art.artist_name, fontSize = 12.sp, color = Color.Gray)
        }    }
}

@Composable
fun rememberThemeRepresentativeImages(): State<Map<String, String>> {
    val themeImageMapState = remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val context = LocalContext.current
    val db = Firebase.firestore

    DisposableEffect(Unit) {
        val registration = db.collection("artworks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // 에러 처리 (필요 시 로깅)
                    return@addSnapshotListener
                }
                snapshot?.let { snap ->
                    // 각 문서에서 theme과 imageUrl이 존재하는 데이터만 추출
                    val pairs = snap.documents.mapNotNull { doc ->
                        val theme = doc.getString("theme")
                        val imageUrl = doc.getString("imageUrl")
                        if (theme != null && imageUrl != null) {
                            theme to imageUrl
                        } else null
                    }
                    // 같은 테마별로 그룹화
                    val grouped: Map<String, List<String>> = pairs.groupBy(
                        keySelector = { it.first },
                        valueTransform = { it.second }
                    )
                    // 각 테마 그룹에서 랜덤으로 하나의 imageUrl 선택
                    val freshMap = grouped.mapValues { entry ->
                        entry.value.randomOrNull() ?: ""
                    }
                    // CoroutineScope를 사용해 백그라운드에서 캐싱 처리
                    kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
                        val dailyMap = getDailyThemeRepresentativeImages(context, freshMap)
                        // 메인 스레드에서 상태 업데이트
                        withContext(Dispatchers.Main) {
                            themeImageMapState.value = dailyMap
                        }
                    }
                }
            }
        onDispose {
            registration.remove()
        }
    }
    return themeImageMapState
}

suspend fun getDailyThemeRepresentativeImages(
    context: Context,
    freshMap: Map<String, String>
): Map<String, String> {
    val prefs = context.getSharedPreferences("daily_theme", Context.MODE_PRIVATE)
    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val savedDate = prefs.getString("date", null)
    val savedJson = prefs.getString("theme_map", null)
    return if (savedDate == todayDate && savedJson != null) {
        // 저장된 JSON을 Map으로 복원
        val type = object : TypeToken<Map<String, String>>() {}.type
        Gson().fromJson(savedJson, type)
    } else {
        // 오늘 날짜로 freshMap을 저장
        val json = Gson().toJson(freshMap)
        prefs.edit().putString("date", todayDate)
            .putString("theme_map", json)
            .apply()
        freshMap
    }
}

@Composable
fun ThemeBox(theme: String, imageUrl: String?, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFD9D9D9)) // 이미지가 로드되지 않으면 fallback 배경
            .clickable {
                navController.navigate("themeArtworks/$theme")
            }
    ) {
        // 이미지가 있으면 배경으로 채워넣기 (Crop 처리하여 박스 크기에 맞게)
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = theme,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        // 테마 이름 표시
        Text(
            text = theme,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(15.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}

@Composable
fun Modifier.verticalColumnScrollbar(
    scrollState: ScrollState,
): Modifier {
    return drawWithContent {
        // Draw the column's content
        drawContent()
        // Dimensions and calculations
        val viewportHeight = this.size.height
        val totalContentHeight = scrollState.maxValue.toFloat() + viewportHeight
        val scrollValue = scrollState.value.toFloat()
        // Compute scrollbar height and position
        val scrollBarHeight =
            (viewportHeight / totalContentHeight) * viewportHeight
        val scrollBarStartOffset =
            (scrollValue / totalContentHeight) * viewportHeight
        // Draw the scrollbar
        drawRoundRect(
            cornerRadius = CornerRadius(10f),
            color = Color.LightGray,
            topLeft = Offset(this.size.width - 30f, scrollBarStartOffset),
            size = Size(3.dp.toPx(), scrollBarHeight)
        )
    }
}