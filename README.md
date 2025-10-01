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
│ └── project_new.db # База рецептов
│
├── images/ # Пример картинок к рецептам
│
├── models/ # Заглушка (см. README.md)
│ └── README.md
│
└── README.md

````

---

## 🚀 Быстрый запуск

### 🐍 Локально (FastAPI)

```bash
cd backend
pip install -r requirements.txt
uvicorn main:app --reload
```
Откроется на `http://localhost:8000`

### 🐳 Через Docker

> Требуется установленный Docker

```bash
cd backend
docker build -t bonmi-app:latest .
docker run -d -p 8000:8000 --name bonmi_backend \
  -v $(pwd)/best.pt:/app/best.pt \
  --env-file .env \
  bonmi-app:latest
```

### ⚡ Быстрый запуск через скрипт `deploy.sh`

```bash
cd backend
chmod +x deploy.sh
./deploy.sh
```

Скрипт:

- Удаляет старые контейнеры и образы
    
- Чистит volume и кэш
    
- Собирает backend-образ
    
- Запускает backend-контейнер с моделью и переменными окружения
    

---

## 🤖 YOLOv8

Установка:

```bash
pip install ultralytics
```

Пример запуска модели:

```bash
yolo task=detect mode=predict model=model/best.pt source=path/to/image.jpg
```

---

## 🧠 Технологии

- YOLOv8 (Ultralytics)
    
- FastAPI
    
- Kotlin
    
- SQLite + JSON
    
- Google Colab

- Docker
    

---

## 👥 Команда проекта

- **Евгений Папчинский** — Data Scientist, ML-YOLOv8, Backend, DevOps
    
- **Егор Канатов** — Android-developer (Kotlin)
    
- **Булат Гибадуллин** — Data Engineer (YOLO dataset & recipe DB)
    
- **Сергей Косыгин** — QA, UX
    
---

## 📄 Лицензия

Проект создан в рамках учебной проектной деятельности.  
Открыт для дальнейшего развития и доработки.
