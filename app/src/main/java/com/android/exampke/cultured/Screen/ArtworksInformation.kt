package com.android.exampke.cultured.Screen

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    // artworkId를 이용해 Firestore에서 상세 정보를 불러오거나,
    // 이미 로컬에 있는 데이터를 활용하여 화면에 표시할 수 있습니다.
    // 간단한 예시:
    if (artwork == null) {
        CircularProgressIndicator()
    } else {
        ArtworkDetails(screenHeight, artwork, navController = navController)
    }

}

suspend fun fetchArtworkById(artworkId: String): Artwork? {
    val db = Firebase.firestore
    val doc = db.collection("artworks").document(artworkId).get().await()
    return doc.toObject(Artwork::class.java)
}