<template>
  <nav class="navbar navbar-expand-sm bg-light navbar-light fixed-top my-navbar">
    <div class="container-fluid nav-container">
      <form class="d-flex input-group w-100" @submit.prevent>
        <div class="search-group justify-content-center">
          <input id="searchTxt" v-model="searchText" type="text" class="search-text" placeholder="Insert name" autocomplete="off"/>
          <button class="search-btn" type="button" data-toggle="tooltip" data-placement="top" title="Submit" @click="askDataBySearch">
            <span class="material-icons"  style="vertical-align: middle">search</span>
          </button>
        </div>
        <button class="search-btn hide-btn-lg" type="button" @click="openMenu">
          <span class="material-icons"  style="vertical-align: middle">filter_list</span>
        </button>
      </form>
    </div>
  </nav>
</template>

<script>
export default {
  name: "SearchBar",
  data() {
    return {
      searchText: ""
    }
  },
  methods: {
    askDataBySearch: function() {
      this.$emit('askDataBySearch', this.searchText)
    },
    openMenu: function() {
      const sideNav = document.getElementById("sidenav");
      const cardPictureId = document.getElementById("cardpicture");
      if (sideNav.style.display === "none") {
        if (cardPictureId.style.display === "flex") {
          cardPictureId.style.display = "none";
        }
        sideNav.style.display = "block";
      } else {
        sideNav.style.display = "none";
      }
    }
  },
  watch: {
    searchText: function() {
      this.$emit('askDataBySearch', this.searchText)
    },
    deep: true
  }
}
</script>

<style scoped>
@import "../resources/stylesheets/responsive-navbar.css";
</style>