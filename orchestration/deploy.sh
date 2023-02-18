kubectl apply -f eureka.yml
kubectl apply -f api-gateway.yml

kubectl apply -f db-users.yml
kubectl apply -f db-contents.yml
kubectl apply -f db-saleads.yml

kubectl apply -f users-service.yml
kubectl apply -f contents-service.yml
kubectl apply -f balon-service.yml
