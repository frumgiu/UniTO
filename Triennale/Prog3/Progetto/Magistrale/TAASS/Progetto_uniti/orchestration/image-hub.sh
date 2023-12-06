cd ..

cd eureka-server
docker image build -t fabioferrero/unitiprog:service-discovery-server .
docker image push fabioferrero/unitiprog:service-discovery-server

cd ..

cd api-gateway
docker image build -t fabioferrero/unitiprog:api-gateway .
docker image push fabioferrero/unitiprog:api-gateway
#
cd ..

cd user-microservice
docker image build -t fabioferrero/unitiprog:users-service .
docker image push fabioferrero/unitiprog:users-service

cd ..

cd content-microservice
docker image build -t fabioferrero/unitiprog:contents-service .
docker image push fabioferrero/unitiprog:contents-service

cd ..

cd balon-microservice
docker image build -t fabioferrero/unitiprog:balon-service .
docker image push fabioferrero/unitiprog:balon-service

cd ..

cd telegram-microservice
docker image build -t fabioferrero/unitiprog:telegram-service .
docker image push fabioferrero/unitiprog:telegram-service




