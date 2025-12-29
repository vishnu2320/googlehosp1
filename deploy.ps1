# Deploy Hospital Management System to Kubernetes

Write-Host "🚀 Starting Deployment..."

# 1. Namespace
kubectl apply -f k8s/namespace.yaml

# 2. Database
Write-Host "📦 Deploying Database..."
kubectl apply -f k8s/postgres-secret.yaml
kubectl apply -f k8s/postgres-pvc.yaml
kubectl apply -f k8s/postgres-statefulset.yaml
kubectl apply -f k8s/postgres-service.yaml

# 3. Backend
Write-Host "⚙️ Deploying Backend..."
kubectl apply -f k8s/backend-service.yaml
kubectl apply -f k8s/backend-deployment.yaml

# 4. Frontend
Write-Host "🎨 Deploying Frontend..."
kubectl apply -f k8s/frontend-service.yaml
kubectl apply -f k8s/frontend-deployment.yaml

# 5. Ingress
Write-Host "🌐 Configuring Ingress..."
kubectl apply -f k8s/ingress.yaml

Write-Host "✅ Deployment manifest applied!"
Write-Host "ℹ️ Run 'kubectl get pods -n hospital-system -w' to monitor progress."
