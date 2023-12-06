<template>
  <div class="filter-sidebar-container" id="sidenav">
    <div class="filter-sidebar-inner">
      <span class="material-icons filter-icon">filter_list</span>
      <span class="filter-title">Filters</span>
      <hr class="solid">
      <p class="title-list"> Regions </p>
      <div class="container-button-filter">
        <ul class="ks-cboxtags">
          <li v-for="(option, index) in options" :key="index" >
            <input v-model="checkedOptions" type="checkbox" :id="index" :value="option" @change="filterByTag">
            <label :for="index">{{ option }}</label>
          </li>
        </ul>
      </div>
      <p class="title-list"> Years </p>
      <div class="container-date-filter">
        <div class="sub-container-date-filter">
          <p class="date-label">From</p>
          <b-form-select :options="minYearToChoice" v-model="selectedMinYear" class="date-picker" @change="filterByTag"/>
        </div>
        <div class="sub-container-date-filter">
          <p class="date-label">To</p>
          <b-form-select :options="maxYearToChoice" v-model="selectedMaxYear" class="date-picker" @change="filterByTag"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import store from '@/store';

export default {
  name: "FilterTable",
  props: ['options', 'defaultMin', 'defaultMax'],
  data() {
    return {
      /* Intervallo di anni selezionato */
      selectedMinYear: store.state.filterInfo.lastSelectedMin,
      selectedMaxYear: store.state.filterInfo.lastSelectedMax,
      /* Intervallo di anni disponibile, calcolato in base ai parametri selezionati */
      minYearToChoice: [], maxYearToChoice: [],
      checkedOptions: [],
    }
  },
  methods: {
    filterByTag: function () {
      console.log("selected years: " + this.selectedMinYear + " - " + this.selectedMaxYear);
      store.dispatch('changeFilterInfo', {newMinYear: this.selectedMinYear, newMaxYear: this.selectedMaxYear, newTags: this.checkedOptions})
      this.$emit('askDataByFilter');
    },
    getNumbersMin: function () {
      this.minYearToChoice = new Array(this.selectedMaxYear - this.defaultMin + 1).fill(this.defaultMin).map((n, i) => n+i);
    },
    getNumbersMax: function () {
      this.maxYearToChoice = new Array(this.defaultMax - this.selectedMinYear + 1).fill(this.defaultMax).map((n, i) => n-i);
      this.maxYearToChoice.reverse();
    },
    closeMenu: function () {
      const menuId = document.getElementById("sidenav");
      menuId.style.display = "none";
    }
  },
  mounted() {
    this.getNumbersMin();
    this.getNumbersMax();
  } ,
  watch: {
    selectedMinYear: function (newValue) {
      if (newValue <= this.selectedMaxYear) {
        this.selectedMinYear = newValue;
        this.getNumbersMax();
      }
    },
    selectedMaxYear: function (newValue) {
      if (newValue >= this.selectedMinYear) {
        this.selectedMaxYear = newValue;
        this.getNumbersMin();
      }
    }
  }
}
</script>

<style scoped>
  @import url("../resources/stylesheets/filter-menu/collapse-filter-menu.css");
  @import url("../resources/stylesheets/filter-menu/button-filter.css");
  @import url("../resources/stylesheets/filter-menu/date-filter.css");
</style>