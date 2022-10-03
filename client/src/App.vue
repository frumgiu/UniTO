<template>
  <div id="app">
    <SearchBar ref="searchBarRef" @askDataBySearch="askDataBySearch" @askCloseCard="closeCard"/>
    <FilterTable ref="filterOptionRef" :options="regions" :defaultMin="2010" :defaultMax="new Date().getFullYear()" @askDataByFilter="askDataByFilter"/>
    <MapOptionMenu ref="mapOptionsRef" @setLayer="setLayerMap" @askUserPosition="askUserPosition"/>
    <CardPicture ref="cardRef" />
    <Map ref="mapRef" :data-geo="savedData" :layer-style="layerStyle" @askOpenCard="openCard" @askCloseCard="closeCard" @askCloseMenus="closeMenu"/>
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

export default {
  name: 'App',
  components: {
    Map,
    MapOptionMenu,
    CardPicture,
    SearchBar,
    FilterTable
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
          this.$refs.mapRef.setViewState(coords, zoom);
        }
        /* Chiedo che venga interrogato il db */
        const prefix = this.$store.state;
        getData(prefix.lastSearchTxt, prefix.filterInfo.lastCheckedTag, prefix.filterInfo.lastSelectedMin, prefix.filterInfo.lastSelectedMax, coords).then(response => {
          this.savedData = response;
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
    askUserPosition: function(location) {
      getNameForCoord(location).then(response => {
        if (this.$store.state.lastSearchTxt !== response){
          this.$refs.searchBarRef.setSearchOnUserPlace(response);
        } else {
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
    }
  }
}
</script>

<style>
  @import url('https://fonts.googleapis.com/css2?family=Dosis&display=swap'); /* l'errore e' un bug di webstorm */

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
</style>