<template>
  <div id="app">
    <div id="nav-bar" class="fixed-top my-navbar">
      <SearchBar ref="searchBarRef" @askDataBySearch="askDataBySearch" @askCloseCard="closeCard"/>
      <div class="vl"></div>
      <NavigationBar />
    </div>
    <FilterTable ref="filterOptionRef" :options="regions" :defaultMin="2010" :defaultMax="new Date().getFullYear()" @askDataByFilter="askDataByFilter"/>
    <div id="map-display">
      <MapOptionMenu ref="mapOptionsRef" @setLayer="setLayerMap" @askUserPosition="askUserPosition"/>
      <CardPicture ref="cardRef"/>
      <Map ref="mapRef" :data-geo="savedData" :layer-style="layerStyle" @askOpenCard="openCard" @askCloseCard="closeCard" @askCloseMenus="closeMenu"/>
    </div>

    <div id="gallery-display" style="display: block">
      <div id="gallery">
        <hr class="solid" style="margin-top: 4.7rem"/>
        <div class="image-group-wrapper">
          <div class="image-wrapper">
            <div class="image-gallery">Test della gallery</div>
            <hr class="solid"/>
            <div>Didascalie</div>
          </div>
          <div class="image-wrapper">
            <div class="image-gallery">Test della gallery</div>
            <hr class="solid"/>
            <div>Didascalie</div>
          </div>
          <div class="image-wrapper">
            <div class="image-gallery">Test della gallery</div>
            <hr class="solid"/>
            <div>Didascalie</div>
          </div>
          <div class="image-wrapper">
            <div class="image-gallery">Test della gallery</div>
            <hr class="solid"/>
            <div>Didascalie</div>
          </div>
          <div class="image-wrapper">
            <div class="image-gallery">Test della gallery</div>
            <hr class="solid"/>
            <div>Didascalie</div>
          </div>
          <div class="image-wrapper">
            <div class="image-gallery">Test della gallery</div>
            <hr class="solid"/>
            <div>Didascalie</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import 'material-icons/iconfont/material-icons.css';
import {getData, getCoordsForLocation, getNameForCoord} from '@/./controllers/ControllerTableData'
import SearchBar from "@/components/SearchBar";
import FilterTable from "@/components/FilterTable";
import CardPicture from "@/components/CardPicture";
import MapOptionMenu from "@/components/MapOptionMenu";
import Map from "@/components/Map";
import NavigationBar from "@/components/NavigationBar";

export default {
  name: 'App',
  components: {
    NavigationBar,
    Map,
    MapOptionMenu,
    CardPicture,
    SearchBar,
    FilterTable
  },
  data() {
    return {
      /* filter values for regions */
      regions: ["Europe", "Asia", "Africa", "Americas", "Oceania"],
      /* default data layer */
      layerStyle: "2d",
      /* saved data to display */
      savedData: []
    }
  },
  mounted() {
    this.askDataByFilter();
  },
  methods: {
    contactDB: function () {
      let coords;
      /* Controllo se ho nella search bar una stringa che si riferisce a un luogo */
      getCoordsForLocation(this.$store.state.lastSearchTxt).then(response => {
        if (response !== "not valid location") {
          coords = response;
          let zoom = coords.info === "country"  ? 5 : 10;
          this.$refs.mapRef.setViewState(coords, zoom);
        }
        /* Chiedo che venga interrogato il db */
        const prefix = this.$store.state;
        getData(prefix.lastSearchTxt, prefix.filterInfo.lastCheckedTag, prefix.filterInfo.lastSelectedMin, prefix.filterInfo.lastSelectedMax, coords).then(response => {
          this.savedData = response;
        }, error => {
          console.log(error);
        })
      }, error => {
        console.log(error);
      })
    },
    askDataBySearch: function() {
      this.closeCard();
      this.contactDB();
    },
    askDataByFilter: function() {
      this.closeCard();
      this.contactDB();
    },
    askUserPosition: function(location) {
      getNameForCoord(location).then(response => {
        if (this.$store.state.lastSearchTxt !== response){
          this.$refs.searchBarRef.setSearchOnUserPlace(response);
        } else {
          this.$refs.mapRef.setViewState(location, 10);
        }
      });
    },
    closeMenu: function() {
      this.$refs.mapOptionsRef.closeMenu();
      this.$refs.filterOptionRef.closeMenu();
    },
    closeCard: function() {
      this.$refs.cardRef.closeCard();
    },
    openCard: function() {
      this.$refs.cardRef.openCard();
    },
    setLayerMap: function(style) {
      this.layerStyle = style;
    },
  }
}
</script>

<style>
  @import url('https://fonts.googleapis.com/css2?family=Dosis&display=swap'); /* l'errore e' un bug di webstorm */
  @import url('resources/stylesheets/responsive-navbar.css');

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

  .vl {
    border-top: 0.09rem solid #967bdc;
    height: 50px;
  }

  #gallery-display {
    z-index: 9;
    background-color: whitesmoke;
    width: 55%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    border-radius: 0 0.8rem 0.8rem 0;
    box-shadow: rgba(0, 0, 0, 0.1) 0.122rem 0.122rem 0.163rem;
  }

  .image-group-wrapper {
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    flex-flow: row wrap;
    gap: 0.8rem 0.01rem;
    width: 100%;
    height: 100%;
    cursor: default;
  }

  .image-wrapper{
    width: 48%;
    height: fit-content;
    padding: 0.3rem 0.2rem;
    border: 0.09rem solid #48a36a;
    border-radius: 0.8rem;
    box-shadow: rgba(0, 0, 0, 0.1) 0.122rem 0.122rem 0.163rem;
    text-align: center;
  }

</style>