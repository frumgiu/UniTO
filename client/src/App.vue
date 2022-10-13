<template>
  <div id="app">
    <div id="nav-bar" class="fixed-top my-navbar navbar-transparent">
      <SearchBar ref="searchBarRef" @askDataBySearch="contactDB" @askCloseCard="closeCard"/>
      <div class="vl"></div>
      <NavigationBar />
    </div>
    <FilterTable ref="filterOptionRef" :options="regions" :defaultMin="2010" :defaultMax="new Date().getFullYear()" @askDataByFilter="contactDB"/>
    <div id="map-display">
      <MapOptionMenu ref="mapOptionsRef" @setLayer="setLayerMap" @askUserPosition="askUserPosition"/>
      <CardPicture ref="cardRef"/>
      <Map ref="mapRef" :data-geo="savedData" :layer-style="layerStyle" @askOpenCard="openCard" @askCloseCard="closeCard" @askCloseMenus="closeMenu" @askUpdateData="contactDB"/>
    </div>

    <div id="gallery-display" class="gallery-split">
      <div id="gallery">
        <hr class="solid" style="margin-top: 4.7rem"/>
        <div class="image-group-wrapper" >
          <!--  <div v-for="(value, index) in savedData" :key="index">
         <GalleryElement :single-data="value"/>
       </div> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import 'material-icons/iconfont/material-icons.css';
import {getData, getCoordsForLocation, getNameForCoord} from '@/./controllers/ControllerTableData'
import SearchBar from "@/components/SearchBar";
import FilterTable from "@/components/FilterTable";
import CardPicture from "@/components/CardPicture";
import MapOptionMenu from "@/components/MapOptionMenu";
import Map from "@/components/Map";
import NavigationBar from "@/components/NavigationBar";
//import GalleryElement from "@/components/GalleryElement";
import store from "@/store";

export default {
  name: 'App',
  components: {
    NavigationBar,
    Map,
    MapOptionMenu,
    CardPicture,
    SearchBar,
    FilterTable,
    //GalleryElement
  },
  data() {
    return {
      windowH: window.innerWidth,
      /* filter values for regions */
      regions: ["Europe", "Asia", "Africa", "Americas", "Oceania"],
      /* default data layer */
      layerStyle: "2d",
      /* saved data to display */
      savedData: []
    }
  },
  mounted() {
    navigator.geolocation.getCurrentPosition(position => {
          const startPositon = {log: position.coords.longitude, lat: position.coords.latitude};
          this.$refs.mapRef.setViewState(startPositon, 8);
        },
        () => {
          console.log("User did not allow geolocation. Starting from a default location")
        })
    this.$refs.mapRef.saveMapBBox();
    this.contactDB();
  },
  methods: {
    contactDB: function () {
      this.closeCard();
      /* Controllo se ho nella search bar una stringa che si riferisce a un luogo */
      getCoordsForLocation(this.$store.state.lastSearchTxt).then(response => {
        if (response !== "not valid location") {
          //let zoom = coords.info === "country"  ? 5 : 10;
          //console.log("bbox to fit info: " + response.bbox[0] + ',' + response.bbox[2] + '\n' + response.bbox[1] + ',' + response.bbox[3])
          store.dispatch('changeBBInfo', {newMinLog: response.bbox[0], newMaxLog: response.bbox[2], newMinLat: response.bbox[1], newMaxLat: response.bbox[3]});
          //this.$refs.mapRef.setViewState(response, 6);
          /* TODO fitbounds method */
        }
        /* Chiedo che venga interrogato il db */
        const prefix = this.$store.state;
        getData(prefix.lastSearchTxt, prefix.filterInfo.lastCheckedTag, prefix.filterInfo.lastSelectedMin, prefix.filterInfo.lastSelectedMax, prefix.currentBBInfo).then(response => {
          this.savedData = response;
        }, error => {
          console.log(error);
        })
      }, error => {
        console.log(error);
      })
    },
    /* todo eliminare */
    askDataBySearch: function() {
      this.closeCard();
      this.contactDB();
    },
    askDataByFilter: function() {
      this.closeCard();
      this.contactDB();
    },
    askUserPosition: function(location) {
      getNameForCoord(location).then(response => {
        if (this.$store.state.lastSearchTxt !== response){
          this.$refs.searchBarRef.setSearchOnUserPlace(response);
        } else {
          /* TODO fitbounds method */
          this.$refs.mapRef.setViewState(location, 10);
        }
      });
    },
    closeMenu: function() {
      this.$refs.mapOptionsRef.closeMenu();
      this.$refs.filterOptionRef.closeMenu();
    },
    closeCard: function() {
      this.$refs.cardRef.closeCard();
    },
    openCard: function() {
      this.$refs.cardRef.openCard();
    },
    setLayerMap: function(style) {
      this.layerStyle = style;
    },
  }
}
</script>

<style>
  @import url('https://fonts.googleapis.com/css2?family=Dosis&display=swap'); /* l'errore e' un bug di webstorm */
  @import url('resources/stylesheets/navbar/responsive-navbar.css');
  @import url('resources/stylesheets/gallery/gallery-container.css');

  #app {
    font-family: "Dosis", sans-serif;
    font-weight: 500;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: left;
  }

  hr.solid {
    border-top: 0.09rem solid #967bdc;
    margin: 0.6rem;
  }

  .vl {
    border-right: 0.09rem solid #48a36a;
    margin: 0 0.6rem;
    height: 30px;
  }
</style>