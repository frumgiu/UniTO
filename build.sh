./mvnw -DskipTests=true clean package

cd demoRabbitSender
docker build -t demotelegramapi .

cd ../demoRAbbitListener
docker build -t listenrabbit .

cd ..