package com.example.smartcook.screens

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartcook.data.RiveLoadingAnimation
import com.example.smartcook.data.viewModels.ItemViewModel
import com.example.smartcook.screens.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(
    navController: NavController,
    itemViewModel: ItemViewModel
) {
    val context = LocalContext.current
    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        itemViewModel.loadRecipesFromServer(context)
        delay(1000)
        isLoaded = true
    }

    LaunchedEffect(isLoaded) {
        if (isLoaded) {
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.LoadingScreen.route) { this.inclusive = true }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        RiveLoadingAnimation(modifier = Modifier.size(360.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Загрузка рецептов...",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}