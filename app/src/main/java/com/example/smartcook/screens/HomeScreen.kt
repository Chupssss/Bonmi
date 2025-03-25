package com.example.smartcook.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartcook.components.RecipeListScreen
import com.example.smartcook.data.ItemViewModel
import com.example.smartcook.data.RecipePreviewData

@Composable
fun HomeScreen(navController: NavController, model: ItemViewModel = viewModel()) {
    val recipes = model.recipes.collectAsState().value

    RecipeListScreen(
        recipes = recipes,
        onClick = { recipe: RecipePreviewData ->
            navController.navigate("fullRecipe/${recipe.id}")
        },
        onToggleFavorite = { model.toggleFavorite(it) }
    )
}