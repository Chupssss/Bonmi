package com.example.smartcook.data.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcook.data.RecipeCacheManager
import com.example.smartcook.data.Response.RecipeResponse
import com.example.smartcook.data.itemData.RecipePreviewData
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


class ItemViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipePreviewData>>(emptyList())
    val recipes: StateFlow<List<RecipePreviewData>> = _recipes


    fun loadRecipesFromServer(
        context: Context,
        url: String = "http://your.server.ip:8000"
    ) {
        viewModelScope.launch {
            val client = HttpClient(OkHttp) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }

            try {
                val responseText = client.get(url).bodyAsText()
                val parsed = Json.decodeFromString<RecipeResponse>(responseText)

                val mapped = parsed.recipes.map { dto ->
                    RecipePreviewData(
                        id = dto.id,
                        title = dto.name,
                        sDescription = dto.description,
                        fullDescription = dto.instructions,
                        ingredients = dto.ingredients,
                        image = dto.path_recipe,
                        carbohydrates = dto.total_carbohydrates,
                        time = dto.time,
                        fats = dto.total_fats,
                        proteins = dto.total_proteins,
                        calories = dto.total_calories,
                        favorite = dto.id in loadFavoriteIds(context)
                    )
                }

                _recipes.value = mapped
                RecipeCacheManager.saveRecipes(context, mapped)
            } catch (e: Exception) {
                println("Ошибка загрузки рецептов: ${e.message}")
                val cachedRecipes = RecipeCacheManager.loadRecipes(context)
                _recipes.value = cachedRecipes
            } finally {
                client.close()
            }
        }
    }

    /**
     * Переключает статус избранного рецепта и сохраняет результат в файл
     */
    fun toggleFavorite(item: RecipePreviewData, context: Context) {
        _recipes.value = _recipes.value.map {
            if (it.id == item.id) it.copy(favorite = !it.favorite) else it
        }
        saveFavoriteIds(context, _recipes.value.filter { it.favorite }.map { it.id })
    }

    /**
     * Получает рецепт по ID
     */
    fun getRecipeById(id: Int): RecipePreviewData? {
        return _recipes.value.find { it.id == id }
    }
}
