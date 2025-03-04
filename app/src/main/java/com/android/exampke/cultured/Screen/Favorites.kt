package com.android.exampke.cultured.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

@Composable
fun FavoritesScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        PageTitle("Favorites")
        var selectedOptions by remember { mutableStateOf(listOf<String>()) }
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // 로그인하지 않은 경우
            Row(modifier = Modifier.fillMaxWidth()) {
                ExpandableFilter(
                    title = "Artist",
                    options = listOf(
                        "None",
                    ),
                    selectedOptions = selectedOptions,
                    onOptionSelected = { option, selected ->
                        selectedOptions = if (selected) {
                            selectedOptions + option
                        } else {
                            selectedOptions - option
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                ExpandableFilter(
                    title = "Art Type",
                    options = listOf(
                        "None",
                    ),
                    selectedOptions = selectedOptions,
                    onOptionSelected = { option, selected ->
                        selectedOptions = if (selected) {
                            selectedOptions + option
                        } else {
                            selectedOptions - option
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                ExpandableFilter(
                    title = "Location",
                    options = listOf(
                        "None",
                    ),
                    selectedOptions = selectedOptions,
                    onOptionSelected = { option, selected ->
                        selectedOptions = if (selected) {
                            selectedOptions + option
                        } else {
                            selectedOptions - option
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                ExpandableFilter(
                    title = "Theme",
                    options = listOf(
                        "None",
                    ),
                    selectedOptions = selectedOptions,
                    onOptionSelected = { option, selected ->
                        selectedOptions = if (selected) {
                            selectedOptions + option
                        } else {
                            selectedOptions - option
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Log In to set your favorites",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            navController.navigate("login")
                        }
                )
            }

        } else {
            // 로그인한 경우: 즐겨찾기 데이터 불러오기
            var favorites by remember { mutableStateOf<List<Artwork>>(emptyList()) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(currentUser.uid) {
                favorites = fetchFavoritesForUser(currentUser.uid)
                isLoading = false
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (favorites.isEmpty()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    ExpandableFilter(
                        title = "Artist",
                        options = listOf(
                            "None",
                        ),
                        selectedOptions = selectedOptions,
                        onOptionSelected = { option, selected ->
                            selectedOptions = if (selected) {
                                selectedOptions + option
                            } else {
                                selectedOptions - option
                            }
                        },
                        modifier = Modifier.weight(1f)

                    )
                    ExpandableFilter(
                        title = "Art Type",
                        options = listOf(
                            "None",
                        ),
                        selectedOptions = selectedOptions,
                        onOptionSelected = { option, selected ->
                            selectedOptions = if (selected) {
                                selectedOptions + option
                            } else {
                                selectedOptions - option
                            }
                        },
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    ExpandableFilter(
                        title = "Location",
                        options = listOf(
                            "None",
                        ),
                        selectedOptions = selectedOptions,
                        onOptionSelected = { option, selected ->
                            selectedOptions = if (selected) {
                                selectedOptions + option
                            } else {
                                selectedOptions - option
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ExpandableFilter(
                        title = "Theme",
                        options = listOf(
                            "None",
                        ),
                        selectedOptions = selectedOptions,
                        onOptionSelected = { option, selected ->
                            selectedOptions = if (selected) {
                                selectedOptions + option
                            } else {
                                selectedOptions - option
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Add favorites",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

            } else {
                // 필터 상태 변수: 각각의 카테고리별 선택 옵션
                var selectedArtistOptions by remember { mutableStateOf<List<String>>(emptyList()) }
                var selectedArtTypeOptions by remember { mutableStateOf<List<String>>(emptyList()) }
                var selectedLocationOptions by remember { mutableStateOf<List<String>>(emptyList()) }
                var selectedThemeOptions by remember { mutableStateOf<List<String>>(emptyList()) }

                // 즐겨찾기 목록에서 각 필터 옵션의 distinct 값을 추출
                val artistOptions = favorites.map { it.artist_name }.distinct()
                val artTypeOptions = favorites.map { it.artType }.distinct()
                val locationOptions = favorites.map { it.location_country }.distinct()
                val themeOptions = favorites.map { it.theme }.distinct()

                // 필터를 적용하여 즐겨찾기 목록을 필터링
                val filteredFavorites = favorites.filter { artwork ->
                    val matchesArtist = if (selectedArtistOptions.isNotEmpty())
                        selectedArtistOptions.contains(artwork.artist_name) else true
                    val matchesArtType = if (selectedArtTypeOptions.isNotEmpty())
                        selectedArtTypeOptions.contains(artwork.artType) else true
                    val matchesLocation = if (selectedLocationOptions.isNotEmpty())
                        selectedLocationOptions.contains(artwork.location_country) else true
                    val matchesTheme = if (selectedThemeOptions.isNotEmpty())
                        selectedThemeOptions.contains(artwork.theme) else true

                    matchesArtist && matchesArtType && matchesLocation && matchesTheme
                }

                // 필터 UI: 첫 번째 Row (Artist, Art Type)
                Row(modifier = Modifier.fillMaxWidth()) {
                    ExpandableFilter(
                        title = "Artist",
                        options = artistOptions,
                        selectedOptions = selectedArtistOptions,
                        onOptionSelected = { option, selected ->
                            selectedArtistOptions = if (selected) {
                                selectedArtistOptions + option
                            } else {
                                selectedArtistOptions - option
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ExpandableFilter(
                        title = "Art Type",
                        options = artTypeOptions,
                        selectedOptions = selectedArtTypeOptions,
                        onOptionSelected = { option, selected ->
                            selectedArtTypeOptions = if (selected) {
                                selectedArtTypeOptions + option
                            } else {
                                selectedArtTypeOptions - option
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                // 두 번째 Row (Location, Theme)
                Row(modifier = Modifier.fillMaxWidth()) {
                    ExpandableFilter(
                        title = "Location",
                        options = locationOptions,
                        selectedOptions = selectedLocationOptions,
                        onOptionSelected = { option, selected ->
                            selectedLocationOptions = if (selected) {
                                selectedLocationOptions + option
                            } else {
                                selectedLocationOptions - option
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ExpandableFilter(
                        title = "Theme",
                        options = themeOptions,
                        selectedOptions = selectedThemeOptions,
                        onOptionSelected = { option, selected ->
                            selectedThemeOptions = if (selected) {
                                selectedThemeOptions + option
                            } else {
                                selectedThemeOptions - option
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                // 즐겨찾기 목록 표시

                val rememberScrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalColumnScrollbar(rememberScrollState)
                        .verticalScroll(rememberScrollState)
                        .weight(1f)
                ) {
                    filteredFavorites.forEach { artwork ->
                        // 각 Row에 클릭 가능한 영역을 추가하여 상세 화면으로 이동
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
                                        )
                                    ,
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
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
        AdsSection(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun ExpandableFilter(
    title: String = "Size",
    options: List<String>,
    selectedOptions: List<String>,
    onOptionSelected: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth() // 기본값은 fillMaxWidth()
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = Color(0xFFA4A4A4), shape = RoundedCornerShape(10.dp))
    ) {
        // 필터 탭 헤더: 클릭 시 DropdownMenu를 표시합니다.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .background(Color(0xFFFFFFFF))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
                tint = Color(0xFFA4A4A4)
            )
        }

        // DropdownMenu를 사용하여 필터 옵션들을 오버레이로 표시합니다.
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        val currentlySelected = selectedOptions.contains(option)
                        onOptionSelected(option, !currentlySelected)
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedOptions.contains(option),
                                onCheckedChange = { checked ->
                                    onOptionSelected(option, checked)
                                }
                            )
                            Text(
                                text = option,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}

// Helper function: 현재 사용자의 즐겨찾기 artwork 목록을 Firestore에서 불러오는 함수
suspend fun fetchFavoritesForUser(userId: String): List<Artwork> {
    val db = Firebase.firestore
    val favSnapshot = db.collection("favorites")
        .whereEqualTo("userId", userId)
        .get()
        .await()
    val artworks = mutableListOf<Artwork>()
    for (doc in favSnapshot.documents) {
        val artworkId = doc.getString("artworkId")
        if (artworkId != null) {
            val artDoc = db.collection("artworks").document(artworkId).get().await()
            artDoc.toObject(Artwork::class.java)?.let { artworks.add(it) }
        }
    }
    return artworks
}