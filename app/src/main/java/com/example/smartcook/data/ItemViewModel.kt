package com.example.smartcook.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.find



class ItemViewModel: ViewModel() {
    private val _recipes = MutableStateFlow<List<RecipePreviewData>>(TestObjects.recipes)
    val recipes: StateFlow<List<RecipePreviewData>> = _recipes

    fun toggleFavorite(item: RecipePreviewData) {
        _recipes.value = _recipes.value.map {
            if (it.id == item.id) it.copy(favorite = !it.favorite) else it
        }
    }
    fun getRecipeById(id: Int): RecipePreviewData? {
        return recipes.value.find { it.id == id }
    }

    fun toggleFavorite(id: Int) {
        _recipes.value = _recipes.value.map { recipe ->
            if (recipe.id == id) recipe.copy(favorite = !recipe.favorite) else recipe
        }
    }
}