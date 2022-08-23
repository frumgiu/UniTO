<template>
  <div class="col-sm-6 col-md-6 single-column align-items-baseline">
    <b-dropdown-text style="font-size: 0.9rem; color: gray">{{titleMenu}}</b-dropdown-text>
    <b-form-checkbox-group v-for="(option, index) in options" :key="index">
      <b-form-checkbox @change="filterByTag" type="checkbox" :value="option" v-model="checkedOptions" class="text-item">{{option}}</b-form-checkbox>
    </b-form-checkbox-group>
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
  .single-column{
    width: 100%;
    max-width: 100%;
  }

  .text-item {
    width: 100%;
    max-width: 100%;
    font-size: 0.9rem;
    margin: 0 !important;
  }
</style>