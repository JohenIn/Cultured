package com.android.exampke.cultured

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// 네비게이션 아이템 모델 정의
data class NavItem(val route: String, val label: String, val icon: ImageVector)

// 하단 네비게이션 바 컴포저블
@Composable
fun BottomNavBar(navController: NavController) {

    val vibrate = rememberVibrate()

    val navItems = listOf(
        NavItem("today", "Today", Icons.Outlined.Home),
        NavItem("navigate", "Navigate", Icons.Outlined.Search),
        NavItem("favorites", "Favorites", Icons.Outlined.FavoriteBorder),
        NavItem("setting", "Setting", Icons.Outlined.Settings)
    )

    // 현재 백스택 엔트리를 관찰하여 현재 route를 가져옴
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFFF0F0F0),
        modifier = Modifier.height(50.dp)
    ) {
        navItems.forEach { item ->
            val isSelected = currentRoute == item.route


            // NavigationBar는 Row로 구성되어 있으므로, 각 아이템에 weight를 주어 균등하게 배분합니다.
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable (interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true, color = Color.LightGray),){
                        navController.navigate(item.route)
                        vibrate()
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) Color(0xFF000000) else Color(0xFFAAAAAA),
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        text = item.label,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        color = if (isSelected) Color(0xFF000000) else Color(0xFFAAAAAA)
                    )
                }
            }
        }
    }
}

@Composable
fun rememberVibrate(): () -> Unit {
    val context = LocalContext.current
    // context가 변경되지 않도록 remember로 묶어줍니다.
    val vibrator = remember(context) {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    return {
        val vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(vibrationEffect)
    }
}
