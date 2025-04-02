package com.example.smartcook.data

data class RecipePreviewData(
    val id: Int,
    val title: String,
    val image: Int,
    var favorite: Boolean,
    val time: String,
    val sDescription: String,
    val fullDescription: String,
/*    val total: String,
    val protein: String,
    val fats: String,
    val carbohydartes: String,*/
)
