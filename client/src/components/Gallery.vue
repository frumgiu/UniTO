<template>
  <div id="gallery-display">
    <div id="gallery" class="gallery-display-inside">
      <div class="image-group-wrapper">
        <div v-for="(value, index) in pageData" :key="index" class="for-wrapper">
          <GalleryElement :singleDataFileName="value.filename"/>
        </div>
      </div>
      <div class="pagination">
        <button class="buttonAction" v-on:click="prev"> Back </button>
        <span class="page-index">page {{ currentPage }}</span>
        <button class="buttonAction" v-on:click="next(savedData.length)"> Next </button>
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
  props: ['savedData'],
  data () {
    return {
      currentPage: 1,
      ElementPerPage: 15
    }
  },
  methods: {
    prev: function () {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    next: function (element) {
      if (this.currentPage <= element/this.ElementPerPage) {
        this.currentPage++;
      }
    }
  },
  computed: {
    indexStarted: function () {
      return (this.currentPage - 1) * this.ElementPerPage;
    },
    indexEnd: function () {
      return this.indexStarted + this.ElementPerPage;
    },
    pageData: function() {
      return this.savedData.slice(this.indexStarted, this.indexEnd);
    }
  }
}
</script>

<style scoped>
  @import url('../resources/stylesheets/gallery/gallery-container.css');
</style>