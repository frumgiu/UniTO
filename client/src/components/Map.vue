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
import { ScreenGridLayer } from "@deck.gl/aggregation-layers";
import mapboxgl from "mapbox-gl";
import VueDeckgl from 'vue-deck.gl'

const pathData = [
  {coordinates: [7.686736, 45.054847]},
  {coordinates: [7.685089, 45.071217]},
  {coordinates: [37.629005, 55.699336]},
  {coordinates: [12.5928976284, 55.6890472438]}
]

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
      myData: pathData,
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
      style: this.mapStyle || "mapbox://styles/posie98/cl5xzs8te001614lidiwsno0m",
      center: [this.viewState.longitude, this.viewState.latitude],
      zoom: this.viewState.zoom,
      pitch: this.viewState.pitch,
      bearing: this.viewState.bearing,
    });
  },
  computed: {
    layers() {
      const layer =  new ScreenGridLayer({
        id: "screen-grid-layer",
        data: this.dataGeo,
        visible: true,
        cellSizePixels: 20,
        opacity: 0.8,
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
        getWeight: 10
      });
      return [layer];
    }
  }
}
</script>

<style scoped>
  .deck-class {
  }
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