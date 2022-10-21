<template>
  <div id="gallery-wrapper-elements" class="image-wrapper">
    <img class="card-img-top" v-bind:src="getSmallPictureUrl" loading="lazy" alt="Card image cap">
    <div class="blur-txt-wrapper">
      <div class="wiki-wrapper">
        <div class="blur-txt">Visit on Wikimedia</div>
        <a v-bind:href="wikiPageUrl" target="_blank">
          <button class="wikimedia-btn">
            <span class="material-icons" style="font-size: 1.2rem">launch</span>
          </button>
        </a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "GalleryElement",
  props: ['singleDataFileName'],
  data() {
    return {
      cryptoNode: require('crypto'),
      name: ''
    }
  },
  methods: {
    getPrefix: function () {
      const md5sum = this.cryptoNode.createHash('md5');
      md5sum.update(new Buffer(this.singleDataFileName, 'utf8'));
      const md5val = md5sum.digest('hex');
      return md5val[0] + "/" + md5val[0] + md5val[1] + "/";
    }
  },
  computed: {
    getSmallPictureUrl() {
      const prefix = this.getPrefix();
      return "https://upload.wikimedia.org/wikipedia/commons/thumb/" + prefix
          + this.singleDataFileName + "/320px-" + this.singleDataFileName;
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