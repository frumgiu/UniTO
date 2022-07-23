<template>
<nav class="navbar navbar-expand-sm bg-light navbar-light fixed-top">
  <div class="container-fluid nav-container">
    <form class="d-flex input-group w-100" @submit.prevent>
      <input v-model="searchText" type="text" class="search-text" placeholder="search a picture" autocomplete="off" @change="textChanged"/>
      <button class="search-btn" type="button" data-toggle="tooltip" data-placement="top" title="Submit" @click="askDataBySearch">
        <span class="material-icons"  style="vertical-align: middle">search</span>
      </button>
      <DropDown titleMenuCat="Category" :optionsCat='categories' :tag-selected="tagSelected"/>
    </form>
  </div>
</nav>
</template>

<!--
<div class="container-fluid">
  <div class="row justify-content-center">
    <form class="card card-sm search-sec">
      <div class="card-body row align-items-center search-sec-body">
        <div class="col-xs-8 col-md-8 input-container">
          <input id="search" type="text" class="search-text" placeholder="search a picture" autocomplete="off"/>
          <button class="search-btn">
            <span class="material-icons"  style="vertical-align: middle">search</span>
          </button>
        </div>
        <div class="col-xs-4 col-md-4 tag-container">
          <DropDown titleMenu="Category" :options='["Buildings", "Statues", "Parks"]'/>
      </div>
      </div>
    </form>
  </div>
</div>
-->

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
  .nav-container {
    width: 100%;
    max-width: 100%;
  }

  .search-text {
    width: 40%;
    max-width: 100%;
    font-size: 0.94rem;
    padding: 0.3rem;
    border: 0.1rem solid gray;
    border-radius: 0.6rem;
    outline: none;
    transition: all 0.3s;
    align-items: center;
  }

  .search-text:focus {
    border: 0.1rem solid #967bdc;
    outline: none;
    box-shadow: none;
  }

  .search-text::placeholder {
    font-size: 14px;
    transition: all 0.3s;
    color: gray;
  }

  .search-btn {
    width: available;
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