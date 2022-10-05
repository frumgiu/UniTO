<template>
  <div class="gallery-map-btn-wrapper hide-btn-sm">
    <button class="gallery-map-btn gallery-close-btn" type="button" data-toggle="tooltip" data-placement="top" title="Close gallery" @click="closeGallery">
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
  methods: {
    closeGallery: function() {
      const gallery = document.getElementById("gallery-display");
      gallery.style.display = "none";
      document.getElementById('icon-expand').innerHTML = 'arrow_forward';
    },
    openGallery: function() {
      const gallery = document.getElementById("gallery-display");
      if (gallery.style.display === 'block' && gallery.style.width === '55%') {
        gallery.style.width = '100%';
        document.getElementById('icon-expand').innerHTML = 'arrow_back';
      } else {
        gallery.style.width = '55%';
        gallery.style.display = 'block'
        document.getElementById('icon-expand').innerHTML = 'arrow_forward';
      }
    }
  }
}
</script>

<style scoped>
  @import url('../resources/stylesheets/button-navigation.css');
</style>