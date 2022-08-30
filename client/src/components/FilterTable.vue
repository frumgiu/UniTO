<template>
  <div class="filter-table">
    <span class="filter-title">Filters</span>
    <hr class="solid">
    <div class="container">
      <span class="title-list"> {{titleMenu}} </span>
      <ul class="ks-cboxtags">
        <li v-for="(option, index) in options" :key="index" >
          <input v-model="checkedOptions" type="checkbox" :id="index" :value="option" @change="filterByTag">
          <label :for="index">{{ option }}</label>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "FilterTable",
  props: ['titleMenu', 'options'],
  data() {
    return {
      checkedOptions: []
    }
  },
  methods: {
    filterByTag: function () {
      this.$emit('askDataByFilter', this.checkedOptions);
    }
  }
}
</script>

<style scoped>
  .filter-table {
    width: 20%;
    min-width: 15%; max-width: 25%;
    position:absolute;
    bottom: 0; right: 0;
    margin-right: 1rem;
    margin-bottom: 1.4rem;
    z-index: 9;
    background: white;
    border-radius: 0.8rem;
    padding: 0.3rem 0.5rem;
    text-align: center;
  }

  .filter-title {
    font-size: 1.2rem;
    vertical-align: middle;
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
    margin: 0.4rem;
  }

  .title-list{
    width: 100%;
    max-width: 100%;
    font-size: 0.95rem;
  }

  .container {
    width: 100%;
    max-width: 100%;
    font-size: 0.9rem;
    padding: 0;
  }

  ul.ks-cboxtags {
    width: 100%;
    max-width: 100%;
    list-style: none;
    padding: 0;
    position: relative;/* added */
    justify-content: space-evenly;/* added */
    display: flex; flex-flow: row wrap;
  }

  ul.ks-cboxtags li{
    display: inline-block;
    flex: 0 0 auto;
    max-width: 100%;
  }

  ul.ks-cboxtags li label{
    display: inline-block;
    background-color: rgba(255, 255, 255, .9);
    border: 0.1rem solid #c2d9e8;
    color: #60b2f0;
    border-radius: 0.6rem;
    transition: all .2s;
    margin: 0.3rem 0;
    padding: 0.3rem 0.4rem;
    cursor: pointer;
  }

  ul.ks-cboxtags li label::before {
    display: inline-block;
    vertical-align: middle;
    -webkit-font-smoothing: antialiased;
    font-family: "Material Icons";
    font-size: 0.9rem;
    padding-right: 0.2rem;
    content: "add";
    transition: transform .3s ease-in-out;
  }

  ul.ks-cboxtags li input[type="checkbox"]:checked + label::before {
    content: "remove";
    transform: rotate(-360deg);
    transition: transform .3s ease-in-out;
  }

  ul.ks-cboxtags li input[type="checkbox"]:checked + label {
    border: 0.1rem solid #c2d9e8;
    background-color: #60b2f0;
    color: white;
    transition: all .2s;
  }

  ul.ks-cboxtags li input[type="checkbox"] {
    position: absolute;
    opacity: 0;
  }
  ul.ks-cboxtags li input[type="checkbox"]:focus + label {
    border: 0.1rem solid #c2d9e8;
  }
</style>