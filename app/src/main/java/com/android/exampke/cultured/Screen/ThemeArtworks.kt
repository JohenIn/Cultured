package com.android.exampke.cultured.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.ui.theme.ArtworksCard
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
        PageTitle("Theme: $theme", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .weight(1f)) {
            items(artworks) { artwork ->
                ArtworksCard(navController, artwork)
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

