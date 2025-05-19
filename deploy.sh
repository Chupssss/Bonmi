#!/bin/bash

echo "🧹 Удаляем все контейнеры..."
docker rm -f $(docker ps -aq) 2>/dev/null || true

echo "🧹 Удаляем все образы..."
docker rmi -f $(docker images -aq) 2>/dev/null || true

echo "🧹 Чистим volume и кеш..."
docker system prune -a --volumes -f

echo "🐳 Собираем образ..."
docker build -t bonmi-app:latest .

echo "🚀 Запускаем контейнер..."
docker run -d \
  --name bonmi_backend \
  -p 8000:8000 \
  -v $(pwd)/best.pt:/app/best.pt \
  --env-file .env \
  bonmi-app:latest
