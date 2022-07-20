<template>
  <div id="app">
    <!-- Per ora non uso la component della searchbar, cosi posso filtrare usando un computed senza dover interrogare il DB -->
    <nav class="navbar navbar-expand-sm bg-light navbar-light ">
      <div class="container-fluid nav-container">
        <form class="d-flex input-group w-100">
          <input id="search" v-model="searchText" type="text" class="search-text" placeholder="search a picture" autocomplete="off"/>
          <button class="search-btn" data-toggle="tooltip" data-placement="top" title="Search" disabled>
            <span class="material-icons"  style="vertical-align: middle">search</span>
          </button>
          <DropDown titleMenuCat="Category" :optionsCat='categories' :tag-selected="tagList"/>
        </form>
      </div>
    </nav>
    <!-- -->
    <TagList v-if="tagList !== 0" :tagList='tagList'/>
    <div style="text-align: left; margin-top: 2rem; margin-left: 1rem">{{messageDemo}}</div>
    <ul class="list-demo">
      <li v-for="(data, index) in filteredSavedData" :key="index" style="text-align: left" >{{data.name}}</li>
    </ul>
    <div>Tag List: {{tagList}}</div>
  </div>
</template>

<!--
<SearchBar @filterByTag="getDataByTag" @getDataWithSearch="getDataWithSearch" :tag-selected="tagList"/>

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
//import SearchBar from './components/SearchBar';
import DropDown from "@/components/DropDown";
import TagList from "@/components/TagList";
import 'material-icons/iconfont/material-icons.css';
import {getData, getDataByTag} from '@/controllers/ControllerTableData'

export default {
  name: 'App',
  components: {
    //VueDeckgl,
    //SearchBar,
    DropDown,
    TagList
  },
  data() {
    return {
      messageDemo: "Table elements:",
      savedData: [],
      tagList: [],
      searchText: "",
      categories: ["Buildings", "Park", "Statues"]
    }
  },
  mounted() {
    this.getData();
  },
  methods: {
    getData: function() {
      getData().then(response => {
            this.savedData = response;
          }, error => { console.log(error); })
    },
    getDataByTag: function(tag) {
      getDataByTag(tag).then(response => {
        this.savedData += response;
      }, error => { console.log(error); })
    }
  },
  computed: {
    layers() {
      // create layers using @deck.gl/layers
      return [
        //layers
      ]
    },
    filteredSavedData() {
      return this.savedData.filter(p => {
        return p.name.toLowerCase().indexOf(this.searchText.toLowerCase()) !== -1;
      })
    }
  }
}
</script>

<style>
  #app {
    font-family: Noto Sans, sans-serif;
    /*Avenir, Helvetica, Arial, sans-serif;*/
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
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

  .nav-container {
    width: 100%;
    max-width: 100%;
  }

  .search-text {
    width: 40%;
    max-width: 100%;
    font-size: 0.94rem;
    padding: 0.3rem;
    border: 0.1rem solid gray;
    border-radius: 0.6rem;
    outline: none;
    transition: all 0.3s;
    align-items: center;
  }

  .search-text:focus {
    border: 0.1rem solid #967bdc;
    outline: none;
    box-shadow: none;
  }

  .search-text::placeholder {
    font-size: 14px;
    transition: all 0.3s;
    color: gray;
  }

  .search-btn {
    width: available;
    max-width: 100%;
    cursor: pointer;
    background: none;
    border: none;
    color: black;
  }

  .search-btn:hover {
    color: #48a36a;
  }
</style>