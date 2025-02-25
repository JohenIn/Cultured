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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FavoritesScreen() {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Favorites",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
        )

        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Artist, Title, Art Type or whatever you want") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .padding(horizontal = 10.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            val categories = listOf(
                "Trending",
                "Baroque",
                "Bauhaus",
                "Expressionism",
                "Fauvism",
                "Impressionism",
                "Pop Art",
                "Realism",
                "Renaissance",
                "Romanticism",
                "Surrealism",
                "Symbolism",
                "Abstract",
                "Art Nouveau",
                "Cubism",
                "Dadaism",
                "Minimalism",
                "Post-Impressionism",
                "Pre-Raphaelite",
                "Rococo",
                "Ukiyo-e"
            )

            for (category in categories) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 20.dp)
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFD9D9D9))
                        .clickable {}
                ) {
                    Text(
                        category,
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .background(
                    Color(0xFFE9D9D9)
                )
                .height(60.dp)
        ) {
            Text("광고삽입예정", modifier = Modifier.align(Alignment.Center))

        }
    }
}