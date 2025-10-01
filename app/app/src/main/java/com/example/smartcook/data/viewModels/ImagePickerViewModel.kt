package com.example.smartcook.data.viewModels

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcook.data.itemData.Ingredient
import com.example.smartcook.data.itemData.RecipePreviewData
import com.example.smartcook.data.Response.IngredientsResponse
import com.example.smartcook.data.Response.RecipeResponse
import com.example.smartcook.data.uploadImageToServer
import com.example.smartcook.data.uploadIngredientsToServer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ImagePickerViewModel : ViewModel() {

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients

    private val _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage: StateFlow<Bitmap?> = _selectedImage

    private val _resultRecipes = MutableStateFlow<List<RecipePreviewData>>(emptyList())
    val resultRecipes: StateFlow<List<RecipePreviewData>> = _resultRecipes

    fun setSelectedImage(bitmap: Bitmap?) {
        _selectedImage.value = bitmap
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun toggleFavorite(id: Int) {
        _resultRecipes.value = _resultRecipes.value.map {
            if (it.id == id) it.copy(favorite = !it.favorite) else it
        }
    }

    fun uploadPhotoAndGetIngredients(
        context: Context,
        url: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val bitmap = _selectedImage.value
                if (bitmap == null) {
                    onError(IllegalStateException("Изображение не выбрано"))
                    _isLoading.value = false
                    return@launch
                }

                val response = uploadImageToServer(bitmap, url)
                val parsed = Json.decodeFromString<IngredientsResponse>(response)

                _ingredients.value = parsed.ingredients.map { dto ->
                    Ingredient(
                        id = dto.id,
                        name = dto.name,
                        name_en = dto.name_en,
                        detected = dto.detected,

                        )
                }

                onSuccess()
            } catch (e: Exception) {
                onError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun uploadSelectedIngredients(
        selectedIngredients: List<String>,
        context: Context,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response =
                    uploadIngredientsToServer(
                        selectedIngredients,
                        "http://78.107.235.156:8000/match"
                    )
                val parsed = Json.decodeFromString<RecipeResponse>(response)

                _resultRecipes.value = parsed.recipes.map { dto ->
                    RecipePreviewData(
                        id = dto.id,
                        title = dto.name,
                        image = dto.path_recipe,
                        sDescription = dto.description,
                        fullDescription = dto.instructions,
                        ingredients = dto.ingredients,
                        calories = dto.total_calories,
                        proteins = dto.total_proteins,
                        fats = dto.total_fats,
                        carbohydrates = dto.total_carbohydrates,
                        time = dto.time,
                        favorite = false
                    )
                }
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}