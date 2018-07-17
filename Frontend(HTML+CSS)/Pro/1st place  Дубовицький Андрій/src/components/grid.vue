<template id="grid-template">
  <table>
    <thead>
      <tr>
        <th>
          <input type="checkbox" name="select-all">
        </th>
        <th v-for="key in columns"
          @click="sortBy(key.name)"
          :class="{ active: sortKey == key.name }">
          {{ key.value  }}
          <span class="arrow" :class="sortOrders[key.name] > 0 ? 'asc' : 'dsc'">
          </span>
        </th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="entry in filteredData">
        <td>
          <input type="checkbox" name="select-word">
        </td>
        <td v-for="key in columns">
          {{ entry[key.name]}}
        </td>
      </tr>
    </tbody>
  </table>
</template>
<script>
  export default {
    template: '#grid-template',
  props: {
    data: Array,
    columns: Array,
    filterKey: String
  },
  data: function () {
    var sortOrders = {}
    this.columns.forEach(function (key) {
      sortOrders[key.name] = 1
    })
    return {
      sortKey: '',
      sortOrders: sortOrders
    }
  },
  computed: {
    filteredData: function () {
      var sortKey = this.sortKey
      var filterKey = this.filterKey && this.filterKey.toLowerCase()
      var order = this.sortOrders[sortKey] || 1
      var data = this.data
      if (filterKey) {
        data = data.filter(function (row) {
          return Object.keys(row).some(function (key) {
            return String(row[key]).toLowerCase().indexOf(filterKey) > -1
          })
        })
      }
      if (sortKey) {
        data = data.slice().sort(function (a, b) {
          a = a[sortKey]
          b = b[sortKey]
          return (a === b ? 0 : a > b ? 1 : -1) * order
        })
      }
      return data
    }
  },
  methods: {
    sortBy: function (key) {
      this.sortKey = key
      this.sortOrders[key] = this.sortOrders[key] * -1
    }
  }
  }
</script>
