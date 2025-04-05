package com.example.smartcook.screens.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.smartcook.R
import com.example.smartcook.data.viewModels.ItemViewModel
import com.example.smartcook.data.NavItem
import com.example.smartcook.screens.FavoriteRecipesScreen
import com.example.smartcook.screens.HomeScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, itemViewModel: ItemViewModel) {

    val NavItemsList = listOf(
         NavItem("Поиск", R.drawable.home_24px),
         NavItem("Избранные", R.drawable.favorite_24px)
    )

    var selectedIndex by remember {
        mutableStateOf(0)
    }

    Scaffold (
        Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.ImagePicker.route) },modifier = Modifier
                .padding(10.dp)) {
                Icon(painter = painterResource(id = R.drawable.photo_camera_24px), contentDescription = null)
            }
        },
        bottomBar = {
            NavigationBar {
                NavItemsList.forEachIndexed { index, navItem -> NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = {
                              selectedIndex = index
                    },
                    icon = { 
                           Icon(painter = painterResource(id = navItem.icon), contentDescription = "Icon")
                    },
                    label = { Text(text = navItem.label)}) }
                /*FloatingActionButton(onClick = { navController.navigate("imagePicker") },modifier = Modifier
                    .padding(10.dp)) {
                    Icon(painter = painterResource(id = R.drawable.photo_camera_24px), contentDescription = null)
                }*/
            }
        }
        ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            when(selectedIndex){
                0-> HomeScreen(navController, itemViewModel)
                1-> FavoriteRecipesScreen(navController, itemViewModel)
            }
        }
    }
}