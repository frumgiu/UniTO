<template>
  <div class="filter-sidebar-container collapse-container" id="sidenav">
    <div class="filter-sidebar-inner">
      <span class="material-icons filter-icon">filter_list</span>
      <span class="filter-title">Filters</span>
      <hr class="solid">
      <p class="title-list"> {{titleMenu}} </p>
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
export default {
  name: "FilterTable",
  props: ['titleMenu', 'options', 'defaultMin', 'defaultMax'],
  data() {
    return {
      selectedMinYear: this.defaultMax, selectedMaxYear: this.defaultMax,
      minYearToChoice: [], maxYearToChoice: [],
      checkedOptions: [],
    }
  },
  methods: {
    filterByTag: function () {
      this.$emit('askDataByFilter', this.checkedOptions, this.selectedMinYear, this.selectedMaxYear);
    },
    getNumbersMin: function () {
      this.minYearToChoice = new Array(this.selectedMaxYear - this.defaultMin + 1).fill(this.defaultMin).map((n, i) => n+i);
    },
    getNumbersMax: function () {
      this.maxYearToChoice = new Array(this.defaultMax - this.selectedMinYear + 1).fill(this.defaultMax).map((n, i) => n-i);
      this.maxYearToChoice.reverse();
    }
  },
  mounted() {
    this.getNumbersMin();
    this.getNumbersMax();
  },
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
  @import "../resources/stylesheets/button-filter.css";
  @import "../resources/stylesheets/date-filter.css";
  @import "../resources/stylesheets/collapse-filter.css";

  hr.solid {
    border-top: 0.09rem solid #967bdc;
    margin: 0.5rem;
  }

  .title-list{
    width: fit-content;
    max-width: 100%;
    font-size: 0.95rem;
    cursor: default;
  }
</style>