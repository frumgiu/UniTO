<template>
  <div id="app">
    <SearchBar @askDataBySearch="askDataBySearch" @askCloseCard="closeCard"/>
    <FilterTable :options="regions" :defaultMin="2010" :defaultMax="new Date().getFullYear()" @askDataByFilter="askDataByFilter"/>
    <div class="nav-options-container">
      <div class="nav-options-inner">
        <span class="material-icons nav-icon">explore</span>
        <span class="filter-title">Navigation</span>
        <hr class="solid">
        <button class="btn-position" >
          <span class="material-icons"  style="vertical-align: middle; margin-right: 0.2rem">my_location</span>
          Go to my location
        </button>
        <div class="layer-options-container">
          <p>Type of layer</p>
          <div class="layer-options-inner">
            <button class="layer-options">2D</button>
            <span style="color: white; margin:0 0.15rem">|</span>
            <button class="layer-options">3D</button>
            <span style="color: white; margin:0 0.15rem">|</span>
            <button class="layer-options">icons</button>
          </div>
        </div>
      </div>
    </div>
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
    margin: 0.5rem;
  }

  .nav-options-container {
    width: fit-content;
    z-index: 9;
    position: fixed;
    bottom: 0;
    right: 0;
    margin-bottom: 1.4rem;
    margin-right: 1.4rem;
    overflow: auto;
    background-color: transparent;
    border-radius: 0.8rem;
    box-shadow: rgba(0, 0, 0, 0.1) 0.122rem 0.122rem 0.163rem;
  }

  .nav-options-inner {
    width: 100%;
    height: 100%;
    border-radius: 0.8rem;
    background: rgba(255,255,255, 0.4);
    padding: 0.5rem 0.8rem;
    overflow-y: auto;
    overflow-x: hidden;
  }

  .nav-icon {
    width: fit-content;
    vertical-align: middle;
    margin: 0 0.3rem;
    cursor: default;
  }

  .btn-position {
    background: none;
    border: none;
    color: black;
    width: fit-content;
  }

  .btn-position:hover {
    color: #48a36a;
  }

  .layer-options-container {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-flow: row wrap;
  }

  .layer-options-inner {
    background-color: #48a36a;
    margin-left: 0.9rem;
    border-radius: 0.6rem;
    box-shadow: #7EBE96 0.122rem 0.122rem 0.163rem;
    padding: 0.3rem 0.6rem;
  }

  .layer-options {
    border: none;
    border-radius: 0.35rem;
    background-color: #48a36a;
    color: white;
  }

  .layer-options:hover {
    background-color: #7EBE96;
  }
</style>