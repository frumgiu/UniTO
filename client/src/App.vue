<template>
  <div id="app">
    <SearchBar @getData="askTableData" @askDataBySearch="askDataBySearch" @askDataByFilter="askDataByFilter"/>
    <alert-warning ref="alertWarning" :showDismissibleAlert="false"/>
    <Map :data-geo="savedData"/>
  </div>
</template>

<!--
    <div style="text-align: left; margin-top: 2rem; margin-left: 1rem">{{messageDemo}}</div>
    <ul class="list-demo">
      <li v-for="(data, index) in savedData" :key="index" style="text-align: left" >{{data.name}}, {{ data.coordinates}}</li>
    </ul>
-->

<script>
import SearchBar from "@/components/SearchBar";
import AlertWarning from "@/components/generic/AlertWarning";
import Map from "@/components/Map";
import 'material-icons/iconfont/material-icons.css';
import {getData, getDataBySearch, getDataByFilter} from '@/./controllers/ControllerTableData'

export default {
  name: 'App',
  components: {
    Map,
    SearchBar,
    AlertWarning,
  },
  data() {
    return {
      savedData: [],
    }
  },
  mounted() {
    this.askTableData();
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
          this.$refs.alertWarning.setInvalidInput();
        } else {
          this.savedData = response;
        }
      }, error => { console.log(error); })
    },
    askDataByFilter: function(tagsList) {
      getDataByFilter(tagsList).then(response => {
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