<template>
  <div class="gallery-map-btn-wrapper hide-btn-sm">
    <button :disabled="!galleryOpen" class="gallery-map-btn gallery-close-btn" type="button" data-toggle="tooltip" data-placement="top" title="Close gallery" @click="closeGallery">
      <span class="material-icons"  style="vertical-align: middle">close</span>
    </button>
    <button id="tooltip-expand" class="gallery-map-btn gallery-open-btn" type="button" data-toggle="tooltip" data-placement="top" title="Expand gallery" @click="openGallery">
      <span id="icon-expand" class="material-icons"  style="vertical-align: middle">arrow_forward</span>
    </button>
  </div>
</template>

<script>

export default {
  name: "NavigationBar",
  data() {
    return {
      galleryOpen: true
    }
  },
  methods: {
    closeGallery: function() {
      const gallery = document.getElementById("gallery-display");
      const navbar = document.getElementById("nav-bar");
      gallery.style.display = "none";
      navbar.style.backgroundColor = "white";
      navbar.style.boxShadow = "rgba(0, 0, 0, 0.2) 0.122rem 0.122rem 0.163rem";
      document.getElementById('icon-expand').innerHTML = 'arrow_forward';
      this.galleryOpen = false;
    },
    openGallery: function() {
      const gallery = document.getElementById("gallery-display");
      const navbar = document.getElementById("nav-bar");
      if (gallery.style.display === 'block' && gallery.style.width === '55%') {
        gallery.style.width = '100%';
        document.getElementById('icon-expand').innerHTML = 'arrow_back';
      } else {
        gallery.style.width = '55%';
        gallery.style.display = 'block'
        document.getElementById('icon-expand').innerHTML = 'arrow_forward';
      }
      navbar.style.backgroundColor = "transparent";
      navbar.style.boxShadow = "none";
      this.galleryOpen = true;
    }
  }
}

</script>

<style scoped>
  @import url('../resources/stylesheets/button-navigation.css');
</style>