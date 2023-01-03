#./mvnw clean install
./mvnw -DskipTests=true clean package

cd eureka-server
docker build -t service-discovery-server .

cd ../api-gateway
docker build -t api-gateway .

cd ../content-microservice
docker build -t content-service .

cd ../user-microservice
docker build -t user-service .

cd ..