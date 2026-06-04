#!/bin/bash
set -e

echo "Starting Sala Lilás..."
docker-compose up -d --build
echo "Backend: http://localhost:8080/api/v1/swagger-ui/index.html"