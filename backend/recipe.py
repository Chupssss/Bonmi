import sqlite3
import re


conn = sqlite3.connect("project_new.db")
cursor = conn.cursor()


with open('detailed_recipes_10.txt', "r", encoding="utf-8") as file:
    content = file.read()
    recipes = content.split("=" * 50)

    for recipe in recipes:
        if recipe.strip():
            # Извлечение полей с использованием регулярных выражений
            name = re.search(r"Название рецепта: (.+)", recipe).group(1)
            description = re.search(r"Описание: (.+)", recipe).group(1)
            instructions = re.search(r"Инструкция: (.+)", recipe, re.DOTALL).group(1).strip()
            total_calories = float(re.search(r"Калории: (\d+(\.\d+)?) ккал", recipe).group(1))
            total_proteins = float(re.search(r"Белки: (\d+(\.\d+)?) г", recipe).group(1))
            total_fats = float(re.search(r"Жиры: (\d+(\.\d+)?) г", recipe).group(1))
            total_carbohydrates = float(re.search(r"Углеводы: (\d+(\.\d+)?) г", recipe).group(1))

            cursor.execute('''
                INSERT INTO recipes (name, description, instructions, total_calories, total_proteins, total_fats, total_carbohydrates)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            ''', (name, description, instructions, total_calories, total_proteins, total_fats, total_carbohydrates, ))
            conn.commit()




conn.close()
