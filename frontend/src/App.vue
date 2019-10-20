<template>
  <div id="app">
    <section class="columns is-fullheight">
      <aside class="column is-narrow is-fullheight">
        <p class="menu-label">Menu</p>
        <Menu></Menu>
      </aside>
      <router-view class="column"></router-view>
    </section>
  </div>
</template>

<script>
  import Menu from './components/Menu'

  export default {
    name: 'app',
    data(){
      return{
      }
    },

    components: {
      Menu

    }
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;

    margin-top: 60px;
  }
</style>
