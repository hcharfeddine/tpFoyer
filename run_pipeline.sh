#!/bin/bash
set -e

echo "=== DevOps Pipeline Simulation ==="

echo "1. Compile Application"
mvn clean compile

echo "2. Run Tests"
mvn test

echo "3. Package Application"
mvn package -DskipTests

echo "4. Build Docker Image"
docker build -t my-app:latest .

echo "5. Deploy to Kubernetes (Simulated)"
echo "kubectl apply -f k8s/mysql-deployment.yaml"
echo "kubectl apply -f k8s/app-deployment.yaml"

echo "=== Pipeline Completed Successfully! ==="
