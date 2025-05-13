package com.example.smartcook.data.itemData

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val id: Int,
    val name: String,
    val name_en: String?,
    var detected: Boolean,

)