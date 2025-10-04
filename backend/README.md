# 🧠 Bonmi Backend

**Bonmi API**
---

## 🚀 Возможности

- 🔍 Распознавание продуктов по фотографии (`/upload`)
- 📑 Подбор рецептов на основе выбранных ингредиентов (`/match`)
- 📚 Получение всех рецептов (`/recipes`)
- 🗃️ Хранение рецептов и ингредиентов в SQLite
- 🧠 Интеграция с YOLOv8 (Ultralytics)
- 🔐 Использование переменных окружения (`.env`)

---

## ⚙️ Запуск

### 🐍 Локально

```bash
pip install -r requirements.txt
uvicorn main:app --reload
```

👉 Приложение будет доступно на: http://localhost:8000

---

### **🐳 Docker**

```
docker build -t bonmi-app:latest .
docker run -d \
  --name bonmi_backend \
  -p 8000:8000 \
  -v $(pwd)/best.pt:/app/best.pt \
  --env-file .env \
  bonmi-app:latest
```

---

### **📜 Автоматический запуск**

```
chmod +x deploy.sh
./deploy.sh
```

Скрипт deploy.sh:

- Удаляет старые контейнеры и образы
    
- Чистит Docker volume и кеш
    
- Собирает образ и запускает контейнер
    

---

## **🌐 Эндпоинты**

|**Метод**|**URL**|**Описание**|
|---|---|---|
|POST|/upload|Загрузка фото, распознавание продуктов|
|POST|/match|Подбор рецептов по ингредиентам|
|GET|/recipes|Получение всех рецептов|

---

## **🔧 Переменные окружения**

  

Настраиваются через .env или --env-file в Docker.

  

Пример .env.example:

```
BASE_URL=http://localhost:8000
DB_PATH=project_new.db
MODEL_PATH=best.pt
```

---

## **🗂 Структура**

```
backend/
├── main.py          # Основной API-сервер (FastAPI)
├── project_new.db   # База данных SQLite
├── Dockerfile       # Сборка backend-контейнера
├── deploy.sh        # Скрипт авторазвертывания
├── README.md        # Этот файл
└── .env.example     # Пример переменных окружения
```

---

## **🛠️ Зависимости**

- FastAPI

- Uvicorn

- Ultralytics (YOLOv8)
   
- SQLite
   
- Pillow
   
- Python-dotenv


  

Установка:

```
pip install -r requirements.txt
```
