<template>
  <div id="gallery-wrapper-elements" class="image-wrapper">
    <div class="image-gallery">Test della gallery</div>
    <hr class="solid"/>
    <div>{{ singleData.filename }}</div>
  </div>
</template>

<script>
export default {
  name: "GalleryElement",
  props: ['singleData']
}
</script>

<style scoped>
  @import url('../resources/stylesheets/gallery/gallery-element.css');
</style>