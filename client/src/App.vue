<template>
  <div id="app">
    <SearchBar @askDataBySearch="askDataBySearch" @askCloseCard="closeCard"/>
    <FilterTable ref="filterOptionRef" :options="regions" :defaultMin="2010" :defaultMax="new Date().getFullYear()" @askDataByFilter="askDataByFilter"/>
    <MapOptionMenu ref="mapOptionsRef" @setLayer="setLayerMap" @askUserPosition="askUserPosition"/>
    <CardPicture ref="cardRef" :name-picture="namePicture" :country-picture="countryPicture" :region-picture="regionPicture" :year-picture="yearPicture" />
    <Map ref="mapRef" :data-geo="savedData" :layer-style="layerStyle" @askOpenCard="openCard" @askCloseCard="closeCard" @askCloseMenus="closeMenu"/>
  </div>
</template>

<script>
import 'material-icons/iconfont/material-icons.css';
import {getData, getCoordsForLocation} from '@/./controllers/ControllerTableData'
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
      /* card info to display */
      namePicture: "", yearPicture: 0,
      regionPicture: "", countryPicture: "",
      /* latest filter info */
      lastSelectedMin: new Date().getFullYear(), lastSelectedMax: new Date().getFullYear(),
      lastSearchText: "", lastCheckedTag: [],
      /* filter values for regions */
      regions: ["Europe", "Asia", "Africa", "Americas", "Oceania"],
      /* default data layer */
      layerStyle: "2d",
      /* saved data to display */
      savedData: []
    }
  },
  mounted() {
    this.askDataByFilter([], this.lastSelectedMin - 1, this.lastSelectedMax - 1); // start with data from previous year
  },
  methods: {
    contactDB: function () {
      let coords;
      /* Controllo se ho nella search bar una stringa che si riferisce a un luogo */
      getCoordsForLocation(this.lastSearchText).then(response => {
        if (response !== "not valid location") {
          coords = response;
          let zoom = coords.info === "country"  ? 5 : 10;
          this.$refs.mapRef.setViewState(coords, zoom);
        }
        /* Chiedo che venga interrogato il db */
        getData(this.lastSearchText, this.lastCheckedTag, this.lastSelectedMin, this.lastSelectedMax, coords).then(response => {
          this.savedData = response;
        }, error => {
          console.log(error);
        })
      }, error => {
        console.log(error);
      })

    },
    askDataBySearch: function(searchText) {
      this.lastSearchText = searchText;
      this.closeCard();
      this.contactDB();
    },
    askDataByFilter: function(tagsList, minYear, maxYear) {
      this.lastCheckedTag = tagsList;
      this.lastSelectedMin = minYear;
      this.lastSelectedMax = maxYear;
      this.closeCard();
      this.contactDB();
    },
    askUserPosition: function(location) {
      this.$refs.mapRef.setViewState(location, 12);
    },
    closeMenu: function() {
      this.$refs.mapOptionsRef.closeMenu();
      this.$refs.filterOptionRef.closeMenu();
    },
    closeCard: function() {
      this.$refs.cardRef.closeCard();
    },
    openCard: function(namePicture, countryPicture, regionPicture, yearPicture) {
      this.$refs.cardRef.openCard(namePicture, countryPicture, regionPicture, yearPicture);
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