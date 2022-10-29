<template>
  <div id="app">
    <div id="nav-bar" class="fixed-top my-navbar navbar-transparent">
      <SearchBar ref="searchBarRef" @askDataBySearch="contactDBBySearchTxt" @askCloseCard="closeCard"/>
      <div class="vl"></div>
      <NavigationBar ref="navigationBarRef" @askCloseMenus="closeMenu" @askCloseCard="closeCard"/>
    </div>
    <FilterTable ref="filterOptionRef" :options="regions" :defaultMin="2010" :defaultMax="new Date().getFullYear()" @askDataByFilter="contactDB"/>
    <div id="map-display">
      <MapOptionMenu ref="mapOptionsRef" @setLayer="setLayerMap" @askUserPosition="askUserPosition"/>
      <CardPicture ref="cardRef"/>
      <Map ref="mapRef" :data-geo="savedData" :layer-style="layerStyle" @askOpenCard="openCard"
           @askCloseCard="closeCard" @askCloseMenus="closeMenu" @askUpdateData="contactDB"/>
    </div>
    <Gallery :saved-data="savedData" />
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
import store from "@/store";
import Gallery from "@/components/Gallery";

export default {
  name: 'App',
  components: {
    Gallery,
    NavigationBar,
    Map,
    MapOptionMenu,
    CardPicture,
    SearchBar,
    FilterTable,
  },
  data() {
    return {
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
          const startPosition = {log: position.coords.longitude, lat: position.coords.latitude};
          this.$refs.mapRef.setViewState(startPosition, 11);
        },
        () => {
          console.log("User did not allow geolocation. Starting from a default location")
        })

    this.$refs.mapRef.triggerUpdateData();
  },
  methods: {
    contactDB: function () {
      this.closeCard();
      /* Chiedo che venga interrogato il db */
      const prefix = this.$store.state;
      getData(prefix.lastSearchTxt, prefix.filterInfo.lastCheckedTag, prefix.filterInfo.lastSelectedMin, prefix.filterInfo.lastSelectedMax, prefix.currentBBInfo).then(response => {
        this.savedData = response;
      }, error => {
        console.log(error);
      })
    },
    contactDBBySearchTxt: function () {
      getCoordsForLocation(this.$store.state.lastSearchTxt).then(response => {
        if (response !== "not valid location") {
          store.dispatch('changeBBInfo', {
            newMinLog: response.bbox[0],
            newMaxLog: response.bbox[2],
            newMinLat: response.bbox[1],
            newMaxLat: response.bbox[3]
          });
          this.$refs.mapRef.fitBoundsMap();
        }
        this.contactDB();
      }, error => {
        console.log(error);
      })
    },
    askUserPosition: function (location) {
      getNameForCoord(location).then(response => {
        if (this.$store.state.lastSearchTxt === response) {
          this.contactDBBySearchTxt();
        } else {
          this.$refs.searchBarRef.setSearchOnUserPlace(response);
        }
      });
    },
    closeMenu: function () {
      this.$refs.mapOptionsRef.closeMenu();
      this.$refs.filterOptionRef.closeMenu();
    },
    closeCard: function () {
      this.$refs.cardRef.closeCard();
    },
    openCard: function () {
      this.$refs.cardRef.openCard();
      this.$refs.navigationBarRef.closeGallery();
    },
    setLayerMap: function (style) {
      this.layerStyle = style;
    }
  }
}
</script>

<style>
  @import url('https://fonts.googleapis.com/css2?family=Dosis&display=swap'); /* l'errore e' un bug di webstorm */
  @import url('resources/stylesheets/navbar/responsive-navbar.css');
  @import url('resources/stylesheets/responsive-vl.css');

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
  /* width */
  ::-webkit-scrollbar {
    width: 0.4rem;
  }
  /* Track */
  ::-webkit-scrollbar-track {
    box-shadow: inset 0 0 1px grey;
    border-radius: 0.6rem;
  }
  /* Handle */
  ::-webkit-scrollbar-thumb {
    background: #967bdc;
    border-radius: 0.6rem;
  }
  /* Handle on hover */
  ::-webkit-scrollbar-thumb:hover {
    background: #7653D1;
  }
</style>