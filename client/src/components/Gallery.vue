<template>
  <div id="gallery-display">
    <div id="gallery">
      <hr class="solid" style="margin-top: 5rem"/>
      <div class="image-group-wrapper" >
        <div v-for="(value, index) in savedData" :key="index">
          <GalleryElement :singleDataFileName="value.filename"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import GalleryElement from "@/components/GalleryElement";

export default {
  name: "Gallery",
  components: {
    GalleryElement
  },
  props: ['savedData']
}
</script>

<style scoped>
  @import url('../resources/stylesheets/gallery/gallery-container.css');
</style>