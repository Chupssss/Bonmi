from ultralytics import YOLO
from googletrans import Translator
import sqlite3
conn = sqlite3.connect("project_new.db")  # Укажите точный путь к вашей базе, если она не в текущей папке
cursor = conn.cursor()

model_path = "best.pt"
model = YOLO(model_path)
class_names = model.names  # Словарь с номерами и названиями классов
results = model("test.jpg")
translator = Translator()
for result in results:
    result.show()
    for box in result.boxes:
        class_id = int(box.cls.item())
        class_name = class_names.get(class_id, "Неизвестный класс")
        confidence = float(box.conf.item())
        if confidence > 0.49:
            translated_row = ''
            if 's' in class_name:
                class_name = class_name[:-1]
            translated_row = [translator.translate(class_name, src='en', dest='ru').text]
            cursor.execute("SELECT id FROM product WHERE name = ?", (*translated_row,))
            product = cursor.fetchone()
            print(product[0])
            cursor.execute("INSERT INTO found (product_id)  VALUES (?)", (product[0],))
conn.commit()