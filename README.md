### Progetto di tesi della studentessa Frumento Giulia, matricola 834773.

Server run on port **3080**.

Client run on port **8080**.

vue.config.js is used to have interactions between the two side, 
thanks to a Vue CLI inbuilt functionality. All the calls 
start with /api will be redirected to http://localhost:3080.

---

In the mounted stage the client send a get request to the server and receive 
the data from db.

The advance settings are work in progress and don't trigger any requests.

---
# Development phase

## Client Side
### Project setup
```
cd .\client\
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```
---
## Server Side
### Project setup
```
cd .\server\
npm install
```

### Compiles and hot-reloads for development
```
npm run dev
```