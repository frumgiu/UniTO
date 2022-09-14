<template>
  <div class="card card-style" id="cardpicture" style="display: none">
    <img class="card-img-top" src="https://www.wikimedia.it/wp-content/uploads/2020/08/loggiato.jpg" alt="Card image cap">
    <div class="card-body">
      <h5 class="card-title">{{ namePicture }}</h5>
      <hr class="solid">
      <p class="card-text">Location: {{ countryPicture }}, {{ regionPicture }} <br/>Year: {{ yearPicture }} </p>
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <a href="https://deck.gl/docs/developer-guide/interactivity" target="_blank">
          <button class="card-btn">
            <span class="material-icons" style="vertical-align: middle">link</span>
            <span class="card-link-text">Wikipedia</span>
          </button>
        </a>
        <button class="close-btn" type="button" data-toggle="tooltip" data-placement="top" title="Close picture" @click="closeCard">
          <span class="material-icons"  style="vertical-align: middle">close</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CardPicture",
  props: ["namePicture", "countryPicture", "regionPicture", "yearPicture"],
  methods: {
    openCard: function(coordTop, coordLeft, namePicture, countryPicture, regionPicture, yearPicture) {
      const cardPictureId = document.getElementById("cardpicture");
      cardPictureId.style.display = "flex";
      cardPictureId.style.position = "absolute";
      cardPictureId.style.top = coordTop + "px";
      cardPictureId.style.left = coordLeft + "px";
      this.namePicture = namePicture;
      this.countryPicture = countryPicture;
      this.regionPicture = regionPicture;
      this.yearPicture = yearPicture;
    },
    closeCard: function () {
      const cardPictureId = document.getElementById("cardpicture");
      cardPictureId.style.display = "none";
    }
  }
}
</script>

<style scoped>
  @import url("../resources/stylesheets/card-picture-style.css");
</style>