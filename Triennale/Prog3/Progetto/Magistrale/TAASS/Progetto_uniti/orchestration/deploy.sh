
kubectl apply -f rabbitmq.yml

kubectl apply -f db-users.yml
kubectl apply -f db-contents.yml
kubectl apply -f db-saleads.yml

sleep 2

kubectl apply -f eureka.yml
kubectl apply -f api-gateway.yml

kubectl apply -f users-service.yml
kubectl apply -f contents-service.yml
kubectl apply -f balon-service.yml

sleep 3

kubectl apply -f telegram-service.yml
