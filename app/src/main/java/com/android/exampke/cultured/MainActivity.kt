package com.android.exampke.cultured

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.exampke.cultured.Screen.ArtworkInformationScreen
import com.android.exampke.cultured.Screen.FavoritesScreen
import com.android.exampke.cultured.Screen.LogInScreen
import com.android.exampke.cultured.Screen.NavigateScreen
import com.android.exampke.cultured.Screen.SettingScreen
import com.android.exampke.cultured.Screen.ThemeArtworksScreen
import com.android.exampke.cultured.Screen.TodayScreen
import com.android.exampke.cultured.ui.theme.CulturedTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        enableEdgeToEdge()
        setContent {
            CulturedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {// NavController 생성
                    val navController = rememberNavController()
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = { BottomNavBar(navController = navController) }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "today",
                            modifier = Modifier
                                .padding(innerPadding)
                                .navigationBarsPadding()
                        ) {
                            composable("today") { TodayScreen(navController = navController) }
                            composable("navigate") { NavigateScreen(navController = navController) }
                            composable("favorites") { FavoritesScreen(navController = navController) }
                            composable("setting") { SettingScreen(navController = navController) }
                            composable("themeArtworks/{theme}") { backStackEntry ->
                                val theme = backStackEntry.arguments?.getString("theme") ?: ""
                                ThemeArtworksScreen(theme = theme, navController = navController)
                            }
                            composable("artworkInformation/{artworkId}") { backStackEntry ->
                                val artworkId =
                                    backStackEntry.arguments?.getString("artworkId") ?: ""
                                ArtworkInformationScreen(
                                    artworkId = artworkId,
                                    navController = navController
                                )
                            }
                            composable("login") { LogInScreen(navController = navController) }
                        }
                    }
                }
            }
        }
    }
}
