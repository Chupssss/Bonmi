package com.example.smartcook.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartcook.R
import com.example.smartcook.data.RecipePreviewData
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.smartcook.screens.RecipeCard

@Composable
fun RecipeListScreen(
    recipes: List<RecipePreviewData>,
    onClick: (RecipePreviewData) -> Unit,
    onToggleFavorite: (RecipePreviewData) -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    val filteredRecipes = remember(searchText, recipes) {
        recipes.filter { it.title.contains(searchText, ignoreCase = true) }
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var showSearchBar by remember { mutableStateOf(true) }
    var showScrollToTop by remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .collect { offset ->
                showSearchBar = offset < 10
            }
    }
/*
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                showScrollToTop = index > 1
            }
    }*/

    Column(Modifier.fillMaxSize()) {

        AnimatedVisibility(
            visible = showSearchBar,
            enter = expandVertically(),
            exit = shrinkVertically()
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
                        painter = painterResource(id = R.drawable.search_24px),
                        contentDescription = null
                    )
                }
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f)
        ) {
            items(filteredRecipes.size) { index ->
                val recipe = filteredRecipes[index]
                RecipeCard(
                    recipe = recipe,
                    onClick = { onClick(recipe) },
                    onToggleFavorite = { onToggleFavorite(recipe) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // под FAB
            }
        }

/*        if (showScrollToTop) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
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
        }*/
    }
}