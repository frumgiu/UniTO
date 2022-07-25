<template>
  <VueDeckgl
    :layers="layers"
    :viewState="viewState"
    @click="handleClick"
    @view-state-change="updateViewState" >
    <div id="map" ref="map"/>
  </VueDeckgl>
</template>

<script>
import { ScreenGridLayer } from "@deck.gl/aggregation-layers";
import mapboxgl from "mapbox-gl";
import VueDeckgl from 'vue-deck.gl'

export default {
  name: "Map",
  components: {
    VueDeckgl
  },
  props: ['dataGeo'],
  data() {
    return {
      accessToken: "pk.eyJ1IjoicG9zaWU5OCIsImEiOiJjbDV5MTVteXAwOHRoM2VwZDFlYzN4YTJuIn0.1rRyi4xUKIBqfnhfA9GfVQ",
      mapStyle: "mapbox://styles/posie98/cl5xzs8te001614lidiwsno0m",
      pathData: this.dataGeo,
      viewState: {
        latitude: 44.30905,
        longitude: 8.47715,
        zoom: 8,
        bearing: 0,
        pitch: 0,
      },
    };
  },
  created() {
    // We need to set mapbox-gl library here in order to use it in template
    this.map = null;
  },
  methods: {
    updateViewState(viewState) {
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

    handleClick() {
    }
  },
  mounted() {
    // creating the map
    this.map = new mapboxgl.Map({
      accessToken: this.accessToken,
      container: this.$refs.map,
      interactive: false,
      style:
          this.mapStyle || "mapbox://styles/posie98/cl5xzs8te001614lidiwsno0m",
      center: [this.viewState.longitude, this.viewState.latitude],
      zoom: this.viewState.zoom,
      pitch: this.viewState.pitch,
      bearing: this.viewState.bearing,
    });
  },
  computed: {
    layers() {
      const layer = new ScreenGridLayer({
        id: "screen-grid-layer",
        data: this.dataGeo,
        visible: true,
        cellSizePixels: 50,
        opacity: 0.8,
        getPosition: d => d.coordinates,
        getWeight: () => 4
      });
      return [layer];
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