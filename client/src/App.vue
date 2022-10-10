<template>
  <div id="app">
    <div id="nav-bar" class="fixed-top my-navbar navbar-transparent">
      <SearchBar ref="searchBarRef" @askDataBySearch="askDataBySearch" @askCloseCard="closeCard"/>
      <div class="vl"></div>
      <NavigationBar />
    </div>
    <FilterTable ref="filterOptionRef" :options="regions" :defaultMin="2010" :defaultMax="new Date().getFullYear()" @askDataByFilter="askDataByFilter"/>
    <div id="map-display">
      <MapOptionMenu ref="mapOptionsRef" @setLayer="setLayerMap" @askUserPosition="askUserPosition"/>
      <CardPicture ref="cardRef"/>
      <Map ref="mapRef" :data-geo="savedData" :layer-style="layerStyle" @askOpenCard="openCard" @askCloseCard="closeCard" @askCloseMenus="closeMenu" @askUpdateData="askUpdateData"/>
    </div>

    <div id="gallery-display" class="gallery-split">
      <div id="gallery">
        <hr class="solid" style="margin-top: 4.7rem"/>
       <!-- <div class="image-group-wrapper" v-for="(value, index) in savedData" :key="index">
          <GalleryElement :single-data="value"/>
        </div> -->
      </div>
    </div>
  </div>
</template>

<script>
import 'material-icons/iconfont/material-icons.css';
import {getData, getCoordsForLocation, getNameForCoord, updateData} from '@/./controllers/ControllerTableData'
import SearchBar from "@/components/SearchBar";
import FilterTable from "@/components/FilterTable";
import CardPicture from "@/components/CardPicture";
import MapOptionMenu from "@/components/MapOptionMenu";
import Map from "@/components/Map";
import NavigationBar from "@/components/NavigationBar";
import store from "@/store";

export default {
  name: 'App',
  components: {
    NavigationBar,
    Map,
    MapOptionMenu,
    CardPicture,
    SearchBar,
    FilterTable
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
    this.askDataByFilter();
  },
  methods: {
    contactDB: function () {
      let coords;
      /* Controllo se ho nella search bar una stringa che si riferisce a un luogo */
      getCoordsForLocation(this.$store.state.lastSearchTxt).then(response => {
        if (response !== "not valid location") {
          coords = response;
          let zoom = coords.info === "country"  ? 5 : 10;
          /* TODO: se e' una citta o stato, mancano gli altri casi */
          //console.log("bbox to fit info: " + response.bbox[0] + ',' + response.bbox[2] + '\n' + response.bbox[1] + ',' + response.bbox[3])
          store.dispatch('changeBBInfo', {newMinLog: response.bbox[0], newMaxLog: response.bbox[2], newMinLat: response.bbox[1], newMaxLat: response.bbox[3]});
          this.$refs.mapRef.setViewState(coords, zoom, false);
        }
        /* Chiedo che venga interrogato il db */
        const prefix = this.$store.state;
        getData(prefix.lastSearchTxt, prefix.filterInfo.lastCheckedTag, prefix.filterInfo.lastSelectedMin, prefix.filterInfo.lastSelectedMax, coords).then(response => {
          this.savedData = response;
          this.askUpdateData();
        }, error => {
          console.log(error);
        })
      }, error => {
        console.log(error);
      })
    },
    askDataBySearch: function() {
      this.closeCard();
      this.contactDB();
    },
    askDataByFilter: function() {
      this.closeCard();
      this.contactDB();
    },
    askUpdateData: function() {
      const bbcurrent = this.$store.state.currentBBInfo;
      updateData(bbcurrent).then(response => this.savedData = response, error => console.log(error));
    },
    askUserPosition: function(location) {
      getNameForCoord(location).then(response => {
        if (this.$store.state.lastSearchTxt !== response){
          this.$refs.searchBarRef.setSearchOnUserPlace(response);
        } else {
          this.$refs.mapRef.setViewState(location, 10, false);
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