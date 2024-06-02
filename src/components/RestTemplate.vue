<template>
    <div id="app">
      <h1>API Data</h1>
      <!-- <div v-if="data">{{ data }}</div> -->
      <div v-if="data"> userId : {{ data.userId }} </div>
      <div v-if="data"> id : {{ data.id }} </div>
      <div v-if="data">  title : {{ data.title }}</div>
      <div v-if="data"> body : {{ data.body }} </div>
      <div v-else>Loading...</div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        data: null
      };
    },
    created() {
      axios.get('http://localhost:8081/fetch-post')
        .then(response => {
          this.data = response.data;
        })
        .catch(error => {
          console.error("There was an error!", error);
        });
    }
  };
  </script>
  
  <style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }
  </style>