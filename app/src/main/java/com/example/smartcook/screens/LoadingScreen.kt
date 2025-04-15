package com.example.smartcook.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        RiveLoadingAnimation(modifier = Modifier.size(200.dp))
    }
}