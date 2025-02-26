package com.android.exampke.cultured.Screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorFilter.Companion.colorMatrix
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.exampke.cultured.R

@Composable
fun FavoritesScreen() {

    Column(modifier = Modifier.fillMaxSize()) {
        PageTitle("Favorites")
        var selectedOptions by remember { mutableStateOf(listOf<String>()) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ExpandableFilter(
                title = "Artist",
                options = listOf(
                    "Pablo Picasso",
                    "Vincent van Gogh",
                    "Claude Monet",
                    "Leonardo da Vinci",
                    "Salvador Dalí",
                    "Henri Matisse",
                    "Andy Warhol",
                    "Jackson Pollock",
                    "Frida Kahlo",
                    "Rembrandt van Rijn"
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
                    "Painting",
                    "Sculpture",
                    "Architecture",
                    "Pottery",
                    "Drawing",
                    "Printmaking",
                    "Photography",
                    "Digital Art",
                    "Installation Art",
                    "Performance Art"
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ExpandableFilter(
                title = "Location",
                options = listOf(
                    "Italy",
                    "France",
                    "USA",
                    "Germany",
                    "Spain",
                    "United Kingdom",
                    "Japan",
                    "Russia",
                    "Netherlands",
                    "China"
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
                    "Folk Art",
                    "Modernism",
                    "Avant-garde",
                    "Abstract",
                    "Impressionism",
                    "Surrealism",
                    "Cubism",
                    "Expressionism",
                    "Minimalism",
                    "Pop Art"
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
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 0.5.dp,
                        color = Color(0xFFD9D9D9),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gourds),
                    contentDescription = "image of the art",
                )
                Column(modifier = Modifier.padding(start = 15.dp, top = 15.dp)) {
                    Text(
                        "Gourds",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                    )
                    Text(
                        "Henri Matisse",
                        fontSize = 16.sp,
                        color = Color.Black,
                    )
                    Text("Painting", fontSize = 16.sp, color = Color.Black)
                    Text("Oil painting", fontSize = 16.sp, color = Color.Black)
                    Text("MoMA, NY, US", fontSize = 16.sp, color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            repeat(10){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 0.5.dp,
                            color = Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gourds),
                        contentDescription = "image of the art",
                        colorFilter = ColorFilter.colorMatrix(
                            ColorMatrix().apply {
                                setToSaturation(0.1f)
                            })
                    )
                    Column(modifier = Modifier.padding(start = 15.dp, top = 15.dp)) {
                        Text(
                            "Title",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                        )
                        Text(
                            "Artist",
                            fontSize = 16.sp,
                            color = Color.Black,
                        )
                        Text("Art Type", fontSize = 16.sp, color = Color.Black)
                        Text("Medium", fontSize = 16.sp, color = Color.Black)
                        Text("Location", fontSize = 16.sp, color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

            }
            Spacer(modifier = Modifier.height(20.dp))

        }
        AdsSection(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun ExpandableFilter(
    title: String = "Size",
    options: List<String> = listOf("S", "M", "L", "XL"),
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
