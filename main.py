from fastapi import FastAPI, UploadFile, File
from fastapi.responses import JSONResponse
from fastapi.staticfiles import StaticFiles
from PIL import Image
from io import BytesIO
from ultralytics import YOLO
import sqlite3
import uvicorn
from pydantic import BaseModel
from typing import List

app = FastAPI()


app.mount("/images", StaticFiles(directory="images"), name="images")


model = YOLO("best.pt")


BASE_URL = "http://78.107.235.156:8000"
DB_PATH = "project_new.db"


def get_ingredient_ids_by_names(detected_en_names):

    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()


    safe_names = [name.strip().lower() for name in detected_en_names if isinstance(name, str) and name.strip()]
    if not safe_names:
        conn.close()
        return []

    placeholders = ','.join(['?'] * len(safe_names))

    cursor.execute(f"""
        SELECT id FROM product
        WHERE LOWER(name_en) IN ({placeholders})
    """, safe_names)

    ids = [row[0] for row in cursor.fetchall()]
    conn.close()
    return ids


def get_full_recipe(recipe_id, cursor):

    cursor.execute("""
        SELECT name, description, instructions, time,
               total_calories, total_proteins, total_fats, total_carbohydrates,
               path_recipe, ingredients
        FROM recipes
        WHERE id = ?
    """, (recipe_id,))
    r = cursor.fetchone()
    if not r:
        return None

    (name, desc, instr, time, kcal, prot, fat, carb, image, ingredients_str) = r


    try:
        ingredient_ids = [int(i.strip()) for i in ingredients_str.split() if i.strip().isdigit()]
    except Exception:
        ingredient_ids = []


    ingredients = []
    if ingredient_ids:
        placeholders = ",".join(["?"] * len(ingredient_ids))
        cursor.execute(f"""
            SELECT name FROM product
            WHERE id IN ({placeholders})
        """, ingredient_ids)
        ingredients = [row[0] for row in cursor.fetchall()]

    return {
        "id": recipe_id,
        "name": name,
        "description": desc,
        "instructions": instr,
        "time": time,
        "total_calories": kcal,
        "total_proteins": prot,
        "total_fats": fat,
        "total_carbohydrates": carb,
        "path_recipe": f"{BASE_URL}/images/{image}",
        "ingredients": ingredients
    }



def find_recipes(detected_en_names):

    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()

    detected_ids = get_ingredient_ids_by_names(detected_en_names)
    if not detected_ids:
        conn.close()
        return []

    matched_recipes = []

    cursor.execute("SELECT id, ingredients FROM recipes")
    for recipe_id, ingredients_text in cursor.fetchall():
        if not ingredients_text:
            continue

        try:
            recipe_product_ids = [int(i.strip()) for i in ingredients_text.split() if i.strip().isdigit()]
        except Exception:
            continue

        if not recipe_product_ids:
            continue

        match_count = sum(1 for pid in recipe_product_ids if pid in detected_ids)
        match_ratio = match_count / len(recipe_product_ids)

        if match_ratio >= 0.1:
            recipe_data = get_full_recipe(recipe_id, cursor)
            if recipe_data:
                matched_recipes.append(recipe_data)

    conn.close()
    return matched_recipes


@app.post("/upload")
async def upload_image(file: UploadFile = File(...)):
    try:
        image_bytes = await file.read()
        image = Image.open(BytesIO(image_bytes))

        results = model.predict(image, conf=0.5)[0]

        detected = []
        for box in results.boxes:
            cls_id = int(box.cls[0].item())
            name = results.names.get(cls_id)
            if name:
                detected.append(name.strip())

        print("🧠 YOLO обнаружило:", detected)

        # Загружаем все продукты
        conn = sqlite3.connect(DB_PATH)
        cursor = conn.cursor()
        cursor.execute("SELECT id, name, name_en FROM product")
        all_products = cursor.fetchall()
        conn.close()

        # Собираем список для ответа
        ingredient_list = []
        detected_lower = [d.lower() for d in detected]  # нормализуем для поиска
        for prod_id, ru_name, en_name in all_products:
            ingredient_list.append({
                "id": prod_id,
                "name": ru_name,
                "name_en": en_name,
                "detected": en_name.lower() in detected_lower
            })

        return JSONResponse(content={"ingredients": ingredient_list})

    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})



@app.get("/recipes")
def get_all_recipes():

    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()

    cursor.execute("SELECT id FROM recipes")
    all_ids = [row[0] for row in cursor.fetchall()]

    all_recipes = []
    for recipe_id in all_ids:
        r = get_full_recipe(recipe_id, cursor)
        if r:
            all_recipes.append(r)

    conn.close()
    return JSONResponse(content={"recipes": all_recipes})

class MatchRequest(BaseModel):
    ingredients: List[str]

@app.post("/match")
def match_selected_ingredients(request: MatchRequest):
    try:
        selected_ingredients = request.ingredients
        print("📥 От клиента получили ингредиенты:", selected_ingredients)
        recipes = find_recipes(selected_ingredients)
        return JSONResponse(content={"recipes": recipes})
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})


if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)