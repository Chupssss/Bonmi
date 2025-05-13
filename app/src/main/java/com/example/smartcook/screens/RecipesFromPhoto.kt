package com.example.smartcook.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartcook.data.itemData.RecipePreviewData
import com.example.smartcook.data.RiveLoadingAnimation
import com.example.smartcook.data.viewModels.ImagePickerViewModel
import com.example.smartcook.data.viewModels.ItemViewModel
import com.example.smartcook.screens.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesFromPhotoScreen(
    navController: NavController,
    recipes: List<RecipePreviewData>,
    isLoading: Boolean,
    imagePickerViewModel: ImagePickerViewModel,
    itemViewModel: ItemViewModel,
    context: Context
) {
    BackHandler(enabled = true) {
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Найденные рецепты", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Main.route)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->

        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(32.dp))
                RiveLoadingAnimation(modifier = Modifier.size(360.dp))
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Загрузка рецептов...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else if (recipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Ничего не найдено ☹️")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            navController.navigate(Screen.FullRecipe.withId(recipe.id))
                        },
                        onToggleFavorite = {
                            imagePickerViewModel.toggleFavorite(recipe.id)
                            itemViewModel.toggleFavorite(recipe, context)
                        }
                    )
                }
            }
        }
    }
}
