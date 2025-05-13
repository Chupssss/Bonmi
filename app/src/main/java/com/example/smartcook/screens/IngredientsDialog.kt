package com.example.smartcook.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.example.smartcook.data.itemData.Ingredient
import io.ktor.websocket.Frame.Text

@Composable
fun IngredientsDialog(
    ingredients: List<Ingredient>,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    val ingredientsState = remember { mutableStateListOf<Ingredient>().apply { addAll(ingredients) } }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = { Text(text = "Выберите ингредиенты") },
        text = {
            LazyColumn {
                items(ingredientsState.size) { index ->
                    val ingredient = ingredientsState[index]
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = ingredient.detected,
                            onCheckedChange = { isChecked ->
                                ingredientsState[index] = ingredient.copy(detected = isChecked)
                            }
                        )
                        Text(text = ingredient.name)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val selected = ingredientsState.filter { it.detected }.map { it.name_en }
                onConfirm(selected as List<String>)
            }) {
                Text("Найти рецепты")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}