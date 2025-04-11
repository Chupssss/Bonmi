package com.example.smartcook.data.viewModels

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcook.data.RecipePreviewData
import com.example.smartcook.data.RecipeResponse
import com.example.smartcook.data.uploadImageToServer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ImagePickerViewModel : ViewModel() {

    private val _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage: StateFlow<Bitmap?> = _selectedImage

    private val _resultRecipes = MutableStateFlow<List<RecipePreviewData>>(emptyList())
    val resultRecipes: StateFlow<List<RecipePreviewData>> = _resultRecipes

    fun setSelectedImage(bitmap: Bitmap?) {
        _selectedImage.value = bitmap
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Загружает фото на сервер и получает рецепты.
     * Устанавливает признак избранного, если ID совпадает с сохранёнными.
     */
    fun uploadSelectedImage(
        context: Context,
        url: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val bitmap = _selectedImage.value
            if (bitmap == null) {
                onError(IllegalStateException("Изображение не выбрано"))
                return@launch
            }

            try {
                val response = uploadImageToServer(bitmap, url)
                val parsed = Json { ignoreUnknownKeys = true }
                    .decodeFromString<RecipeResponse>(response)

                val favoriteIds = loadFavoriteIds(context)

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
                println("Загружено рецептов с фото: ${mapped.size}")
                mapped.forEach {
                    println("Рецепт: ${it.id} | ${it.title} | ${it.ingredients}")
                }
                _resultRecipes.value = mapped
                onSuccess()

            } catch (e: Exception) {
                onError(e)
            }finally {
                delay(1000)
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(recipeId: Int) {
        _resultRecipes.value = _resultRecipes.value.map {
            if (it.id == recipeId) it.copy(favorite = !it.favorite) else it
        }
    }

}