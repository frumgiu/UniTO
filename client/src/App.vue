<template>
  <div id="app">
    <SearchBar @askDataBySearch="askDataBySearch"/>
    <FilterTable :options="categories" :defaultMin="defaultMin" :defaultMax="defaultMax" @askDataByFilter="askDataByFilter"/>
    <div class="card card-style" id="cardpicture">
      <img class="card-img-top" src="https://picsum.photos/600/300/?image=25" alt="Card image cap">
      <div class="card-body">
        <h5 class="card-title">Picture title</h5>
        <hr class="solid">
        <p class="card-text">Country name, Region name<br/> Year number</p>
        <div>
          <a href="https://deck.gl/docs/developer-guide/interactivity" target="_blank">
            <button class="card-btn">
              <span class="material-icons" style="vertical-align: middle">link</span>
              <span class="card-link-text">View on wikipedia</span>
            </button>
          </a>
          <button class="close-btn" type="button" data-toggle="tooltip" data-placement="top" title="Close" @click="closeCard">
            <span class="material-icons"  style="vertical-align: middle">close</span>
          </button>
        </div>
      </div>
    </div>
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
    },
    openCard: function() {
      const cardPictureId = document.getElementById("cardpicture");
      cardPictureId.style.display = "flex";
    },
    closeCard: function() {
      const cardPictureId = document.getElementById("cardpicture");
      cardPictureId.style.display = "none";
    }
  }
}
</script>

<style>
  @import "resources/stylesheets/card-picture-style.css";

  #app {
    font-family: Noto Sans, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: left;
  }
</style>