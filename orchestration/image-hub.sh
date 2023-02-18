cd ..

cd eureka-server
docker image build -t fabioferrero/fabio:service-discovery-server .
docker image push fabioferrero/fabio:service-discovery-server

cd ..

cd api-gateway
docker image build -t fabioferrero/fabio:api-gateway .
docker image push fabioferrero/fabio:api-gateway
#
cd ..

cd user-microservice
docker image build -t fabioferrero/fabio:users-service .
docker image push fabioferrero/fabio:users-service

cd ..

cd content-microservice
docker image build -t fabioferrero/fabio:contents-service .
docker image push fabioferrero/fabio:contents-service

cd ..

cd balon-microservice
docker image build -t fabioferrero/fabio:balon-service .
docker image push fabioferrero/fabio:balon-service




