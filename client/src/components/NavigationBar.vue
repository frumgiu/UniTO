<template>
  <div class="gallery-map-btn-wrapper">
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
      windowH: window.innerWidth,
      galleryOpen: true
    }
  },
  mounted() {
    this.windowH = window.innerWidth;
    if (this.windowH > 768 && this.galleryOpen)
      this.changeGalleryStyle('gallery-split', 'gallery-full', 'gallery-close');
    if (this.windowH <= 768 && this.galleryOpen)
      this.changeGalleryStyle('gallery-full', 'gallery-split', 'gallery-close');

    window.onresize = () => {
      this.windowH = window.innerWidth;
     /* if (this.windowH > 768) {
        if (this.galleryOpen) {
          this.changeNavbarStyle('navbar-transparent', 'navbar-white');
        } else {
          this.changeNavbarStyle('navbar-white', 'navbar-transparent');
        }
      } else { */
        if (this.windowH > 768 && this.galleryOpen)
          this.changeGalleryStyle('gallery-split', 'gallery-full', 'gallery-close');
        //this.changeNavbarStyle('navbar-white', 'navbar-transparent');
    //  }
      if (this.windowH <= 768 && this.galleryOpen)
        this.changeGalleryStyle('gallery-full', 'gallery-split', 'gallery-close');
    }
  },
  methods: {
    closeGallery: function() {
      this.changeGalleryStyle('gallery-close', 'gallery-split', 'gallery-full');
      //this.changeNavbarStyle('navbar-white', 'navbar-transparent');
      document.getElementById('icon-expand').innerHTML = 'arrow_forward';
      this.galleryOpen = false;
    },
    openGallery: function() {
      const gallery = document.getElementById("gallery-display");
      if (gallery.classList.contains('gallery-split') || window.innerWidth < 768 ) {
        this.changeGalleryStyle('gallery-full', 'gallery-split', 'gallery-close');
        if (window.innerWidth >= 768) {
          document.getElementById('icon-expand').innerHTML = 'arrow_back';
        }
      } else {
        this.changeGalleryStyle('gallery-split', 'gallery-full', 'gallery-close');
        document.getElementById('icon-expand').innerHTML = 'arrow_forward';
      }
     // if (window.innerWidth >= 768)
        //this.changeNavbarStyle('navbar-transparent', 'navbar-white');
      this.galleryOpen = true;
    },
    changeGalleryStyle: function (add, removeone, removetwo) {
      const gallery = document.getElementById("gallery-display");
      if (typeof gallery !== undefined) {
        gallery.classList.remove(removeone);
        gallery.classList.remove(removetwo);
      }
      gallery.classList.add(add);
    },
    changeNavbarStyle(add, remove) {
      const navbar = document.getElementById("nav-bar");
      navbar.classList.add(add);
      navbar.classList.remove(remove);
    }
  }
}
</script>

<style scoped>
  @import url('../resources/stylesheets/navbar/button-navigation.css');
</style>