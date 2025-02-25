package com.android.exampke.cultured.Screen

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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.exampke.cultured.R


@Composable
fun TodayScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(width = 2.dp, color = Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.gourds),
                contentDescription = "image of the art",
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.62f)) {
                Text("Henri Matisse")
                Text("French, 1869 - 1954")
                Text("")
                Row() {
                    Text("Gourds")
                    Text("1916")
                }
                Text("Oil on Canvas")
            }
            Column(modifier = Modifier.weight(0.38f)) {
                Text("Painting")
                Text("Oil painting")
                Text("")
                Row() {
                    Text("MoMA,")
                    Text("NY,")
                    Text("US,")
                }
                Row() {
                    Text("1,456")
                    Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "star")
                    Icon(imageVector = Icons.Outlined.Share, contentDescription = "star")
                }
            }
        }
        HorizontalDivider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 10.dp))
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 10.dp)) {
            Text(stringResource(id = R.string.gourds_description), fontSize = 16.sp, lineHeight = 48.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 10.dp, end = 10.dp).background(Color(0xFFE9D9D9)).height(60.dp)) {
            Text("광고삽입예정", modifier = Modifier.align(Alignment.Center))

        }
    }
}