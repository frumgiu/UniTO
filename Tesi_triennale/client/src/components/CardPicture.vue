<template>
  <div class="card card-style" id="cardpicture" style="display: none">
    <div>
      <img class="card-img-top" v-bind:src="getSmallPictureUrl" alt="Card image cap">
      <a v-bind:href="getBigPictureUrl" target="_blank">
        <button class="open-img-btn fa fa-camera" />
      </a>
    </div>
    <div class="card-body">
      <h5 class="card-title">{{ this.$store.state.pictureInfo.namePicture }}</h5>

      <div class="btn-group-line" style="display: flex; justify-content: space-between; align-items: center;">
        <hr class="solid-short">
        <a v-bind:href="wikiPageUrl" target="_blank">
          <button class="card-btn fa fa-wikipedia-w" data-toggle="tooltip" data-placement="top" title="Open Wikimedia"/>
        </a>
        <button class="close-btn" type="button" data-toggle="tooltip" data-placement="top" title="Close" @click="closeCard">
          <span class="material-icons"  style="vertical-align: middle">close</span>
        </button>
        <hr class="solid-short">
      </div>

      <p class="card-text">Location: {{ this.$store.state.pictureInfo.countryPicture }}, {{ this.$store.state.pictureInfo.regionPicture }}
        <br/>Year: {{ this.$store.state.pictureInfo.yearPicture }} </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "CardPicture",
  data() {
    return {
      namePicture: "",
      cryptoNode: require('crypto')
    }
  },
  methods: {
    openCard: function() {
      const cardPictureId = document.getElementById("cardpicture");
      cardPictureId.style.display = "flex";
      cardPictureId.style.position = "absolute";
      cardPictureId.style.top = "30%";
      cardPictureId.style.left = "50%";
      this.namePicture = this.$store.state.pictureInfo.namePicture;
    },
    closeCard: function () {
      const cardPictureId = document.getElementById("cardpicture");
      cardPictureId.style.display = "none";
    },
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
          + this.namePicture + "/320px-" + this.namePicture;
    },
    getBigPictureUrl() {
      const prefix = this.getPrefix();
      return "https://upload.wikimedia.org/wikipedia/commons/" + prefix
          + this.namePicture;
    },
    wikiPageUrl() {
      return "https://commons.wikimedia.org/wiki/File:" + this.namePicture;
    }
  }
}
</script>

<style scoped>
  @import url("../resources/stylesheets/card-picture-style.css");
</style>