package com.android.exampke.cultured.Screen

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


@Composable
fun NavigateScreen(navController: NavController) {
    var themes by remember { mutableStateOf<List<String>>(emptyList()) }

    // Firestore에서 테마 목록을 비동기로 가져옵니다.
    LaunchedEffect(Unit) {
        themes = fetchUniqueThemes()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PageTitle("Navigate")
        CustomSearchBar()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            themes.forEach { theme ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 20.dp)
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD9D9D9))
                        .clickable {
                            navController.navigate("themeArtworks/$theme")

                        }
                ) {
                    Text(
                        theme,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(15.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        AdsSection(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun PageTitle(title: String) {
    Text(
        title,
        fontSize = 48.sp,
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

suspend fun fetchUniqueThemes(): List<String> {
    val db = Firebase.firestore
    val snapshot = db.collection("artworks").get().await()

    // 각 문서의 "theme" 필드를 가져오고, null이 아닌 값들을 모아서 distinct 처리합니다.
    return snapshot.documents.mapNotNull { it.getString("theme") }.distinct()
}