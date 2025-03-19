package com.example.smartcook.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartcook.R
import com.example.smartcook.data.ItemViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteRecipesScreen(model: ItemViewModel = viewModel()) {
    val items by model.recipes.collectAsState()
    val favoriteItems = items.filter { it.favorite }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var isSearchBarVisible by remember { mutableStateOf(true) }
    var searchText by remember { mutableStateOf("") }
    var isScrollButtonVisible by remember { mutableStateOf(false) }
    var lastScrollIndex by remember { mutableStateOf(0) }

    val filteredItems = favoriteItems.filter { it.title.contains(searchText, ignoreCase = true) }


    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                isSearchBarVisible = index == 0
                isScrollButtonVisible = index > lastScrollIndex
                lastScrollIndex = index
            }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            AnimatedVisibility(visible = isSearchBarVisible) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        scope.launch {
                            listState.scrollToItem(0)
                        }
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
                modifier = Modifier
                    .fillMaxSize()
                /*.background(colorResource(id = R.color.light))*/
            ) {
                items(filteredItems) {
                    Card(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .height(224.dp)
                            .fillMaxWidth()
                            .padding(16.dp),
                        //colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.dark))
                    ) {
                        Column {
                            Image(
                                contentDescription = "RecipeImage",
                                painter = painterResource(id = it.image),
                                modifier = Modifier
                                    .height(152.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Row {
                                Text(
                                    text = it.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .weight(1f)
                                        .fillMaxSize()
                                        .align(Alignment.CenterVertically)
                                    //.background(colorResource(id = R.color.dark))
                                )
                                IconButton(onClick = {
                                    model.toggleFavorite(it)
                                }) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (it.favorite) R.drawable.favorite_24px__1_
                                            else R.drawable.favorite_24px
                                        ),
                                        contentDescription = null,
                                        tint = if (it.favorite) Color.Red else LocalContentColor.current
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isScrollButtonVisible, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            SmallFloatingActionButton(onClick = {
                scope.launch { listState.animateScrollToItem(0) }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_upward_24px),
                    contentDescription = null
                )
            }
        }
    }
}