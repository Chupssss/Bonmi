package com.example.smartcook.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.smartcook.components.RecipeListScreen
import com.example.smartcook.data.RecipePreviewData
import com.example.smartcook.data.viewModels.ItemViewModel
import com.example.smartcook.screens.navigation.Screen

@Composable
fun FavoriteRecipesScreen(navController: NavController, itemViewModel: ItemViewModel) {
    val context = LocalContext.current

    val recipes = itemViewModel.recipes.collectAsState().value.filter { it.favorite }

    RecipeListScreen(
        recipes = recipes,
        onClick = { recipe: RecipePreviewData ->
            navController.navigate(Screen.FullRecipe.withId(recipe.id))
        },
        onToggleFavorite = { itemViewModel.toggleFavorite(it, context) }
    )
}