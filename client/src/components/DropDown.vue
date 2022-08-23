<template>
  <span class="drop-sec">
      <b-dropdown variant="none" class="drop-btn" no-caret>
        <template #button-content id="drop-btn">
          <span class="material-icons" style="vertical-align: middle">filter_list</span>
        </template>
        <div class="advanced-search">Filters</div>
        <b-dropdown-divider/>
        <div class="row list-container w-100">
          <DropDownColumn :titleMenu="titleMenuCat" :options="optionsCat" @askDataByFilter="askDataByFilter"/>
        </div>
      </b-dropdown>
  </span>
</template>

<script>
import DropDownColumn from "@/components/DropDownColumn";

export default {
  name: "DropDown",
  components: {DropDownColumn},
  props: ['titleMenuCat', 'optionsCat'],
  methods: {
    askDataByFilter: function(options) {
      this.$emit('askDataByFilter', options)
    }
  }
}
</script>

<style>
  .advanced-search {
    width: 100%;
    max-width: 100%;
    margin: 0.1rem 0.5rem;
    align-items: center;
    color: black;
  }

  .drop-sec {
    width: available;
    background: none;
    border: none;
    margin-left: 0.3rem;
    padding: 0 !important;
  }

 .drop-btn > button {
    width: 100%;
    max-width: 100%;
    border: none;
    color: black;
    margin: 0;
  }
  .drop-btn:hover > button:hover {
    border: none;
    color: #48a36a;
  }

  .dropdown-menu {
    border-radius: 0.6rem !important;
    border: none !important;
  }

  .list-container {
    width: 100%;
    max-width: 100%;
    margin: 0 !important;
  }
</style>