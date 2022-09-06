<template>
  <nav class="navbar navbar-expand-sm bg-light navbar-light fixed-top my-navbar">
    <div class="nav-container">
      <form class="input-group" @submit.prevent>
        <div class="search-group">
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
      if (sideNav.style.display === "none") {
        this.closeCard();
        sideNav.style.display = "block";
      } else {
        sideNav.style.display = "none";
      }
    },
    closeCard: function() {
      const cardPictureId = document.getElementById("cardpicture");
      if (cardPictureId.style.display === "flex") {
        cardPictureId.style.display = "none";
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
/* BASE */
.my-navbar {
  width: fit-content;
  max-width: 100%;
  border-radius: 0.8rem;
  margin-left: 1rem;
  margin-top: 1.4rem;
  padding: 0.3rem 0.6rem !important;
  box-shadow: rgba(0, 0, 0, 0.2) 0.122rem 0.122rem 0.163rem;
}

.nav-container {
  width:100%;
  max-width: 100%;
  padding: 0;
  margin: 0;
}

.search-group {
  width: fit-content;
  align-content: baseline;
  border: 0.09rem solid #967bdc;
  border-radius: 0.6rem;
  background-color: #fff;
  padding: 0.06rem;
}

.search-group:focus-within{
  border: 0.1rem solid #967bdc;
}

.search-text {
  width: fit-content;
  max-width: 100%;
  font-size: 0.95rem;
  padding: 0.3rem;
  border: none; border-radius: 1.2rem;
  outline: none;
  transition: all 0.3s;
  align-items: center;
}

.search-text::placeholder {
  font-size: 0.95rem;
  color: gray;
}

.search-btn {
  width: fit-content;
  max-width: 100%;
  cursor: pointer;
  background: none;
  border: none;
  color: black;
}

.search-btn:hover {
  color: #48a36a;
}

/* RESPONSIVE */
@media screen and (min-width: 1281px) {
  .hide-btn-lg {
    display: none!important;
  }
}

@media screen and (min-width: 1025px) and (max-width: 1280px){
  .hide-btn-lg {
    display: none!important;
  }

}

@media screen and (min-width: 768px) and (max-width: 1024px) {
}

@media screen and (min-width: 768px) and (max-width: 1024px) and (orientation: landscape) {
}

@media screen and (min-width: 481px) and (max-width: 767px) {
  .my-navbar {
    width: 100%;
    margin: 0;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
  }
  .input-group {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

@media screen and (min-width: 320px) and (max-width: 480px) {
  .my-navbar {
    width: 100%;
    margin: 0;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
  }
  .input-group {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

@media screen and (min-width: 320px) and (max-width: 480px) and (orientation: landscape) {
  .my-navbar {
    width: 100%;
    margin: 0;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
  }
  .input-group {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

@media screen and (max-width: 319px) {
  .my-navbar {
    width: 100%;
    margin: 0;
    border-top-left-radius: 0;
    border-top-right-radius: 0;
  }
  .input-group {
    display: flex;
    justify-content: space-between;
    align-items: center;
    overflow: hidden;
  }
  .search-text{
    width: 56%;
    font-size: 0.9rem;
  }
  .search-text::placeholder {
    font-size: 0.9rem;
  }
  .search-group {
    width: 65%;
    display: flex;
    justify-content: space-between;
  }
}
</style>