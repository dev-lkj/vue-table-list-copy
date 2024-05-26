<template>
  <div id="app">
    <register-list :list="list" @save-items="updateTable"></register-list>
    <br/><br/><br/>
    <hr/>
    <br/><br/><br/>
    <the-table :items="savedItems"></the-table>
  </div>
</template>

<script>
import RegisterList from './components/RegisterList.vue';
import TheTable from './components/TheTable.vue';

export default {
  name: 'App',
  components: {
    TheTable,
    RegisterList,
  },
  data() {
    return {
      list: [
        {
          id: '1',
          productName: '햇반',
          productNo: '123456',
          appear: 'Y',
          createdAt: '2024-05-01',
          numbering: 1,
        },
        {
          id: '2',
          productName: '쌀국수',
          productNo: '98765',
          appear: 'Y',
          createdAt: '2024-05-02',
          numbering: 2,
        },
        {
          id: '3',
          productName: '라면',
          productNo: '345',
          appear: 'Y',
          createdAt: '2024-05-03',
          numbering: 3,
        },
        {
          id: '4',
          productName: '냉면',
          productNo: '789',
          appear: 'Y',
          createdAt: '2024-05-04',
          numbering: 2,
        },
        {
          id: '5',
          productName: '짜장면',
          productNo: '893',
          appear: 'Y',
          createdAt: '2024-05-05',
          numbering: 1,
        },
      ],
      savedItems: [],
    };
  },
  methods: {
    updateTable(items) {
      this.savedItems = items;
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
