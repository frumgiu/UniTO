<template>
  <VueDeckgl
      :layers="layers"
      :viewState="viewState"
      @click="handleClick"
      @view-state-change="updateViewState"
      class="deck-class">
    <div id="map" ref="map"/>
  </VueDeckgl>
</template>

<script>
import {ScreenGridLayer} from "@deck.gl/aggregation-layers";
import {IconLayer} from "@deck.gl/layers";
import mapboxgl from "mapbox-gl";
import VueDeckgl from 'vue-deck.gl';

export default {
  name: "Map",
  components: {
    VueDeckgl
  },
  props: ['dataGeo'],
  data() {
    return {
      accessToken: "pk.eyJ1IjoicG9zaWU5OCIsImEiOiJjbDV5MTVteXAwOHRoM2VwZDFlYzN4YTJuIn0.1rRyi4xUKIBqfnhfA9GfVQ",
      mapStyle: "mapbox://styles/posie98/cl7jhub3v005j14nfyksvuc9p",
      viewState: {
        latitude: 44.3072,
        longitude: 8.484106,
        zoom: 8,
        bearing: 0,
        pitch: 0,
      },
    };
  },
  created() {
    this.map = null;
  },
  methods: {
    updateViewState: function(viewState) {
      console.log("updating view state...");
      this.viewState = {
        ...viewState
      }
      this.map.jumpTo({
        center: [viewState.longitude, viewState.latitude],
        zoom: viewState.zoom,
        bearing: viewState.bearing,
        pitch: viewState.pitch,
      });
    },
    handleClick: function() {},
    showIcon: function(info) {
      const cardPictureId = document.getElementById("cardpicture");
      const sideNav = document.getElementById("sidenav");
      if (sideNav.style.display === "block" && window.innerWidth <= 768) {
        sideNav.style.display = "none";
      }
      cardPictureId.style.display = "flex";
      cardPictureId.style.position = "absolute";
      cardPictureId.style.top = info.y + "px";
      cardPictureId.style.left = info.x + "px";
      console.log(info.y + " and " + info.x);
    }
},
  mounted() {
    this.map = new mapboxgl.Map({
      accessToken: this.accessToken,
      container: this.$refs.map,
      interactive: false,
      style: this.mapStyle || "mapbox://styles/posie98/cl7jhub3v005j14nfyksvuc9p",
      center: [this.viewState.longitude, this.viewState.latitude],
      zoom: this.viewState.zoom,
      pitch: this.viewState.pitch,
      bearing: this.viewState.bearing,
    });
  },
  computed: {
    layers() {
      if (this.viewState.zoom <= 20 && this.viewState.zoom >= 9) {
        return [
          new IconLayer({
          id: 'icon-layer',
          data: this.dataGeo,
          iconAtlas: 'https://raw.githubusercontent.com/visgl/deck.gl-data/master/website/icon-atlas.png',
          iconMapping: { marker: {x:0, y:0, width: 128, height: 128, mask: true} },
          getIcon: () => 'marker',
          getPosition: (d) => [d.log, d.lat],
          getSize: () => 4,
          sizeScale: 10,
          getColor: [72, 163, 106],
          pickable: true,
          onClick: (info) => this.showIcon(info)
        })];
      } else {
        return [
          new ScreenGridLayer({
            id: "screen-grid-layer",
            data: this.dataGeo,
            cellSizePixels: 14,
            opacity: 1,
            colorRange: [
              [255, 255, 178, 25],
              [254, 217, 118, 85],
              [254, 178, 76, 127],
              [253, 141, 60, 170],
              [240, 59, 32, 212],
              [189, 0, 38, 255]
            ],
            gpuAggregation: true,
            aggregation: 'SUM',
            getPosition: (d) => [d.log, d.lat],
            getWeight: 4
          })
        ];
      }
    }
  }
}
</script>

<style scoped>
  #map {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: #e5e9ec;
    overflow: hidden;
  }
</style>