<template>
  <div class="nav-options-container" id="sidemapnav">
    <div class="nav-options-inner">
      <span class="material-icons nav-icon">explore</span>
      <span class="filter-title">Navigation</span>
      <hr class="solid">
      <button class="btn-position" >
        <span class="material-icons"  style="vertical-align: middle; margin-right: 0.2rem">my_location</span>
        Go to my location
      </button>
      <div class="layer-options-container">
        <p class="layer-title">Type of layer</p>
        <div class="layer-options-inner">
          <button class="layer-options">2D</button>
          <button class="layer-options">3D</button>
          <button class="layer-options">icons</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MapOptionMenu",
  methods: {
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