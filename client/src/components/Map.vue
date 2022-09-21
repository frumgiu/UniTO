<template>
  <VueDeckgl
      :layers="layers"
      :viewState="viewState"
      @click="nothing"
      @view-state-change="updateViewState"
      @drag="closeCard"
      class="deck-class">
    <div id="map" ref="map"/>
  </VueDeckgl>
</template>

<script>
import {ScreenGridLayer, HexagonLayer} from "@deck.gl/aggregation-layers";
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
      layerStyle: "3d",
      accessToken: "pk.eyJ1IjoicG9zaWU5OCIsImEiOiJjbDV5MTVteXAwOHRoM2VwZDFlYzN4YTJuIn0.1rRyi4xUKIBqfnhfA9GfVQ",
      mapStyle: "mapbox://styles/posie98/cl7jhub3v005j14nfyksvuc9p",
      viewState: {
        latitude: 44.3072,
        longitude: 8.484106,
        zoom: 5,
        bearing: 0,
        pitch: 0,
      },
    };
  },
  created() {
    this.map = null;
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

    navigator.geolocation.getCurrentPosition(position => {
          const startPositon = {log: position.coords.longitude, lat: position.coords.latitude};
          this.setViewState(startPositon, 8);
        },
        () => {
          console.log("User did not allow geolocation. Starting from a default location")
        })
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
      this.closeNavMenuSmallDevice();
    },
    setViewState: function(obj, zoom) {
      this.viewState = {
        longitude: obj.log,
        latitude: obj.lat,
        zoom: zoom,
        bearing: this.viewState.bearing,
        pitch: this.viewState.pitch,
        transitionDuration: 800
      };
    },
    closeNavMenuSmallDevice: function() {
      if (window.innerWidth <= 1024) {
        this.$emit('askCloseMenus');
      }
    },
    showIcon: function(info) {
      this.setViewState(info.object, this.viewState.zoom);
      this.$emit('askOpenCard', (window.innerWidth/2 - 100), (window.innerHeight/2 - 100), info.object.filename, info.object.country_formal, info.object.region, info.object.year);
      },
    closeCard: function() {
      console.log("close card because I'm dragging the map")
      this.$emit('askCloseCard');
    },
    nothing: function() {}
  },
  computed: {
    layers() {
      if (this.dataGeo.length === 0) {
        return [];
      } else {
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
              getColor: [150, 123, 220],
              pickable: true,
              onClick: (info) => this.showIcon(info)
            })
          ];
        } else {
          if (this.layerStyle === "2d") {
            return [
              new ScreenGridLayer({
                id: "screen-grid-layer",
                data: this.dataGeo,
                cellSizePixels: 14,
                opacity: 1,
                colorRange: [
                  [255, 255, 178, 100],
                  [254, 217, 118, 140],
                  [254, 178, 76, 180],
                  [253, 141, 60, 200],
                  [240, 59, 32, 220],
                  [189, 0, 38, 240]
                ],
                gpuAggregation: true,
                aggregation: 'SUM',
                getPosition: (d) => [d.log, d.lat],
                getWeight: 4
              })
            ];
          } else {
            console.log("Creating 3D layer");
            return [
                new HexagonLayer({
                  id: "hexagon-layer",
                  data: this.dataGeo,
                  colorRange: [
                    [255, 255, 178, 100],
                    [254, 217, 118, 140],
                    [254, 178, 76, 180],
                    [253, 141, 60, 200],
                    [240, 59, 32, 220],
                    [189, 0, 38, 240]
                  ],
                  pickable: false,
                  extruded: true,
                  radius: 2000,
                  coverage: 1,
                  elevationScale: 4,
                  getPosition: (d) => [d.log, d.lat],
                })
            ];
          }
        }
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