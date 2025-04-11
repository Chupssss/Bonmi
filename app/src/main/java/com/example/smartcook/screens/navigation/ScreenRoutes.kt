package com.example.smartcook.screens.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object ImagePicker : Screen("imagePicker")
    object RecipesFromPhoto : Screen("recipesFromPhoto")
    object FullRecipe : Screen("fullRecipe/{recipeId}") {
        fun withId(id: Int) = "fullRecipe/$id"
    }
}