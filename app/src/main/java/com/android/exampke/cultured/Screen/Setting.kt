package com.android.exampke.cultured.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun SettingScreen() {
    Box(modifier = Modifier.fillMaxSize()){
        Text(text = "Setting", modifier = Modifier
            .align(Alignment.Center)
            .clickable {})
    }
}