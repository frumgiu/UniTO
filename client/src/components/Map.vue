<template>
  <div class="deck-container">
    <div id="map" ref="map"/>
    <canvas id="deck-canvas" ref="canvas"/>
  </div>
</template>

<script>
import { Deck } from "@deck.gl/core";
import { ScreenGridLayer } from"@deck.gl/aggregation-layers";
import mapboxgl from "mapbox-gl";

export default {
  data() {
    return {
      accessToken: "pk.eyJ1IjoicG9zaWU5OCIsImEiOiJjbDV5MTVteXAwOHRoM2VwZDFlYzN4YTJuIn0.1rRyi4xUKIBqfnhfA9GfVQ",
      mapStyle: "mapbox://styles/posie98/cl5xzs8te001614lidiwsno0m",
      viewState: {
        latitude: 44.3072,
        longitude: 8.484106,
        zoom: 16,
        bearing: 0,
        pitch: 0,
      },
      pathData: [
        {position: [45.054847, 7.686736]},
        {position: [45.071217, 7.685089]},
        {position: [44.30459, 8.48426]},
      ]
    };
  },
  created() {
    this.map = null;
    this.deck = null;
  },
  mounted() {
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

    this.deck = new Deck({
      canvas: this.$refs.canvas,
      width: '100%',
      height: '100%',
      initialViewState: this.viewState,
      controller: true,
      onViewStateChange: ({ viewState }) => {
        this.map.jumpTo({
          center: [viewState.longitude, viewState.latitude],
          zoom: viewState.zoom,
          bearing: viewState.bearing,
          pitch: viewState.pitch,
        });
      },
    })
  },
  computed: {
    getLayers() {
      const layers = new ScreenGridLayer({
        id: 'test-layer',
        data: this.pathData,
        opacity: 1,
        cellSizePixels: 50,
        getPosition: (d) => d.position,
        getWeight: 4,
        colorRange:  [
          [0, 25, 0, 25],
          [0, 85, 0, 85],
          [0, 127, 0, 127],
          [0, 170, 0, 170],
          [0, 190, 0, 190],
          [0, 255, 0, 255]
        ],
        gpuAggregation: true,
        aggregation: 'SUM'
      });
      return [layers];
    }
  },
  watch: {
    //Watch the computed prop, if data change the layer change
    getLayers(newLayers) {
      this.deck.setProps({newLayers});
    }
  }
};
</script>

<style scoped>
.deck-container {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
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
#deck-canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>