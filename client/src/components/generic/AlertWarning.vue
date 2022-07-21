<template>
  <span>
    <b-alert class="warning-alert" v-model="showDismissibleAlert" variant="warning">
      {{ warningAlertText }}
    </b-alert>
  </span>
</template>

<script>
export default {
  name: "AlertWarning",
  props: ['showDismissibleAlert', 'warningAlertText'],
  methods: {
    setNoInput: function() {
      this.showDismissibleAlert = true;
      this.warningAlertText = 'Please, insert a key word!';
    },
    setInvalidInput: function() {
      this.showDismissibleAlert = true;
      this.warningAlertText = 'Nothing found with this key word.\nPlease, try with a different one!';
    }
  },
  watch: {
    showDismissibleAlert(val) {
      if (val) {
        setTimeout(() => this.showDismissibleAlert = false, 4000)
      }
    }
  }
}
</script>

<style scoped>
  .warning-alert {
    width: fit-content;
    max-width: 100%;
    margin-left: 2rem;
    font-size: 1rem;
  }
</style>