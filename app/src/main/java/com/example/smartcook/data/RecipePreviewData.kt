package com.example.smartcook.data

data class RecipePreviewData(
    val id: Int,
    val title: String,
    val image: String = "",
    var favorite: Boolean = false,
    val time: String = "30 минут",
    val sDescription: String,
    val fullDescription: String,
    val ingredients: List<String> = emptyList(),
    val total: String,
    /*val protein: String,
    val fats: String,
    val carbohydartes: String,*/
)
