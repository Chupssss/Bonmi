package com.example.smartcook.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartcook.data.ItemViewModel



import com.example.smartcook.components.RecipeListScreen


@Composable
fun FavoriteRecipesScreen(model: ItemViewModel = viewModel()) {
    val recipes = model.recipes.collectAsState().value.filter { it.favorite }

    RecipeListScreen(
        recipes = recipes,
        onClick = { /* можно добавить переход к полному рецепту */ },
        onToggleFavorite = { model.toggleFavorite(it) }
    )
}