package com.example.smartcook.data.itemData

import kotlinx.serialization.Serializable

@Serializable
data class RecipePreviewData(
    val id: Int,
    val title: String,
    val image: String = "",
    var favorite: Boolean = false,
    val time: String?,
    val sDescription: String,
    val fullDescription: String,
    val ingredients: List<String> = emptyList(),
    val calories: Float,
    val proteins: String,
    val fats: String,
    val carbohydrates: String,
)