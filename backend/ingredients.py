import re
import sqlite3

def clean_ingredient(ingredient):
    """Удаляет граммовку и единицы измерения из ингредиента."""
    return re.split(r"\s*[-–]\s*|\s*\d+.*", ingredient, maxsplit=1)[0].strip()


def get_product_id(cursor, product_name):
    cursor.execute("SELECT id FROM product WHERE name = ?", (product_name,))
    result = cursor.fetchone()
    if result:
        return result[0]

    cursor.execute("SELECT id FROM product WHERE name LIKE ?", (f"%{product_name}%",))
    result = cursor.fetchone()
    if result:
        return result[0]

    shortened_name = re.sub(r'(ы|и|а|я|о|е|ё|ую|ую|ый|ий|ая|ое|ые|ов|ев|ей|ыми|ими|ах|ях|еть|ить|ать|ять|уть)$', '',
                            product_name)
    cursor.execute("SELECT id FROM product WHERE name LIKE ?", (f"%{shortened_name}%",))
    result = cursor.fetchone()
    if result:
        return result[0]
    return None

def save_recipe(recipe_name, ingredients):
    """Сохраняет название рецепта и его ингредиенты в БД, связывая их с recipe_id и id_product."""
    conn = sqlite3.connect("project_new.db")
    cursor = conn.cursor()

    # Получаем id рецепта или создаем новый
    cursor.execute("SELECT id FROM recipes WHERE name = ?", (recipe_name,))
    recipe = cursor.fetchone()

    if recipe:
        recipe_id = recipe[0]
    else:
        cursor.execute("INSERT INTO recipes (name) VALUES (?)", (recipe_name,))
        recipe_id = cursor.lastrowid
    ingredients = sorted_masiv(ingredients)
    spisok_id = []
    for ingredient in ingredients:
        cleaned_ingredient = clean_ingredient(ingredient)
        product_id = get_product_id(cursor, cleaned_ingredient)
        if not product_id:
            print(cleaned_ingredient)
        if product_id:

            cursor.execute("INSERT INTO ingredients (id_recipe, id_product) VALUES (?, ?)",
                           (recipe_id, product_id,))
            spisok_id.append(product_id)

    str_spisokid = ''
    for i in set(spisok_id):
        str_spisokid += str(i)
        str_spisokid += ' '
    cursor.execute("UPDATE recipes SET ingredients = ? WHERE id = ?",
                   (str_spisokid, recipe_id))
    conn.commit()
    conn.close()

def process_ingredients(file_path):
    """Читает файл с рецептами и ингредиентами, очищает названия и сохраняет в БД"""
    with open(file_path, "r", encoding="utf-8") as file:
        lines = file.readlines()

        recipe_name = None
        ingredients = []
        for line in lines:
            line = line.strip()
            if not line:
                continue

            # Проверяем, является ли строка названием рецепта
            if re.match(r"^[А-Яа-яA-Za-z\s,]+$", line) and not line.startswith("-"):
                # Сохраняем предыдущий рецепт перед переходом к новому
                if recipe_name and ingredients:
                    save_recipe(recipe_name, ingredients)
                    ingredients = []

                recipe_name = line

            elif line.startswith("-"):
                ingredient = line.lstrip("-").strip()
                ingredients.append(ingredient)

        if recipe_name and ingredients:
            save_recipe(recipe_name, ingredients)


import re

def sorted_masiv(ingredients):
    cleaned_ingredients = []
    for ingredient in ingredients:
        # Удаляем слово перед скобками и текст внутри скобок
        cleaned_ingredient = re.sub(r'\S+\s*\(.*?\)', '', ingredient).strip()
        # Если есть запятые, разбиваем на части и добавляем каждую отдельно
        if ',' in cleaned_ingredient:
            parts = cleaned_ingredient.split(',')
            for part in parts:
                cleaned_ingredients.append(part.strip().capitalize())  # Убираем пробелы и делаем с заглавной буквы
        else:
            cleaned_ingredients.append(cleaned_ingredient.capitalize())

    return cleaned_ingredients



process_ingredients("ingredients.txt")
