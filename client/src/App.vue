<template>
  <div id="app">
    <SearchBar @askDataBySearch="askDataBySearch"/>
    <FilterTable :options="regions" :defaultMin="2010" :defaultMax="new Date().getFullYear()" @askDataByFilter="askDataByFilter"/>
    <CardPicture ref="cardRef" :name-picture="namePicture" :country-picture="countryPicture" :region-picture="regionPicture" :year-picture="yearPicture" />
    <Map :data-geo="savedData" @askOpenCard="openCard" @askCloseCard="closeCard"/>
  </div>
</template>

<script>
import SearchBar from "@/components/SearchBar";
import FilterTable from "@/components/FilterTable";
import Map from "@/components/Map";
import 'material-icons/iconfont/material-icons.css';
import {getData} from '@/./controllers/ControllerTableData'
import CardPicture from "@/components/CardPicture";

export default {
  name: 'App',
  components: {
    CardPicture,
    SearchBar,
    FilterTable,
    Map
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
      regions: ["Europe", "Asia", "Caribbean", "Africa", "Central America", "North America", "Oceania", "South America"],
      /* saved data to display */
      savedData: []
    }
  },
  mounted() {
    this.askDataByFilter([], this.lastSelectedMin, this.lastSelectedMax); // start with only data from current year
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
  #app {
    font-family: Noto Sans, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: left;
  }
</style>