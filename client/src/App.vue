<template>
  <div id="app">
    <SearchBar @getData="askTableData" @askDataBySearch="askDataBySearch"/>
    <FilterTable titleMenu="Regions" :options="categories" :defaultMin="defaultMin" :defaultMax="defaultMax" @askDataByFilter="askDataByFilter"/>
    <Map :data-geo="savedData"/>
  </div>
</template>

<script>
import SearchBar from "@/components/SearchBar";
import FilterTable from "@/components/FilterTable";
import Map from "@/components/Map";
import 'material-icons/iconfont/material-icons.css';
import {getData, getDataBySearch, getDataByFilter} from '@/./controllers/ControllerTableData'

export default {
  name: 'App',
  components: {
    Map,
    SearchBar,
    FilterTable,
  },
  data() {
    return {
      defaultMin: 2010,
      defaultMax: new Date().getFullYear(),
      savedData: [],
      categories: ["Europe", "Asia", "Caribbean", "Africa", "Central America", "North America", "Oceania", "South America"]
    }
  },
  mounted() {
    this.askDataByFilter([], this.defaultMax, this.defaultMax);
  },
  methods: {
    askTableData: function() {
      getData().then(response => {
            this.savedData = response;
          }, error => { console.log(error); })
    },
    askDataBySearch: function(searchText) {
      getDataBySearch(searchText).then(response => {
        if(response === "empty table"){
          this.savedData = [];
        } else {
          this.savedData = response;
        }
      }, error => { console.log(error); })
    },
    askDataByFilter: function(tagsList, minYear, maxYear) {
      getDataByFilter(tagsList, minYear, maxYear).then(response => {
         if(response === "empty table"){
         this.savedData = [];
         //TODO: not working with empty data set
         } else {
        this.savedData = response;
        }
      }, error => { console.log(error); })
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