<template>
  <div class="card card-style" id="cardpicture" style="display: none">
    <img class="card-img-top" src="https://picsum.photos/600/300/?image=25" alt="Card image cap">
    <div class="card-body">
      <h5 class="card-title">{{ namePicture }}</h5>
      <hr class="solid">
      <p class="card-text">{{ countryPicture }}, {{ regionPicture }} <br/> {{ yearPicture }} </p>
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <a href="https://deck.gl/docs/developer-guide/interactivity" target="_blank">
          <button class="card-btn">
            <span class="material-icons" style="vertical-align: middle">link</span>
            <span class="card-link-text">View on wikipedia</span>
          </button>
        </a>
        <button class="close-btn" type="button" data-toggle="tooltip" data-placement="top" title="Close" @click="closeCard">
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