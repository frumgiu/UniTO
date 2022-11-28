## Progetto di tesi della studentessa Frumento Giulia
### Contact: giuliafrumento23@gmail.com

---

Server run on port **3080**.
Client run on port **8080**.

vue.config.js is used to have interactions between the two side, 
thanks to a Vue CLI inbuilt functionality. All the calls 
start with /api will be redirected to http://localhost:3080.

---

In the mounted stage the client send a get request to the server and receive 
the data from db. Database selects the data inside the map area visible on screen, 
this avoids to load tons of data.

The user can interact with a filter menu' and a data visualization menu' that 
provide three different type of data layers.
With the search bar is possible to search and find a city by his native name (example: Torino for Turin).
The camera will move to the searched location and center the bounding box of the city.

---

It can occur a problem with Deck.GL dependencies sometimes (after they realise a new package). 
The only temporary working solution is to change Deck.GL version with latest and re-install package.json with yarn.

---

# Development phase

## Client Side
### Project setup
```
cd .\client\
yarn install
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
yarn install
```

### Compiles and hot-reloads for development
```
npm run dev
```
