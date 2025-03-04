package com.android.exampke.cultured.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.android.exampke.cultured.Artwork
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

@Composable
fun ThemeArtworksScreen(theme: String, navController: NavController) {
    var artworks by remember { mutableStateOf<List<Artwork>>(emptyList()) }

    // 테마가 바뀔 때마다 Firestore에서 데이터를 불러옴
    LaunchedEffect(theme) {
        artworks = fetchArtworksByTheme(theme)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        PageTitle(theme)


        LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
            items(artworks) { artwork ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable {
                        // 선택된 artwork의 document 필드를 route 파라미터로 전달
                        navController.navigate("artworkInformation/${artwork.document}")
                    }) {
                    AsyncImage(
                        model = artwork.imageUrl,
                        contentDescription = artwork.title,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.Top),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column() {
                        Text(
                            text = artwork.title,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = artwork.artist_name,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Row() {
                            Text(
                                text = "${artwork.location_city}, ${artwork.location_country}",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }
            }
        }
        AdsSection(modifier = Modifier.align(Alignment.CenterHorizontally))
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

