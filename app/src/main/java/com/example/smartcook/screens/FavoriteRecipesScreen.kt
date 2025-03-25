package com.example.smartcook.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.smartcook.data.ItemViewModel
import com.example.smartcook.components.RecipeListScreen

@Composable
fun FavoriteRecipesScreen(itemViewModel: ItemViewModel) {
    val recipes = itemViewModel.recipes.collectAsState().value.filter { it.favorite }

    RecipeListScreen(
        recipes = recipes,
        onClick = { /* можно добавить переход к полному рецепту */ },
        onToggleFavorite = { itemViewModel.toggleFavorite(it) }
    )
}