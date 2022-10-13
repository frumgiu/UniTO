<template>
  <div id="gallery-wrapper-elements" class="image-wrapper">
    <div>ciao</div>
  </div>

  <!--
  <div class="image-gallery">{{singleDataFileName}}</div>
  <img class="card-img-top" v-bind:src="getSmallPictureUrl" loading="lazy" width="200" height="200" alt="Card image cap">
      <a v-bind:href="getBigPictureUrl" target="_blank">
        <button class="open-img-btn" data-toggle="tooltip" data-placement="top" title="Open picture">
          <span class="material-icons"  style="vertical-align: middle">launch</span>
        </button>
      </a>
      -->
</template>

<script>
export default {
  name: "GalleryElement",
  props: ['singleDataFileName'],
  data() {
    return {
      cryptoNode: require('crypto')
    }
  },
  methods: {
    getPrefix: function () {
      const md5sum = this.cryptoNode.createHash('md5');
      md5sum.update(new Buffer(this.namePicture, 'utf8'));
      const md5val = md5sum.digest('hex');
      return md5val[0] + "/" + md5val[0] + md5val[1] + "/";
    }
  },
  computed: {
    getSmallPictureUrl() {
      const prefix = this.getPrefix();
      return "https://upload.wikimedia.org/wikipedia/commons/thumb/" + prefix
          + this.singleDataFileName + "/300px-" + this.singleDataFileName;
    },
    getBigPictureUrl() {
      const prefix = this.getPrefix();
      return "https://upload.wikimedia.org/wikipedia/commons/" + prefix
          + this.singleDataFileName;
    },
    wikiPageUrl() {
      return "https://commons.wikimedia.org/wiki/File:" + this.singleDataFileName;
    }
  }
}
</script>

<style scoped>
  @import url('../resources/stylesheets/gallery/gallery-element.css');
</style>