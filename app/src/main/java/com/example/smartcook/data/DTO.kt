package com.example.smartcook.data

import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponse(
    val recipes: List<RecipeDTO>
)

@Serializable
data class RecipeDTO(
    val id: Int,
    val name: String,
    val ingredients: List<String>,
    val instructions: String,
    val description: String,
    val path_recipe: String,
    val time: String?,
    val total_calories: Float,
    val total_proteins: String,
    val total_fats: String,
    val total_carbohydrates: String,
)