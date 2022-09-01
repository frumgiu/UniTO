<template>
  <div class="container-test">
    <button class="filter-btn" type="button" v-b-toggle="'collapse-2'">
      <span class="material-icons filter-icon">filter_list</span>
      <span class="filter-title">Filters</span>
    </button>
    <b-collapse id="collapse-2" visible>
      <hr class="solid"/>
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
          <b-form-select :options="minYearToChoice" v-model="selectedMinYear" :value="selectedMinYear" class="date-picker" @change.native="filterByMinYear"/>
        </div>
        <div class="sub-container-date-filter">
          <p class="date-label">To</p>
          <b-form-select :options="maxYearToChoice" v-model="selectedMaxYear" :value="selectedMaxYear" class="date-picker" @change.native="filterByMaxYear"/>
        </div>
      </div>
    </b-collapse>
  </div>
</template>

<!--
  <div class="filter-table">
    <span class="filter-title">Filters</span>
    <hr class="solid">
    <div class="container-button-filter">
      <p class="title-list"> {{titleMenu}} </p>
      <ul class="ks-cboxtags">
        <li v-for="(option, index) in options" :key="index" >
          <input v-model="checkedOptions" type="checkbox" :id="index" :value="option" @change="filterByTag">
          <label :for="index">{{ option }}</label>
        </li>
      </ul>
    </div>
    <div class="container-button-filter">
      <p class="title-list"> Years </p>
      <div class="row date-row">
        <p class="date-label">From</p>
        <b-form-select v-model="selectedMinYear" :options="minYearToChoice" class="date-picker" />
        <p class="date-label">To</p>
        <b-form-select v-model="selectedMaxYear" :options="maxYearToChoice" class="date-picker" />
      </div>
    </div>
  </div>
-->

<script>
export default {
  name: "FilterTable",
  props: ['titleMenu', 'options'],
  data() {
    return {
      defaultMin: 2010, defaultMax: new Date().getFullYear(),
      selectedMinYear: 2020, selectedMaxYear: 2020,
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
    },
    filterByMinYear: function (evt) {
      let val = evt.target.value;
      if (val <= this.selectedMaxYear){
        this.selectedMinYear = val;
        this.getNumbersMax();
        this.filterByTag();
      }
    },
    filterByMaxYear: function (evt) {
      let val = evt.target.value;
      if (val >= this.selectedMinYear){
        this.selectedMaxYear = val;
        this.getNumbersMin();
        this.filterByTag();
      }
    }
  },
  mounted() {
    this.getNumbersMin();
    this.getNumbersMax();
  },
  watch: {
    selectedMinYear: function (newValue) {
      if (newValue <= this.selectedMaxYear) {
        this.getNumbersMax();
      }
    },
    selectedMaxYear: function (newValue) {
      if (newValue >= this.selectedMinYear) {
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
    border-top: 0.09rem solid #dabeca;
    margin: 0.5rem;
  }

  .title-list{
    width: fit-content;
    max-width: 100%;
    font-size: 0.95rem;
    cursor: default;
  }
</style>