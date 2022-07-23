<template>
<nav class="navbar navbar-expand-sm bg-light navbar-light fixed-top my-navbar">
  <div class="container-fluid nav-container">
    <form class="d-flex input-group w-100" @submit.prevent>
      <div class="search-group justify-content-center">
        <input id="searchTxt" v-model="searchText" type="text" class="search-text" placeholder="Search a picture" autocomplete="off" @change="textChanged"/>
        <button class="search-btn" type="button" data-toggle="tooltip" data-placement="top" title="Submit" @click="askDataBySearch">
          <span class="material-icons"  style="vertical-align: middle">search</span>
        </button>
      </div>
      <DropDown titleMenuCat="Category" :optionsCat='categories' :tag-selected="tagSelected"/>
    </form>
  </div>
</nav>
</template>

<script>
import DropDown from './DropDown';

export default {
  name: "SearchBar",
  components: {
    DropDown
  },
  props: ['tagSelected'],
  data() {
    return {
      searchText: "",
      categories: ["Buildings", "Park", "Statues"]
    }
  },
  methods: {
    askDataBySearch: function() {
      this.$emit('askDataBySearch', this.searchText)
    },
    textChanged: function() {
      this.$emit('textChanged', this.searchText)
    }
  },
  watch: {
    searchText: function(newValue) {
      if (newValue === "") {
        this.$emit('getData')
      }
    },
    deep: true
  }
}
</script>

<style scoped>
  .my-navbar {
    width: fit-content;
    max-width: 100%;
    border-radius: 1.5rem;
    margin: 1rem;
    padding: 0.3rem 0.5rem;
  }

  .nav-container {
    width: 100%;
    max-width: 100%;
    padding: 0;
  }

  .search-group {
    align-content: baseline;
    border: 0.09rem solid #967bdc;
    border-radius: 1.2rem;
    background-color: white;
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
    border: none;
    border-radius: 1.2rem;
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
</style>