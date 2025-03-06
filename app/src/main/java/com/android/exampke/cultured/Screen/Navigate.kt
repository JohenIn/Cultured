package com.android.exampke.cultured.Screen

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.android.exampke.cultured.repository.rememberUniqueThemes
import com.google.common.reflect.TypeToken
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun NavigateScreen(navController: NavController) {
    // 캐싱된 테마별 대표 이미지 Map 구독
    val themeImageMapState = rememberThemeRepresentativeImages()
    val themeImageMap = themeImageMapState.value
    // Map의 키가 테마 목록이 됨
    val themes = themeImageMap.keys.toList()

    Column(modifier = Modifier.fillMaxSize()) {
        PageTitle("Navigate")

        //Firebase는 검색이 용이하지 않으므로 다른 대안을 생각해보아야한다.
        //CustomSearchBar()

        val rememberScrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalColumnScrollbar(rememberScrollState)
                .verticalScroll(rememberScrollState)
                .weight(1f)
        ) {
            themes.forEach { theme ->
                ThemeBox(theme = theme, imageUrl = themeImageMap[theme], navController = navController)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        AdsSection(modifier = Modifier.align(Alignment.CenterHorizontally))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar() {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    SearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = {
            // 검색 동작 처리
        },
        active = active,
        onActiveChange = { active = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        placeholder = {
            Text("Artist, Title, Art Type or Whatever you want")
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color(0xFF727272)
            )
        },
        colors = SearchBarDefaults.colors(
            containerColor = Color(0xFFEAEAEA)
        )
    ) {
        // 여기에 검색 제안 등 추가 콘텐츠를 넣을 수 있습니다.
    }
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