package com.example.smartcook.data

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ImagePickerViewModel: ViewModel() {

    private val _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage: StateFlow<Bitmap?> = _selectedImage

    fun setSelectedImage(bitmap: Bitmap?) {
        _selectedImage.value = bitmap
    }

}