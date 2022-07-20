<template>
<span class="drop-sec">
    <b-dropdown variant="none" class="drop-btn" no-caret>
      <template #button-content id="drop-btn">
        <span class="material-icons" style="vertical-align: middle">dehaze</span>
      </template>
      <div class="advanced-search">Advanced search</div>
      <div class="row two-colomuns-container w-100">
        <DropDownColumn :titleMenu="titleMenuCat" :options="optionsCat" :tag-selected="tagSelected"/>
      </div>

    </b-dropdown>
</span>
</template>

<!--
<span class="drop-sec">
    <b-dropdown variant="none"  class="drop-btn" no-caret>
      <template #button-content id="drop-btn">
        <span class="material-icons" style="vertical-align: middle">dehaze</span>
      </template>
      <b-dropdown-text>{{titleMenu}}</b-dropdown-text>
      <b-form-checkbox-group v-for="(option, index) in options" :key="index">
        <b-form-checkbox>{{option}}</b-form-checkbox>
      </b-form-checkbox-group>
      <b-dropdown-divider></b-dropdown-divider>
    </b-dropdown>
  </span>
-->

<script>
import DropDownColumn from "@/components/DropDownColumn";

export default {
  name: "DropDown",
  components: {DropDownColumn},
  props: ['titleMenuCat', 'optionsCat', 'tagSelected']
}
</script>

<style>
  .advanced-search {
    width: 100%;
    max-width: 100%;
    align-items: center;
    color: gray;
  }

  .drop-sec {
    width: available;
    background: none;
    border: none;
    margin: 0;
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
    min-width: 200% !important;
    width: 320% !important;
    max-width: 580% !important;
    border-radius: 0.6rem !important;
    border: 0.1rem solid #967bdc !important;
  }

  .two-colomuns-container {
    width: 100%;
    max-width: 100%;
    margin: 0 !important;
  }
</style>