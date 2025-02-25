package com.android.exampke.cultured

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.exampke.cultured.Screen.FavoritesScreen
import com.android.exampke.cultured.Screen.NavigateScreen
import com.android.exampke.cultured.Screen.SettingScreen
import com.android.exampke.cultured.Screen.TodayScreen
import com.android.exampke.cultured.ui.theme.CulturedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 전체 화면 모드 설정 (상태바, 네비게이션바 숨김)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        val controller = WindowInsetsControllerCompat(window, window.decorView)
//        controller.systemBarsBehavior =
//            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        controller.hide(WindowInsetsCompat.Type.systemBars())

        enableEdgeToEdge()
        setContent {
            CulturedTheme {
                // NavController 생성
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar(navController = navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "today",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("today") { TodayScreen() }
                        composable("navigate") { NavigateScreen() }
                        composable("favorites") { FavoritesScreen() }
                        composable("setting") { SettingScreen() }
                    }
                }
            }
        }
    }
}
