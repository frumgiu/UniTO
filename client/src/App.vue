<template>
  <div id="app">
    <SearchBar @askDataBySearch="askDataBySearch"/>
    <FilterTable :options="categories" :defaultMin="defaultMin" :defaultMax="defaultMax" @askDataByFilter="askDataByFilter"/>
    <Map :data-geo="savedData"/>
  </div>
</template>

<script>
import SearchBar from "@/components/SearchBar";
import FilterTable from "@/components/FilterTable";
import Map from "@/components/Map";
import 'material-icons/iconfont/material-icons.css';
import {getData} from '@/./controllers/ControllerTableData'

export default {
  name: 'App',
  components: {
    Map,
    SearchBar,
    FilterTable,
  },
  data() {
    return {
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
      this.contactDB();
    },
   askDataByFilter: function(tagsList, minYear, maxYear) {
      this.lastCheckedTag = tagsList;
      this.lastSelectedMin = minYear;
      this.lastSelectedMax = maxYear;
      this.contactDB();
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