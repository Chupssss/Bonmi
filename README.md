# 🍳 Bonmi

Приложение, которое подскажет, что приготовить из того, что есть в холодильнике.  
Просто загрузи фото — и получи рецепты.

---

## 📸 Что делает Bonmi

- Распознаёт продукты на фотографии (YOLOv8)
- Подбирает рецепты из базы по распознанным ингредиентам
- Показывает рецепты с инструкцией, калорийностью и БЖУ
- Позволяет сохранять рецепты в избранное
- Поддерживает ручной поиск рецептов

---

## 🧪 Статус MVP

Минимально жизнеспособная версия уже реализована:

- ✅ Загрузка фото и распознавание продуктов
- ✅ Автоматический подбор подходящих рецептов
- ✅ Лента рецептов и ручной поиск
- ✅ Избранное с быстрым доступом

---

## 📂 Структура проекта

```

Bonmi/
├── app/ # Мобильное приложение (Kotlin)
│ └── src/...
│
├── backend/ # API-сервер (FastAPI)
│ ├── main.py
│ ├── Dockerfile
│ ├── deploy.sh
│ ├── project_new.db # База рецептов
│ └── images/ # Картинки к рецептам
│
├── models/ # YOLOv8 веса, инференс, ноутбуки
│ └── README.md
│
└── README.md

````

---

## 🚀 Быстрый запуск

### Backend (FastAPI)

```bash
cd backend
pip install -r requirements.txt
uvicorn main:app --reload
````

### YOLOv8 (распознавание)

```bash
pip install ultralytics
yolo task=detect mode=predict model=model/yolov8n.pt source=path/to/image.jpg
```

### Frontend (Android)

Открыть папку `frontend/` в Android Studio и собрать проект.

---

## 🧠 Технологии

- YOLOv8 (Ultralytics)
    
- FastAPI
    
- Kotlin
    
- SQLite + JSON
    
- Google Colab
    

---

## 👥 Команда проекта

- **Евгений Папчинский** — Data Scientist, YOLOv8, Backend, DevOps
    
- **Егор Канатов** — Android-developer (Kotlin)
    
- **Булат Гибадуллин** — Data Engineer (YOLO dataset & recipe DB)
    
- **Сергей Косыгин** — QA, UX
    
---

## 📄 Лицензия

Проект создан в рамках учебной проектной деятельности.  
Открыт для дальнейшего развития и доработки.
