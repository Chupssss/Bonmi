package com.example.smartcook.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.smartcook.data.RecipePreviewData
import com.example.smartcook.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(
    recipe: RecipePreviewData,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .height(224.dp)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(recipe.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(152.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = recipe.title,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                )
                IconButton(onClick = onToggleFavorite) {
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
        }
    }
}