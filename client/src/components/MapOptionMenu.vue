<template>
  <div class="nav-options-container" id="sidemapnav">
    <div class="nav-options-inner">
      <span class="material-icons nav-icon">explore</span>
      <span class="filter-title">Navigation</span>
      <hr class="solid">
      <button class="btn-position" :disabled="!acceptedGeolocation" @click="getUserPosition">
        <span class="material-icons" style="vertical-align: middle; margin-right: 0.2rem">my_location</span>
        Go to my location
      </button>
      <div class="layer-options-container">
        <p class="layer-title">Type of layer</p>
        <div class="layer-options-inner">
          <button class="layer-options" @click="set2dLayer">2D</button>
          <button class="layer-options" @click="set3dLayer">3D</button>
          <button class="layer-options" @click="setIconLayer">icons</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MapOptionMenu",
  data() {
    return {
      acceptedGeolocation: ("geolocation" in navigator),
    }
  },
  methods: {
    set2dLayer: function() {
      console.log("2D button pressed");
      this.$emit('setLayer', "2d");
    },
    set3dLayer: function() {
      console.log("3D button pressed");
      this.$emit('setLayer', "3d");
    },
    setIconLayer: function() {
      console.log("icon button pressed");
      this.$emit('setLayer', "icon");
    },
    getUserPosition() {
      console.log(navigator.geolocation);
      navigator.geolocation.getCurrentPosition(position => {
        const userPosition = {log: position.coords.longitude, lat: position.coords.latitude}
        this.$emit('askUserPosition', userPosition);
      },
      error => {
        this.acceptedGeolocation = false;
        console.log(error.message);
      })
    },
    closeMenu() {
      const menuId = document.getElementById("sidemapnav");
      menuId.style.display = "none";
    }
  }
}
</script>

<style scoped>
@import url('../resources/stylesheets/collapse-map-menu.css');
</style>