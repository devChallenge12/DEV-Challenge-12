<template>
  <div class="main__content">
    <div class="content__header">
      <div class="col-desktop-9">
        <h1 class="page__title">Мій словник</h1>
        <p class="page__descr">
          У цей інтерактивний словник Ви можете додавати невідомі слова чи фрази, слідкувати за прогресом їх вивчення та дізнатись про них більш детальну інформацію
        </p>
      </div>
      <div class="col-desktop-3 text-right">
        <a href="#" class="action-btn primary add" type="button" name="button"
        @click.prevent="newWordWindow = !newWordWindow">
          <span class="icon_add"></span>Додати слово / фразу
        </a>
        </div>
    </div>
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
      <div class="filter__view">
        <span class="view_grid">
          <span></span>
          <span></span>
          <span></span>
          <span></span>
          <span></span>
          <span></span>
          <span></span>
          <span></span>
          <span></span>
        </span>
        <span class="view_list active">
          <span></span>
          <span></span>
          <span></span>
        </span>
      </div>
    </div>
    <div class="wodrs__table">
      <grid
          :data="gridData"
          :columns="gridColumns"
          :filter-key="searchQuery">
        </grid>
    </div>
    <div class="new-word__form_wrapper" :class="{ open: newWordWindow }">
      <div class="form__content">
        <a href="#" class="back-button" @click.prevent="newWordWindow = !newWordWindow">Повернутись до словника</a>
        <h1 class="page__title">Додайте слово</h1>
        <p class="page__descr">
          Додайте слово або фразу, які ви хочете запам’ятати. Далі ви зможете повторювати додані слова у режимі тестування
        </p>
        <div class="new-word__form">
          <div class="field">
            <input id="original" type="text" name="original" v-model="newWord.original">
            <label for="original" :class="{ not_empty: newWord.original.length > 0 }">Оригінал</label>
          </div>
          <div class="field">
            <input id="translate" type="text" name="translate" v-model="newWord.translate">
            <label for="translate" :class="{ not_empty: newWord.translate.length > 0 }">Переклад</label>
          </div>
          <ul class="category__select">
            <li class="selected" data-select="Не обрана">Без категорії</li>
          </ul>
          <div class="actions">
            <a href="#" class="action-btn primary no-bdrs" @click.prevent="addWord">Підтвердити</a>
            <a href="#" class="action-btn no-bdrs" @click.prevent="newWordWindow = !newWordWindow">Відмінити</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

  import grid from './grid.vue'

  export default {
    components: {
  		grid
  	},
    data () {
      return {
        newWord: {
          original: '',
          translate: '',
          category: 'Не обрана',
          data: '',
          progress: ''
        },
        searchQuery: '',
        gridColumns: [
          {
            name: 'original',
            value: 'Оригінал'
          },
          {
            name: 'translate',
            value: 'Переклад'
          },
          {
            name: 'category',
            value: 'Категорія'
          },
          {
            name: 'data',
            value: 'Дата'
          },
          {
            name: 'progress',
            value: 'Прогрес'
          }
        ],
        gridData: [
          { original: 'Test word',
            translate: 'Тестове слово',
            category: 'Тест',
            data: '23.06.2018',
            progress: '100%'
          }
        ],
        newWordWindow: false
      }
    },
    watch: {

    },
    created() {

    },
    methods: {
      addWord() {
        if (this.newWord.original || this.newWord.translate) {
          this.newWord.data = this.getDate();
          this.newWord.progress = parseInt(Math.random() * 100) + '%';

          var word = {};
          Object.assign(word, this.newWord);

          this.gridData.push(word);
          this.clear();
          this.newWordWindow = false;
        } else {
          return false;
        }
      },

      clear() {
        this.newWord.original = ''
        this.newWord.translate = ''
        this.newWord.data = ''
        this.newWord.progress = ''
      },

      getDate() {
        var today = new Date(),
            currentData = today.getDay() + '.' + (today.getMonth() + 1) + '.' + 2018;

            return currentData;
      }
    }
  }
</script>
