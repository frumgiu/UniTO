<template>
  <div id="app">
    <SearchBar @askDataBySearch="askDataBySearch"/>
    <FilterTable :options="categories" :defaultMin="defaultMin" :defaultMax="defaultMax" @askDataByFilter="askDataByFilter"/>
    <CardPicture :name-picture="namePicture" :country-picture="countryPicture" :region-picture="regionPicture" :year-picture="yearPicture" />
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
      namePicture: "",
      yearPicture: 0,
      regionPicture: "",
      countryPicture: "",

      defaultMin: 2010, defaultMax: new Date().getFullYear(),
      lastSelectedMin: new Date().getFullYear(), lastSelectedMax: new Date().getFullYear(),
      lastSearchText: "",
      lastCheckedTag: [],
      savedData: [],
      categories: ["Europe", "Asia", "Caribbean", "Africa", "Central America", "North America", "Oceania", "South America"]
    }
  },
  mounted() {
    this.askDataByFilter([], this.lastSelectedMin, this.lastSelectedMax);
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
      const cardPictureId = document.getElementById("cardpicture");
      cardPictureId.style.display = "none";
    },
    openCard: function(coordTop, coordLeft, namePicture, countryPicture, regionPicture, yearPicture) {
      const cardPictureId = document.getElementById("cardpicture");
      cardPictureId.style.display = "flex";
      cardPictureId.style.position = "absolute";
      cardPictureId.style.top = coordTop + "px";
      cardPictureId.style.left = coordLeft + "px";
      this.namePicture = namePicture;
      this.countryPicture = countryPicture;
      this.regionPicture = regionPicture;
      this.yearPicture = yearPicture;
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