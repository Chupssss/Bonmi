package com.example.smartcook.screens

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartcook.data.viewModels.ImagePickerViewModel
import com.example.smartcook.screens.navigation.Screen

@Composable
fun ImagePickerScreen(navController: NavController, model: ImagePickerViewModel = viewModel()){
    val context = LocalContext.current
    val selectedImageBitmap by model.selectedImage.collectAsState()


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            model.setSelectedImage(bitmap)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            model.setSelectedImage(it)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { galleryLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageBitmap != null) {
                Image(
                    bitmap = selectedImageBitmap!!.asImageBitmap(),
                    contentDescription = "Выбранное изображение",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Нажмите, чтобы выбрать изображение")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { cameraLauncher.launch() }) {
            Text("Сделать фото")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Назад")
            }
            Button(
                onClick = {
                    model.uploadSelectedImage(
                        url = "http://78.107.235.156:8000/upload", // Заменить на нужный
                        onSuccess = { result ->
                            println("Успешно: $result")
                            navController.navigate(Screen.Home.route)
                        },
                        onError = { error ->
                            println("Ошибка: ${error.message}")
                        }
                    )
                },
                modifier = Modifier.weight(1f),
                enabled = selectedImageBitmap != null
            ) {
                Text("Далее")
            }
        }
    }
}