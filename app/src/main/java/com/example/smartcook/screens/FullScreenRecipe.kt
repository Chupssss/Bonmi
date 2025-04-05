package com.example.smartcook.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.smartcook.data.viewModels.ItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenRecipe(
    navController: NavController,
    recipeId: Int,
    itemViewModel: ItemViewModel
) {
    val recipe = itemViewModel.getRecipeById(recipeId)

    if (recipe == null) {
        Text("Рецепт не найден")
        return
    }

    val scroll = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scroll)
        ) {
            // Название блюда
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Время приготовления
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Время",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Время приготовления: ${recipe.time}", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Фото блюда
            Image(
                painter = rememberAsyncImagePainter(recipe.image),
                contentDescription = "Фото рецепта",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Описание:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = recipe.sDescription,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Рецепт:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = recipe.fullDescription,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
            Text(
                text = recipe.total,
                fontSize = 16.sp
            )
        }
    }
}