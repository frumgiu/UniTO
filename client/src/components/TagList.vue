<template>
<div class="col-xs-12 selected-filters-container">
  <div class="row">
    <div class="align-items-baseline filter-tag" v-for="(tag, index) in tagList" :key="index">
      <span class="filter-text">{{tag}}</span>
      <button class="delete-btn" data-toggle="tooltip" data-placement="top" title="Remove tag">
        <span class="material-icons"  style="vertical-align: middle; font-size: 0.95rem" @click="removeTag(tag)">clear</span>
      </button>

    </div>
  </div>
</div>
</template>

<script>
export default {
  name: "TagList",
  props: ['tagList'],
  methods: {
    removeTag: function(tag) {
      this.tagList.splice(tag.id, 1);
      //TODO: aggiorna la tabella dei dati
    }
  }
}
</script>

<style scoped>
  .selected-filters-container {
    width: 100%;
    max-width: 100%;
    margin-top: 0.2rem;
    margin-bottom: 0.2rem;
    margin-left: 2rem;
  }

  .filter-tag {
    width: fit-content;
    height: fit-content;
    max-width: 100%;
    padding-left: 0.6rem;
    border: 0.1rem solid white;
    border-radius: 1rem;
    background-color: #967bdc;
  }

  .filter-text {
    font-size: 0.78rem;
    color: white;
    cursor: pointer;
  }

  .delete-btn {
    width: fit-content;
    max-width: 100%;
    cursor: pointer;
    background: none;
    border: none;
    color: white;
  }

  .delete-btn:hover {
    color: #e25a25;
  }
</style>