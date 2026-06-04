#!/bin/bash
set -e

echo "WARNING: This will destroy all data in the database."
read -p "Are you sure? (yes/no): " confirm

if [ "$confirm" != "yes"]; then
  echo "Aborted."
  exit 0
fi

echo "Stopping containers..."
docker-compose down -v

echo "Rebuilding..."
docker-compose up -d --build

echo "Database reset complete."