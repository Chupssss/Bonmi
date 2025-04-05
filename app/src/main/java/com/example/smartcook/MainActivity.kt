package com.example.smartcook


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartcook.data.viewModels.ItemViewModel
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
                        FavoriteRecipesScreen(navController, itemViewModel)
                    }
                    composable(Screen.ImagePicker.route) {
                        ImagePickerScreen(navController)
                    }
                    composable(
                        route = Screen.FullRecipe.route,
                        arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val recipeId = backStackEntry.arguments?.getInt("recipeId")
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
