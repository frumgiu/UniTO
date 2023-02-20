cd ..

cd eureka-server
docker image build -t fabioferrero/unititaass:service-discovery-server .
docker image push fabioferrero/unititaass:service-discovery-server

cd ..

cd api-gateway
docker image build -t fabioferrero/unititaass:api-gateway .
docker image push fabioferrero/unititaass:api-gateway
#
cd ..

cd user-microservice
docker image build -t fabioferrero/unititaass:users-service .
docker image push fabioferrero/unititaass:users-service

cd ..

cd content-microservice
docker image build -t fabioferrero/unititaass:contents-service .
docker image push fabioferrero/unititaass:contents-service

cd ..

cd balon-microservice
docker image build -t fabioferrero/unititaass:balon-service .
docker image push fabioferrero/unititaass:balon-service

cd ..

cd telegram-microservice
docker image build -t fabioferrero/unititaass:telegram-service .
docker image push fabioferrero/unititaass:telegram-service




