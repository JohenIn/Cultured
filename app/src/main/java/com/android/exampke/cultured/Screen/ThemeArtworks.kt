package com.android.exampke.cultured.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import coil3.compose.AsyncImage
import com.android.exampke.cultured.Artwork
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.compose.ui.Alignment
import androidx.navigation.NavController

import kotlinx.coroutines.tasks.await

@Composable
fun ThemeArtworksScreen(theme: String, navController: NavController,) {
    var artworks by remember { mutableStateOf<List<Artwork>>(emptyList()) }

    // 테마가 바뀔 때마다 Firestore에서 데이터를 불러옴
    LaunchedEffect(theme) {
        artworks = fetchArtworksByTheme(theme)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Theme: $theme", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(artworks) { artwork ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        // 선택된 artwork의 document 필드를 route 파라미터로 전달
                        navController.navigate("artworkInformation/${artwork.document}")
                    }) {
                    AsyncImage(
                        model = artwork.imageUrl,
                        contentDescription = artwork.title,
                        modifier = Modifier.size(100.dp)
                            .align(Alignment.Top),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(){
                        Text(
                            text = artwork.title,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = artwork.artist_name,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Row(){
                            Text(
                                text = "${artwork.location_city}, ${artwork.location_country}",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }
            }
        }
    }
}

suspend fun fetchArtworksByTheme(theme: String): List<Artwork> {
    val db = Firebase.firestore
    val snapshot = db.collection("artworks")
        .whereEqualTo("theme", theme)
        .get()
        .await()
    return snapshot.documents.mapNotNull { it.toObject(Artwork::class.java) }
}

