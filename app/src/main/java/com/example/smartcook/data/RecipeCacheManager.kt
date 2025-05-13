package com.example.smartcook.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.smartcook.data.itemData.RecipePreviewData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



private val Context.dataStore by preferencesDataStore(name = "recipe_cache")

object RecipeCacheManager {
    private val RECIPES_KEY = stringPreferencesKey("recipes_json")
    suspend fun saveRecipes(context: Context, recipes: List<RecipePreviewData>) {
        withContext(Dispatchers.IO) {
            val json = Json.encodeToString(recipes)
            context.dataStore.edit { preferences ->
                preferences[RECIPES_KEY] = json
            }
        }
    }

    suspend fun loadRecipes(context: Context): List<RecipePreviewData> {
        return withContext(Dispatchers.IO) {
            val json = context.dataStore.data.map { preferences ->
                preferences[RECIPES_KEY] ?: ""
            }.first()

            if (json.isNotBlank()) {
                try {
                    Json.decodeFromString(json)
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }
    }
}
