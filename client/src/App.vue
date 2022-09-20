<template>
  <div id="app">
    <SearchBar @askDataBySearch="askDataBySearch" @askCloseCard="closeCard"/>
    <FilterTable ref="filterOptionRef" :options="regions" :defaultMin="2010" :defaultMax="new Date().getFullYear()" @askDataByFilter="askDataByFilter"/>
    <MapOptionMenu ref="mapOptionsRef" />
    <CardPicture ref="cardRef" :name-picture="namePicture" :country-picture="countryPicture" :region-picture="regionPicture" :year-picture="yearPicture" />
    <Map :data-geo="savedData" @askOpenCard="openCard" @askCloseCard="closeCard" @askCloseMenus="closeMenu"/>
  </div>

  <!--
  <Test :data-geo="savedData" @askOpenCard="openCard" @askCloseCard="closeCard" @askCloseMenus="closeMenu"/>
  -->
</template>

<script>
import 'material-icons/iconfont/material-icons.css';
import {getData} from '@/./controllers/ControllerTableData'
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
      /* filter values */
      regions: ["Europe", "Asia", "Africa", "Americas", "Oceania"],
      /* saved data to display */
      savedData: []
    }
  },
  mounted() {
    this.askDataByFilter([], this.lastSelectedMin - 1, this.lastSelectedMax - 1); // start with only data from current year
  },
  methods: {
    contactDB: function () {
      getData(this.lastSearchText, this.lastCheckedTag, this.lastSelectedMin, this.lastSelectedMax).then(response => {
        if (response === "empty table") {
          this.savedData = [];
        } else {
          this.savedData = response;
        }
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
    closeMenu: function() {
      this.$refs.mapOptionsRef.closeMenu();
      this.$refs.filterOptionRef.closeMenu();
    },
    closeCard: function() {
      this.$refs.cardRef.closeCard();
    },
    openCard: function(coordTop, coordLeft, namePicture, countryPicture, regionPicture, yearPicture) {
      this.$refs.cardRef.openCard(coordTop, coordLeft, namePicture, countryPicture, regionPicture, yearPicture);
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