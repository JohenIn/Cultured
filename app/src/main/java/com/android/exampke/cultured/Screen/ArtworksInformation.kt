package com.android.exampke.cultured.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.exampke.cultured.Artwork
import com.android.exampke.cultured.ui.theme.ArtworkDetails
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

    // 지역 val 에 복사
    val art = artwork
    if (art == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // art 는 non-null 이므로 바로 넘길 수 있습니다.
        ArtworkDetails(screenHeight, art, navController)
    }
}

suspend fun fetchArtworkById(artworkId: String): Artwork? {
    val db = Firebase.firestore
    val doc = db.collection("artworks").document(artworkId).get().await()
    return doc.toObject(Artwork::class.java)
}