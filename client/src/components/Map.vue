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
import {ScreenGridLayer, HexagonLayer, HeatmapLayer} from "@deck.gl/aggregation-layers";
import {IconLayer} from "@deck.gl/layers";
import mapboxgl from "mapbox-gl";
import VueDeckgl from 'vue-deck.gl';
import store from "@/store";
//import mySvg from "../resources/polaroids-polaroid-svgrepo-com.svg";

export default {
  name: "Map",
  components: {
    VueDeckgl
  },
  props: ['dataGeo', 'layerStyle'],
  data() {
    return {
      accessToken: "pk.eyJ1IjoicG9zaWU5OCIsImEiOiJjbDV5MTVteXAwOHRoM2VwZDFlYzN4YTJuIn0.1rRyi4xUKIBqfnhfA9GfVQ",
      mapStyle: "mapbox://styles/posie98/cl7jhub3v005j14nfyksvuc9p?optimize=true",
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
      style: this.mapStyle || "mapbox://styles/posie98/cl7jhub3v005j14nfyksvuc9p?optimize=true",
      center: [this.viewState.longitude, this.viewState.latitude],
      zoom: this.viewState.zoom,
      pitch: this.viewState.pitch,
      bearing: this.viewState.bearing,
    });
  },
  methods: {
    updateViewState: function(viewState) {
      this.viewState = {
        ...viewState
      }
      this.map.jumpTo({
        center: [viewState.longitude, viewState.latitude],
        zoom: viewState.zoom,
        bearing: viewState.bearing,
        pitch: viewState.pitch,
      });
      this.saveMapBBox();
      this.$emit('askUpdateData');
      /* Qunado viene cambiata la vista i menu vengono chiusi sui device piu' piccoli */
      this.closeNavMenuSmallDevice();
    },
    /* Metodo usato per spostare la visuale */
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
    fitBoundsMap: function() {
      console.log("zoom prima: " + this.map.getZoom());
      console.log("FITBOUNDS CALLED");
      this.map.fitBounds(store.state.currentBBInfo);
      const {log, lat} = this.map.getCenter();
      console.log("zoom dopo: " + this.map.getZoom());
      this.setViewState({log, lat}, this.map.getZoom());
    },
    closeNavMenuSmallDevice: function() {
      if (window.innerWidth <= 1024) {
        this.$emit('askCloseMenus');
      }
    },
    /* Metodo che apre la card */
    showIcon: function(info) {
      this.setViewState(info.object, this.viewState.zoom);
      store.dispatch('changePictureInfo',
          {newName: info.object.filename, newCountry: info.object.country_formal, newRegion: info.object.region, newYear: info.object.year})
      this.$emit('askOpenCard');
    },
    /* Metodo che chiude la card quando viene spostata la visuale */
    closeCard: function() {
      //console.log("close card because I'm dragging the map")
      this.$emit('askCloseCard');
    },
    saveMapBBox: function() {
      const result = this.map.getBounds().toArray();
      store.dispatch('changeBBInfo', {newMinLog: result[0][0], newMaxLog: result[1][0], newMinLat: result[0][1], newMaxLat: result[1][1]});
    },
    nothing: function() {}
  },
  computed: {
    layers() {
      if (this.dataGeo.length === 0) {
        return [];
      } else {
        if (this.viewState.zoom <= 20 && this.viewState.zoom > 11) {
          return [
            new IconLayer({
              id: 'icon-layer',
              data: this.dataGeo,
              iconAtlas: 'https://raw.githubusercontent.com/visgl/deck.gl-data/master/website/icon-atlas.png',
              iconMapping: { marker: {x:0, y:0, width: 128, height: 128, mask: true} },
              getIcon: () => 'marker',
            /*  getIcon: () => ({
                url: `data:image/svg+xml;charset=utf-8,${encodeURIComponent(mySvg)}`,
                width: 128,
                height: 128,
                anchorY: 128
              }),*/
              getPosition: (d) => [d.log, d.lat],
              getSize: () => 4,
              sizeScale: 10,
              getColor: [150, 123, 220],
              pickable: true,
              onClick: (info) => this.showIcon(info)
            }) ];
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
              }) ];
          } else if (this.layerStyle === "3d") {
            return [
                  new HexagonLayer({
                  id: "hexagon-layer",
                  data: this.dataGeo,
                  colorRange: [
                    [1, 152, 189],
                    [73, 227, 206],
                    [216, 254, 181],
                    [254, 237, 177],
                    [254, 173, 84],
                    [209, 55, 78]
                  ],
                  pickable: false,
                  extruded: true,
                  radius: 300,
                  coverage: 1,
                  elevationRange: [0, this.viewState.zoom * 800],
                  elevationScale: 20,
                  //getElevationValue: ,
                  getPosition: (d) => [d.log, d.lat]
                }) ];
          } else {
           return new HeatmapLayer({
             id: "heat-layer",
             data: this.dataGeo,
             colorRange: [
               [1, 152, 189],
               [73, 227, 206],
               [216, 254, 181],
               [254, 237, 177],
               [254, 173, 84],
               [209, 55, 78]
             ],
             pickable: false,
             getPosition: (d) => [d.log, d.lat],
             aggregation: "SUM",
             intensity: 1,
             threshold: 0.4
           })
          }
        }
      }
    }
  },
  watch: {
    layerStyle: function() {
      const obj = {log: this.viewState.longitude, lat: this.viewState.latitude};
      this.setViewState(obj, this.viewState.zoom);
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