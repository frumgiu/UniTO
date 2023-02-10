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

<h3>Per eseguire il progetto con docker : </h3>


1) Aprire terminale su directory: _**spikeTaassProject**_
2) Lanciare il file _**build-images.sh**_ con il seguente comando <br>
    <code> sh build-images.sh</code> per creare le immagini docker
3) Per avviare i container usare il comando <code>sh run.sh</code>

    
**Per verificare il server eureka**:  http://localhost:8761/  <br><br>
**Per provare le API**: http://localhost:8080/api/ ....


## Per Fabio
Cose che mancano
* Controlla la questione OneToMany --> l'ho inserita in Board, ma come si gestisce in Balon e HomeBoard che sono singleton?
* Creare modello per annunci Telegram
* Creare lista annunci Telegram in HomeBoard
* Far comuncare con RabbitMQ Telegram-service e content-service
* Leggere i TODO che ho lasciato in giro
* Controllare se ho dimenticato qualcosa in questa lista

Ciaone, divertiti 