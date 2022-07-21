<template>
  <div id="app">
    <SearchBar @textChanged="textChanged" @getData="askTableData" @askDataBySearch="askDataBySearch" :tag-selected="tagList"/>
    <TagList v-if="tagList !== 0" :tagList='tagList'/>
    <alert-warning ref="alertWarning" :showDismissibleAlert="false"/>
    <div style="text-align: left; margin-top: 2rem; margin-left: 1rem">{{messageDemo}}</div>
    <ul class="list-demo">
      <li v-for="(data, index) in savedData" :key="index" style="text-align: left" >{{data.name}}</li>
    </ul>
  </div>
</template>

<!--
    <VueDeckgl
        id="map"
        :layers="layers"
        :viewState="viewState"
        @click="handleClick"
        @view-state-change="handleViewStateChange">
    </VueDeckgl>
-->

<script>
//import VueDeckgl from 'vue-deck.gl';
import SearchBar from "@/components/SearchBar";
import TagList from "@/components/TagList";
import AlertWarning from "@/components/generic/AlertWarning";
import 'material-icons/iconfont/material-icons.css';
import {getData, getDataBySearch} from '@/../controllers/ControllerTableData'

export default {
  name: 'App',
  components: {
    //VueDeckgl,
    SearchBar,
    TagList,
    AlertWarning
  },
  data() {
    return {
      messageDemo: "Table elements:",
      savedData: [],
      tagList: [],
      categories: ["Buildings", "Park", "Statues"],
    }
  },
  created() {
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
    textChanged: function() {

    }
  },
  computed: {
    layers() {
      // create layers using @deck.gl/layers
      return [
        //layers
      ]
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

  #map {
    position: absolute;
    top: auto;
    left: auto;
    margin-top: 2em;
    width: 100%;
    height: 100%;
    background: floralwhite;
    overflow: hidden;
  }

  .list-demo {
    margin-top: 0.5rem;
  }
</style>