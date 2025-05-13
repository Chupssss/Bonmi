package com.example.smartcook.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.smartcook.data.itemData.Ingredient
import io.ktor.websocket.Frame.Text

@Composable
fun IngredientsDialog(
    ingredients: List<Ingredient>,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    // 👇 Локально отслеживаем выбранные ингредиенты
    val ingredientStates = remember {
        ingredients.associate { it.id to mutableStateOf(it.detected) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 500.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                androidx.compose.material3.Text(
                    text = "Выберите ингредиенты",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Скроллируемая область
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(scrollState)
                ) {
                    ingredients.forEach { ingredient ->
                        val checkedState = ingredientStates[ingredient.id]!!

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it }
                            )

                            androidx.compose.material3.Text(
                                text = ingredient.name,
                                modifier = Modifier.padding(start = 8.dp),
                                fontWeight = if (ingredient.detected) FontWeight.Bold else FontWeight.Normal,
                                color = if (ingredient.detected) Color(0xFF3366CC) else Color.Unspecified
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Назад")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val selectedNames = ingredients.filter {
                            ingredientStates[it.id]?.value == true
                        }.map { it.name_en }
                        onConfirm(selectedNames)
                    }) {
                        androidx.compose.material3.Text("Найти")
                    }
                }
            }
        }
    }
}