<template>
  <div class="fixed-top my-navbar">
    <div class="nav-container">
      <form class="input-group" @submit.prevent>
        <div class="search-group">
          <input id="searchTxt" v-model.lazy="searchText" type="text" class="search-text" placeholder="Insert name or country" autocomplete="off"/>
          <button class="search-btn" type="button" data-toggle="tooltip" data-placement="top" title="Search" @click="askDataBySearch">
            <span class="material-icons"  style="vertical-align: middle">search</span>
          </button>
        </div>
        <div class="btn-container hide-btn-lg">
          <button class="search-btn" type="button" data-placement="top" title="Search options" @click="openMenuFilter">
            <span class="material-icons"  style="vertical-align: middle">filter_list</span>
          </button>
          <button class="search-btn" type="button" data-placement="top" title="Map options" @click="openMenuMap">
          <span class="material-icons"  style="vertical-align: middle">explore</span>
          </button>
        </div>
      </form>
    </div>
  </div>
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
    openMenuFilter: function() {
      const sideNav = document.getElementById("sidenav");
      this.openMenu(sideNav);
    },
    openMenuMap: function() {
      const sidemapnav = document.getElementById("sidemapnav");
      this.openMenu(sidemapnav);
    },
    /* Metodo usato quando mi trovo su un device piu' piccolo e voglio richiedere di aprire un menu */
    openMenu: function (menuId) {
      if (menuId.style.display === "none") {
        this.closeCard();
        menuId.style.display = "block";
      } else {
        menuId.style.display = "none";
      }
    },
    closeCard: function() {
      this.$emit('askCloseCard');
    },
    setSearchOnUserPlace: function(newSearchKey) {
      this.searchText = newSearchKey;
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
@import url('../resources/stylesheets/responsive-navbar.css');
</style>