package com.example.smartcook


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartcook.data.ItemViewModel
import com.example.smartcook.screens.FavoriteRecipesScreen
import com.example.smartcook.screens.FullScreenRecipe
import com.example.smartcook.screens.HomeScreen
import com.example.smartcook.screens.ImagePickerScreen
import com.example.smartcook.screens.navigation.MainScreen
import com.example.smartcook.screens.navigation.Screen
import com.example.smartcook.ui.theme.SmartCookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCookTheme {
                val navController = rememberNavController()
                val itemViewModel: ItemViewModel = viewModel()

                NavHost(navController = navController, startDestination = Screen.Main.route) {
                    composable(Screen.Main.route) {
                        MainScreen(navController, itemViewModel)
                    }
                    composable(Screen.Home.route) {
                        HomeScreen(navController, itemViewModel)
                    }
                    composable(Screen.Favorite.route) {
                        FavoriteRecipesScreen(itemViewModel)
                    }
                    composable(Screen.ImagePicker.route) {
                        ImagePickerScreen(navController)
                    }
                    composable(Screen.FullRecipe.route) { backStackEntry ->
                        val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
                        if (recipeId != null) {
                            FullScreenRecipe(navController, recipeId, itemViewModel)
                        } else {
                            Text("Ошибка: ID рецепта не передан")
                        }
                    }
                }
            }
        }
    }
}
