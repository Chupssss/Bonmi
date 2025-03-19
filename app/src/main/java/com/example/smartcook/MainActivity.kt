package com.example.smartcook


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartcook.data.ItemViewModel
import com.example.smartcook.screens.FavoriteRecipesScreen
import com.example.smartcook.screens.FullScreenRecipe
import com.example.smartcook.screens.HomeScreen
import com.example.smartcook.screens.ImagePickerScreen
import com.example.smartcook.screens.MainScreen
import com.example.smartcook.ui.theme.SmartCookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCookTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "main"){
                    composable(route = "main") {
                        MainScreen(navController)
                    }
                    composable(route = "home"){
                        HomeScreen(ItemViewModel(), navController)
                    }
                    composable(route = "favorite"){
                        FavoriteRecipesScreen()
                    }
                    composable(route = "imagePicker") {
                        ImagePickerScreen(navController)
                    }
                    composable(route = "fullRecipe/{recipeId}") { backStackEntry ->
                        val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
                        if (recipeId != null) {
                            FullScreenRecipe(navController, recipeId)
                        } else {
                            Text("Ошибка: ID рецепта не передан")
                        }
                    }
                }
            }
        }
    }
}
