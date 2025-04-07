package com.example.smartcook.data.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcook.data.RecipePreviewData
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class ItemViewModel : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipePreviewData>>(emptyList())
    val recipes: StateFlow<List<RecipePreviewData>> = _recipes

    init {
        loadRecipesFromServer()
    }

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


    private fun loadRecipesFromServer(url: String = "http://78.107.235.156:8000/recipes") {
        viewModelScope.launch {
            val client = HttpClient(OkHttp) {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }

            try {
                val response = client.get(url).bodyAsText()
                val parsed = Json { ignoreUnknownKeys = true }.decodeFromString<RecipeResponse>(response)
                println("Загружено рецептов: ${parsed.recipes.size}")

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
                        calories = dto.total_calories
                    )
                }

                _recipes.value = mapped
            } catch (e: Exception) {
                println("Ошибка загрузки рецептов: ${e.message}")
            } finally {
                client.close()
            }
        }
    }
}


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