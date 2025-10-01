package com.example.smartcook.data.Response

import kotlinx.serialization.Serializable

@Serializable
data class IngredientsResponse(
    val ingredients: List<IngredientDTO>
)

@Serializable
data class IngredientDTO(
    val id: Int,
    val name: String,
    val name_en: String,
    var detected: Boolean,
)