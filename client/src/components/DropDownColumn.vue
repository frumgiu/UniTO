<template>
  <div class="container">
    <span class="title-list"> {{titleMenu}} </span>
      <ul class="ks-cboxtags">
        <li v-for="(option, index) in options" :key="index" >
          <input v-model="checkedOptions" type="checkbox" :id="index" :value="option" @change="filterByTag">
          <label :for="index">{{ option }}</label>
        </li>
      </ul>
  </div>
</template>

<script>
export default {
  name: "DropDownColumn",
  props: ['titleMenu', 'options'],
  data() {
    return {
      checkedOptions: []
    }
  },
  methods: {
    filterByTag: function () {
      this.$emit('askDataByFilter', this.checkedOptions)
    }
  }
}
</script>

<style scoped>
  .title-list{
    width: 100%;
    max-width: 100%;
    text-align: center;
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
  }

  ul.ks-cboxtags li{
    display: inline-block;
    padding-left: 0.25rem;
    max-width: 100%;
  }

  ul.ks-cboxtags li label{
    display: inline-block;
    background-color: rgba(255, 255, 255, .9);
    border: 0.1rem solid #c2d9e8;
    color: #60b2f0;
    border-radius: 0.6rem;
    transition: all .2s;
    margin: 0.1rem 0;
    padding: 0.3rem 0.4rem;
    cursor: pointer;
  }

  ul.ks-cboxtags li label::before {
    display: inline-block;
    vertical-align: middle;
    -webkit-font-smoothing: antialiased;
    font-family: "Material Icons";
    font-size: 0.9rem;
    padding: 0;
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