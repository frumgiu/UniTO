kubectl delete deployment db-contents
kubectl delete deployment db-saleads
kubectl delete deployment db-users

kubectl delete deployment rabbitmq

kubectl delete deployment service-discovery-server
kubectl delete deployment api-gateway
kubectl delete deployment users-service
kubectl delete deployment contents-service
kubectl delete deployment balon-service

kubectl delete deployment telegram-service

kubectl delete service db-contents
kubectl delete service db-users
kubectl delete service db-saleads
kubectl delete service service-discovery-server
kubectl delete service api-gateway
kubectl delete service users-service
kubectl delete service contents-service
kubectl delete service balon-service

kubectl delete service rabbitmq

kubectl delete service telegram-service

#kubectl delete pvc --all
