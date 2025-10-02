#!/bin/bash

echo "Удаление прошлы контейнеров.."
docker rm -f $(docker ps -aq) 2>/dev/null || true

echo "Удаление прошлых образов.."
docker rmi -f $(docker images -aq) 2>/dev/null || true

echo "Очистка.."
docker system prune -a --volumes -f

echo "Сборка образа.."
docker build -t bonmi-app:latest .

echo "Запуск контейнера.."
docker run -d \
  --name bonmi_backend \
  -p 8000:8000 \
  -v $(pwd)/best.pt:/app/best.pt \
  --env-file .env \
  bonmi-app:latest
