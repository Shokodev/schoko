<template>
  <div id="app">
    <section class="columns is-fullheight">
      <aside id="menu" class="column is-narrow is-fullheight">
        <p  class="menu-label">Menu</p>
        <Menu></Menu>
      </aside>
      <router-view id="content" class="column"></router-view>
    </section>
  </div>
</template>

<script>
  import Menu from './components/Menu'
  import { mapActions } from "vuex"

  export default {
    name: 'app',
    components: {
      Menu
    },
      methods: {
      ...mapActions(["readSettings"])
  },
    mounted() {
      this.readSettings()
    }
  }

</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;

    margin-top: 0.75em;
  }
  #menu{
    padding-left: 1.5em;

  }

  #content{
    padding-left: 1.5em;
  }
</style>
