package com.example.smartcook.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartcook.components.RecipeListScreen
import com.example.smartcook.data.ItemViewModel
import com.example.smartcook.data.RecipePreviewData

@Composable
fun HomeScreen(navController: NavController, itemViewModel: ItemViewModel) {
    val recipes = itemViewModel.recipes.collectAsState().value

    RecipeListScreen(
        recipes = recipes,
        onClick = { navController.navigate("fullRecipe/${it.id}") },
        onToggleFavorite = { itemViewModel.toggleFavorite(it) }
    )
}