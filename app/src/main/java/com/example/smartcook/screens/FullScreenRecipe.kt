package com.example.smartcook.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Рецепт не найден")
        }
        return
    }

    val scroll = rememberScrollState()

    Scaffold(
        topBar = { /* Убираем TopAppBar — кнопка будет поверх изображения */ }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scroll)
        ) {

            // 📸 Блок с изображением и кнопкой назад
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)) {

                Image(
                    painter = rememberAsyncImagePainter(recipe.image),
                    contentDescription = "Фото рецепта",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), shape = CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🧾 Контент в карточке
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Время",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Время приготовления: ${recipe.time}", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ингредиенты:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    recipe.ingredients.forEach {
                        Text(text = "• $it", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Описание:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = recipe.sDescription,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Рецепт:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = recipe.fullDescription,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}