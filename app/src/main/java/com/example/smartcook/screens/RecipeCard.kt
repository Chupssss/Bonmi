package com.example.smartcook.screens


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.smartcook.R
import com.example.smartcook.data.RecipePreviewData

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun RecipeCard(
    recipe: RecipePreviewData,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }),
        exit = fadeOut()
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
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onToggleFavorite()
                    }) {
                        AnimatedContent(
                            targetState = recipe.favorite,
                            transitionSpec = {
                                (fadeIn() + scaleIn()).togetherWith(fadeOut() + scaleOut())
                            },
                            label = "FavoriteToggle"
                        ) { isFavorite ->
                            Icon(
                                painter = painterResource(
                                    id = if (isFavorite) R.drawable.favorite_24px__1_
                                    else R.drawable.favorite_24px
                                ),
                                contentDescription = null,
                                tint = if (isFavorite) Color.Red else LocalContentColor.current
                            )
                        }
                    }
                }
            }
        }
    }
}