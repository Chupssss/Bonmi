package com.example.smartcook


import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartcook.data.viewModels.ImagePickerViewModel
import com.example.smartcook.data.viewModels.ItemViewModel
import com.example.smartcook.screens.FavoriteRecipesScreen
import com.example.smartcook.screens.FullScreenRecipe
import com.example.smartcook.screens.HomeScreen
import com.example.smartcook.screens.ImagePickerScreen
import com.example.smartcook.screens.RecipesFromPhotoScreen
import com.example.smartcook.screens.navigation.MainScreen
import com.example.smartcook.screens.navigation.Screen
import com.example.smartcook.ui.theme.SmartCookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemViewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        val imagePickerViewModel = ViewModelProvider(this)[ImagePickerViewModel::class.java]
        itemViewModel.loadRecipesFromServer(applicationContext)
        setContent {
            SmartCookTheme {
                val navController = rememberNavController()


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
                        ImagePickerScreen(navController, imagePickerViewModel)
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
                    composable(Screen.RecipesFromPhoto.route) {
                        RecipeFromPhotoEntryPoint(
                            navController = navController,
                            imagePickerViewModel = imagePickerViewModel,
                            itemViewModel = itemViewModel,
                            context = applicationContext
                        )
                    }
                }
            }
        }
    }
    @Composable
    fun RecipeFromPhotoEntryPoint(
        navController: NavController,
        imagePickerViewModel: ImagePickerViewModel,
        itemViewModel: ItemViewModel,
        context: Context
    ) {
        val recipes by imagePickerViewModel.resultRecipes.collectAsState()

        println("🔁 Кол-во рецептов: ${recipes.size}")

        RecipesFromPhotoScreen(
            navController = navController,
            recipes = recipes,
            onToggleFavorite = { itemViewModel.toggleFavorite(it, context) }
        )
    }
}
