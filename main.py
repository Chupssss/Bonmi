from fastapi import FastAPI, UploadFile, File
from fastapi.responses import JSONResponse
from fastapi.staticfiles import StaticFiles
from PIL import Image
from io import BytesIO
from ultralytics import YOLO
import sqlite3
import uvicorn

app = FastAPI()

# Раздаём папку images по адресу /images
app.mount("/images", StaticFiles(directory="images"), name="images")

model = YOLO("best.pt")

BASE_URL = "http://78.107.235.156:8000"  # Подставь сюда свой IP или домен
DB_PATH = "project_new.db"


def get_ingredient_ids_by_names(detected_ingredients):
    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()

    placeholders = ','.join('?' for _ in detected_ingredients)
    cursor.execute(f"""
        SELECT id FROM product
        WHERE LOWER(name) IN ({placeholders})
    """, [name.lower() for name in detected_ingredients])

    ids = [row[0] for row in cursor.fetchall()]
    conn.close()
    return ids


def get_full_recipe(recipe_id, cursor):
    cursor.execute("""
        SELECT name, description, instructions,
               total_calories, total_proteins, total_fats, total_carbohydrates, path_recipe
        FROM recipes
        WHERE id = ?
    """, (recipe_id,))
    r = cursor.fetchone()
    if not r:
        return None

    name, desc, instr, kcal, prot, fat, carb, image = r

    # Ингредиенты
    cursor.execute("""
        SELECT p.name FROM ingredients i
        JOIN product p ON p.id = i.id_product
        WHERE i.id_recipe = ?
    """, (recipe_id,))
    ingredients = [row[0] for row in cursor.fetchall()]

    return {
        "id": recipe_id,
        "name": name,
        "description": desc,
        "instructions": instr,
        "total_calories": kcal,
        "total_proteins": prot,
        "total_fats": fat,
        "total_carbohydrates": carb,
        "path_recipe": f"{BASE_URL}/images/{image}",
        "ingredients": ingredients
    }


def find_recipes(detected_ingredients):
    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()

    detected_ids = get_ingredient_ids_by_names(detected_ingredients)
    if not detected_ids:
        conn.close()
        return []

    matched_recipes = []

    cursor.execute("SELECT id FROM recipes")
    all_recipe_ids = [row[0] for row in cursor.fetchall()]

    for recipe_id in all_recipe_ids:
        cursor.execute("""
            SELECT id_product FROM ingredients
            WHERE id_recipe = ?
        """, (recipe_id,))
        recipe_product_ids = [row[0] for row in cursor.fetchall()]

        if not recipe_product_ids:
            continue

        match_count = sum(1 for pid in recipe_product_ids if pid in detected_ids)
        match_ratio = match_count / len(recipe_product_ids)

        if match_ratio >= 0.7:
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
            name = results.names[cls_id]
            detected.append(name)

        recipes = find_recipes(detected)
        return JSONResponse(content={"recipes": recipes})

    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})


@app.get("/recipes")
def get_all_recipes():
    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()

    cursor.execute("SELECT id FROM recipes")
    all_ids = [row[0] for row in cursor.fetchall()]

    all_recipes = [get_full_recipe(recipe_id, cursor) for recipe_id in all_ids]
    conn.close()

    return JSONResponse(content={"recipes": all_recipes})


if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
