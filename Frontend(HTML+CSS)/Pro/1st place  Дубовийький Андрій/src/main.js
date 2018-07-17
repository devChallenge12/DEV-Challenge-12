import Vue from 'vue/dist/vue.js'
import HeaderSidebar from './components/header-sidebar.vue'
import Main from './components/main.vue'

new Vue({
  el: '#app',
  components: {
    navbar: HeaderSidebar,
    main_content: Main
  }
});
