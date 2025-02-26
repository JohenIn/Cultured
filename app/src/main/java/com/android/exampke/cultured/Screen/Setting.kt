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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
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
fun SettingScreen() {

    Column(modifier = Modifier.fillMaxSize()) {
        PageTitle("Setting")

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp)  )
Text("Sign In", modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp)  )
        Text("Language", modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp)  )
Text("Notification", modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp)  )
        Text("Font Size", modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp)  )
        Text("Theme", modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp)  )

        Spacer(modifier = Modifier.weight(1f))
        AdsSection(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}