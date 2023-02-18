# ProgettoTAAS
Progetto esame TAAS delgi studeni Ferrero Fabio, Frumento Giulia e Monsterolo Samuele.
A.A. 2022/2023.

L'applicazione sviluppata è un social network per gli studenti universitari di Torino. <br>
Il progetto è strutturato nel seguente modo: 
* Sono presenti 3 microservizi indipendenti tra di loro: balon-microservice, content-microservice e user-microservice. 
    Ognuno di loro è dockerizzato come contenitore "name-service" e utilizza un proprio database.
* Il microservizio telegram-microservice comunica tramite RabbitMQ con content-microservice. Il compito di questo servizio
    è interrogare una canale Telegram informativo di UniTo e riportarne le notizie nella Home generale.
* Eureka e API gateway FINIRE



## Per Fabio
Cose che mancano
* Controlla la questione OneToMany --> l'ho inserita in Board, ma come si gestisce in Balon e HomeBoard che sono singleton?
* Creare modello per annunci Telegram
* Creare lista annunci Telegram in HomeBoard
* Far comuncare con RabbitMQ Telegram-service e content-service
* Leggere i TODO che ho lasciato in giro
* Controllare se ho dimenticato qualcosa in questa lista

Ciaone, divertiti 

## <ins>Per eseguire con _**Docker**_ </ins>:
* Partendo dalla directory principale del progetto, navigare nella directory <code>/docker</code> eseguendo il
  comando <code>cd docker/</code>
  <br>
  <br>
* Una volta posizionati in <code>docker/</code>, eseguire:
  * <code>sh build-images.sh</code> <br>
    per creare la immagini Docker
  <br>
  <br>
  * <code>sh run-containers.sh</code> <br>
    per avviare i container, e quindi avviare l'applicazione.
    * **L'applicazione è pronta per l'utilizzo:**
    * Per verificare _server Eureka_: http://localhost:8761/
    * Per provare/testare le API: http://localhost:8080/api/<PATH_TO_API>
  <br>
  <br>
  * <code>sh remove-images.sh</code> <br>
    per interrompere l'esecuzione dei containers e rimuovere le immagini da Docker. 
    Nel caso in cui non si volessero rimuovere le immagini, anziché il comando precedente
    è possibile eseguire <code>docker compose -p uniti-backend down</code>



## <ins>Per eseguire con _**Kubernetes**_ </ins>:
* Partendo dalla directory principale del progetto, navigare nella directory <code>/orchestration</code> eseguendo il 
  comando <code>cd orchestration/</code>
  <br>
  <br>
* All'interno di questa directory (<code>orchestration/</code>) si trovano i file di configurazione di Kubernetes 
  (ovvero **Deployment / Service / Persistence** ). <br>
  E' possibile eseguire questi file uno a uno con il comando <br>
  <code>kubectl apply -f eureka.yml</code> <br>
  oppure eseguire lo script <code>deploy.sh</code> digitando il comando <br>
  <code>sh deploy.sh</code> <br>
  (questo comando potrebbe variare per sistema operativo. 
  In tal caso informasi come eseguire file <code>.sh</code> nel contesto del SO utilizzato).
  <br>
  <br>
* **<ins>NB</ins>**: <br>
  * Se state utilizzando **Kubernetes** su **Docker** con **minikube** è necessario
    eseguire il comando <br>
    <code>minikube tunnel</code> <br>
    per far in modo che venga effettuato il forward delle porte. <br>
    **Attenzione:** utilizzare Kubernetes con **minikube** su Docker potrebbe limitare la
    memoria, e quindi **_<ins>CAUSARE PROBLEMI NELL'AVVIO</ins>_**  
    <br>
  * Se invece state utilizzando **Kubernetes** su **Docker** attraverso 
    **Docker Desktop** (**_CONSIGLIATO_**) come da foto, allora siete pronti per l'utilizzo
    (documentazione su https://docs.docker.com/desktop/kubernetes/) 
    <img src="./imgKubernetesREADME.png" style="width:70%">
    <br>
    <br>
  * **L'applicazione è pronta per l'utilizzo:**
    * Per verificare _server Eureka_: http://localhost:8761/
    * Per provare/testare le API: http://localhost:8080/api/<PATH_TO_API>
<br>
<br>
* **ALTRI FILES**:
  * <code>remove_deploy.sh</code>: lanciare questo script una volta terminato l'utilizzo 
  dell'applicazione (verranno rimossi i Deployments / Services / PersistenceVolume creati).
  * <code>image-hub.sh</code>: lanciare questo script se si vogliono caricare le immagini
  su **Docker Hub** dopo averle modificate.
