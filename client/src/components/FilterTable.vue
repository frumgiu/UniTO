<template>
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
      <p>From</p>
      <b-form-select v-model="selectedMinYear" :options="minYearToChoice" class="mb-3">
        <template #first>
          <b-form-select-option :value="null" disabled>-- Please select an option --</b-form-select-option>
        </template>
      </b-form-select>
      <p>To</p>
      <b-form-select v-model="selectedMaxYear" :options="maxYearToChoice" class="mb-3">
        <template #first>
          <b-form-select-option :value="null" disabled>-- Please select an option --</b-form-select-option>
        </template>
      </b-form-select>
      <div>{{this.minYear}} - {{this.maxYear}}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FilterTable",
  props: ['titleMenu', 'options'],
  data() {
    return {
      defaultMin: 2010, defaultMax: new Date().getFullYear(),
      selectedMinYear: 2020, selectedMaxYear: 2020,
      checkedOptions: [],
      minYearToChoice: [], maxYearToChoice: [],
    }
  },
  methods: {
    filterByTag: function () {
      this.$emit('askDataByFilter', this.checkedOptions);
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
  @import "../resources/stylesheets/slider-filter.css";

  .filter-table {
    width: 24%;
    min-width: 15%; max-width: 25%;
    position: absolute;
    bottom: 0; right: 0;
    margin-right: 1rem; margin-bottom: 1.4rem;
    z-index: 9;
    background: rgba(255,255,255, 0.8);
    border-radius: 0.8rem;
    padding: 0.2rem 0.2rem;
    text-align: center;
    box-shadow: rgba(0, 0, 0, 0.2) 0.122rem 0.122rem 0.163rem;
  }

  .filter-title {
    font-size: 1.2rem;
    vertical-align: middle;
    cursor: default;
  }
  .filter-title:before {
    font-family: "Material Icons";
    font-size: 1.2rem;
    padding: 0;
    content: "filter_list";
    margin-right: 0.4rem;
  }

  hr.solid {
    border-top: 0.09rem solid #967bdc;
    margin: 0.5rem;
  }

  .title-list{
    width: 100%;
    max-width: 100%;
    font-size: 0.95rem;
    cursor: default;
  }

  .container-button-filter {
    width: 100%;
    max-width: 100%;
    font-size: 0.9rem;
  }
</style>