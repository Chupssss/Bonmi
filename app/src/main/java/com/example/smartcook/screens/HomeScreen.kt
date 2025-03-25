package com.example.smartcook.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartcook.components.RecipeListScreen
import com.example.smartcook.data.ItemViewModel
import com.example.smartcook.data.RecipePreviewData
import com.example.smartcook.screens.navigation.Screen

@Composable
fun HomeScreen(navController: NavController, itemViewModel: ItemViewModel) {
    val recipes = itemViewModel.recipes.collectAsState().value


    RecipeListScreen(
        recipes = recipes,
        onClick = { recipe: RecipePreviewData ->
            navController.navigate(Screen.FullRecipe.withId(recipe.id)) },
        onToggleFavorite = { itemViewModel.toggleFavorite(it) }
    )
}