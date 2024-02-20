#!/bin/bash

# Step 1: Set up role-based access control (RBAC)
echo "Setting up RBAC..."
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml

# Step 2: Deploy NGINX Ingress Controller using Deployment or DaemonSet
echo "Deploying NGINX Ingress Controller..."
kubectl apply -f https://raw.githubusercontent.com/nginxinc/kubernetes-ingress/v3.4.2/deploy/crds.yaml

# Confirm NGINX Ingress Controller is running
echo "Confirming NGINX Ingress Controller is operational..."
kubectl get pods --namespace=ingress-nginx

# Alternatively, for a LoadBalancer service, use the following (adjust for your cloud provider if necessary):
# kubectl apply -f https://raw.githubusercontent.com/nginxinc/kubernetes-ingress/v3.4.2/deployments/service/loadbalancer.yaml

echo "NGINX Ingress Controller deployment complete."
