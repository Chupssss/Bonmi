package com.example.smartcook.data.viewModels

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

private const val favoritesFileName = "favorites.json"

fun loadFavoriteIds(context: Context): List<Int> {
    val file = File(context.filesDir, favoritesFileName)
    if (!file.exists()) return emptyList()
    return try {
        val json = file.readText()
        Json.decodeFromString(json)
    } catch (e: Exception) {
        emptyList()
    }
}

fun saveFavoriteIds(context: Context, ids: List<Int>) {
    val json = Json.encodeToString(ids)
    File(context.filesDir, favoritesFileName).writeText(json)
}