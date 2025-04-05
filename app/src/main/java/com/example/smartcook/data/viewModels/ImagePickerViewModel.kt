package com.example.smartcook.data.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcook.data.uploadImageToServer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImagePickerViewModel: ViewModel() {

    private val _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage: StateFlow<Bitmap?> = _selectedImage

    fun setSelectedImage(bitmap: Bitmap?) {
        _selectedImage.value = bitmap
    }

    fun uploadSelectedImage(
        url: String,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            val bitmap = _selectedImage.value
            if (bitmap != null) {
                try {
                    val result = uploadImageToServer(bitmap, url)
                    onSuccess(result)
                } catch (e: Exception) {
                    onError(e)
                }
            } else {
                onError(IllegalStateException("Изображение не выбрано"))
            }
        }
    }

}