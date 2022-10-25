<template>
  <div class="gallery-map-btn-wrapper">
    <button :disabled="!galleryOpen" class="gallery-map-btn gallery-close-btn" type="button" data-toggle="tooltip" data-placement="top" title="Close gallery" @click="closeGallery">
      <span class="material-icons"  style="vertical-align: middle">close</span>
    </button>
    <button id="tooltip-expand" class="gallery-map-btn gallery-open-btn" type="button" data-toggle="tooltip" data-placement="top" title="Expand gallery" @click="openGallery">
      <span id="icon-expand" class="material-icons"  style="vertical-align: middle">keyboard_arrow_up</span>
    </button>
  </div>
</template>

<script>

export default {
  name: "NavigationBar",
  data() {
    return {
      windowH: window.innerWidth,
      galleryOpen: true
    }
  },
  mounted() {
    this.windowH = window.innerWidth;
    if (this.windowH > 768 && this.galleryOpen) {
      this.changeGalleryStyle('gallery-split', 'gallery-full', 'gallery-close');
      this.$emit('askCloseCard');
    }
    if (this.windowH <= 768 && this.galleryOpen){
      this.changeGalleryStyle('gallery-full', 'gallery-split', 'gallery-close');
      this.$emit('askCloseMenus');
      this.$emit('askCloseCard');
    }

    window.onresize = () => {
      this.windowH = window.innerWidth;
      if (this.windowH > 768 && this.galleryOpen){
        this.changeGalleryStyle('gallery-split', 'gallery-full', 'gallery-close');
        this.$emit('askCloseCard');
      }
      if (this.windowH <= 768 && this.galleryOpen) {
        this.changeGalleryStyle('gallery-full', 'gallery-split', 'gallery-close');
        this.$emit('askCloseMenus');
        this.$emit('askCloseCard');
      }
    }
  },
  methods: {
    closeGallery: function() {
      this.changeGalleryStyle('gallery-close', 'gallery-split', 'gallery-full');
      document.getElementById('icon-expand').innerHTML = 'keyboard_arrow_up';
      this.galleryOpen = false;
    },
    openGallery: function() {
      const gallery = document.getElementById("gallery-display");
      if (gallery.classList.contains('gallery-split') || window.innerWidth < 768 ) {
        this.changeGalleryStyle('gallery-full', 'gallery-split', 'gallery-close');
        this.$emit('askCloseMenus');
        this.$emit('askCloseCard');
        if (window.innerWidth >= 768) {
          document.getElementById('icon-expand').innerHTML = 'keyboard_arrow_down';
        }
      } else {
        this.changeGalleryStyle('gallery-split', 'gallery-full', 'gallery-close');
        document.getElementById('icon-expand').innerHTML = 'keyboard_arrow_up';
        this.$emit('askCloseCard');
      }
      this.galleryOpen = true;
    },
    changeGalleryStyle: function (add, removeone, removetwo) {
      const gallery = document.getElementById("gallery-display");
      if (typeof gallery !== undefined) {
        gallery.classList.remove(removeone);
        gallery.classList.remove(removetwo);
      }
      gallery.classList.add(add);
      this.changeMarginTopGallery();
    },
    changeMarginTopGallery: function() {
      const gallery = document.getElementById("gallery-display");
      const galleryInside = document.getElementById("gallery");
      if (gallery.classList.contains('gallery-split')) {
        galleryInside.style.marginTop = "0.6rem";
      } else {
        galleryInside.style.marginTop = "5rem";
      }
    }
  }
}
</script>

<style scoped>
  @import url('../resources/stylesheets/navbar/button-navigation.css');
</style>