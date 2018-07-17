<template>
  <div class="header-sidebar__containet">
    <header class="header" :class="{ active: showHeader }">
      <a href="#" class="logo">
        <img src="img/logo-header.png" alt="logo header">
      </a>
      <div class="header__filter">
        <div class="content__filter">
          <div class="filter__search">
            <label for="content_search" class="icon_search">
            </label>
            <input id="content_search" class="input_search" type="text" name="filter-search" v-model="searchQuery">
          </div>
          <ul class="filter__select">
            <li class="selected">Всі дати</li>
          </ul>
          <ul class="filter__select">
            <li class="selected">Всі категорії</li>
          </ul>
        </div>
        <a href="#" class="action-btn primary add" type="button" name="button"
        @click.prevent="newWordWindow = !newWordWindow">
          <span class="icon_add"></span>Додати слово / фразу
        </a>
      </div>
      <a href="#" class="mobile__toggle" @click.prevent="mobileMenu = !mobileMenu" :class="{ show: mobileMenu }">
        <span class="toggle_top"></span>
        <span class="toggle_mid"></span>
        <span class="toggle_bot"></span>
      </a>
    </header>
    <div class="sidebar" :class="{ mobile_show: mobileMenu }">
      <div class="sidebar__content">
        <a href="#" class="logo">
          <img src="img/logo.png" alt="logo">
        </a>
        <ul class="main__menu">
          <li class="title">Профіль</li>
          <li>
            <a href="#" class="icon icon_user">Мій профіль</a>
          </li>
          <li>
            <a href="#" class="icon icon_bell">Оповіщення</a>
          </li>
          <li>
            <a href="#" class="icon icon_sett">Налаштування</a>
          </li>
          <li class="title">Словник</li>
          <li>
            <a href="#" class="active icon icon_pen">Мій словник</a>
          </li>
          <li>
            <a href="#" class="icon icon_page">Тестування</a>
          </li>
          <li>
            <a href="#" class="icon icon_trash">Архів</a>
          </li>
          <li class="exit">
            <a href="#" class="icon icon_exit">Вийти</a>
          </li>
        </ul>
        <div class="copyright">
          &copy; 2018 - preply.com
          <span>All rights reserved</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      mobileMenu: false,
      showHeader: false
    }
  },

  mounted() {
    var table = document.querySelector('.wodrs__table'),
        self = this;

    window.onscroll = function() {
      var scrollTop = window.pageYOffset,
      box = table.getBoundingClientRect().top,

      tablePosition = parseInt(scrollTop) + parseInt(box);

      console.log(tablePosition , scrollTop);
      if (scrollTop > tablePosition) {
        self.showHeader = true;
      } else {
        self.showHeader = false;
      }
    }

  }
}
</script>
