echo "RUNNING docker-compose up -d ... "
echo ""

cd ..

docker-compose up -d rabbitmq
docker-compose up -d db-users
docker-compose up -d db-contents
docker-compose up -d db-saleads
docker-compose up -d pgadmin
docker-compose up -d service-discovery-server
docker-compose up -d api-gateway
docker-compose up -d user-service
docker-compose up -d content-service
docker-compose up -d balon-service

#docker-compose run --name telegram-service telegram-service
