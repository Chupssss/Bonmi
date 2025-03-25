package com.example.smartcook.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartcook.R
import com.example.smartcook.data.ItemViewModel
import com.example.smartcook.data.RecipePreviewData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenRecipe(
    navController: NavController,
    recipeId: Int,
    itemViewModel: ItemViewModel)
{

    val recipe = itemViewModel.getRecipeById(recipeId)

    if (recipe == null) {
        Text("Рецепт не найден")
        return
    }

    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(Modifier.fillMaxWidth()) {
                Text("Заголовок", style = MaterialTheme.typography.headlineLarge)
                IconButton(onClick = {
                    itemViewModel.toggleFavorite(recipe.id)
                }) {
                    Icon(
                        painter = painterResource(
                            id = if (recipe.favorite) R.drawable.favorite_24px__1_
                            else R.drawable.favorite_24px
                        ),
                        contentDescription = null,
                        tint = if (recipe.favorite) Color.Red else LocalContentColor.current
                    )
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.shlepa),
                contentDescription = "recipePreview",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(224.dp)
            )
        }
    }
}