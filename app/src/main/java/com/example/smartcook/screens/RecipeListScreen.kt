package com.example.smartcook.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smartcook.R
import com.example.smartcook.data.RecipePreviewData
import com.example.smartcook.screens.RecipeCard
import kotlinx.coroutines.launch

@Composable
fun RecipeListScreen(
    recipes: List<RecipePreviewData>,
    onClick: (RecipePreviewData) -> Unit,
    onToggleFavorite: (RecipePreviewData) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredRecipes = remember(searchText, recipes) {
        recipes.filter { it.title.contains(searchText, ignoreCase = true) }
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showScrollToTop by remember { mutableStateOf(false) }
    var showSearchBar by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .collect { offset ->
                showSearchBar = offset < 10
            }
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                showScrollToTop = index > 1
            }
    }

    Column(Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showSearchBar,
            modifier = Modifier.animateContentSize()
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    scope.launch { listState.scrollToItem(0) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Поиск рецептов...") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.search_24px),
                        contentDescription = null
                    )
                }
            )
        }

        Box(Modifier.weight(1f)) {
            LazyColumn(state = listState) {
                items(filteredRecipes.size) { index ->
                    val recipe = filteredRecipes[index]
                    RecipeCard(
                        recipe = recipe,
                        onClick = { onClick(recipe) },
                        onToggleFavorite = { onToggleFavorite(recipe) }
                    )
                }
            }

            this@Column.AnimatedVisibility(
                visible = showScrollToTop,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = { scope.launch { listState.animateScrollToItem(0) } }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_upward_24px),
                        contentDescription = null
                    )
                }
            }
        }
    }
}