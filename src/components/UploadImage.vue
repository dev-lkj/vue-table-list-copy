<template>
    <div id="app">
      <h1>Upload Images</h1>
      <div v-for="(file, index) in files" :key="index">
        <input type="file" @change="onFileChange($event, index)" />
        <button @click="uploadFile(file, index)">Upload</button>
      </div>
      <button @click="addFileInput">Add Another File</button>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        files: [null]
      };
    },
    methods: {
      addFileInput() {
        this.files.push(null);
      },
      onFileChange(event, index) {
        this.files.splice(index, 1, event.target.files[0]);
      },
      uploadFile(file, index) {
        if (!file) {
          alert('No file selected');
          return;
        }
  
        const formData = new FormData();
        formData.append('file', file);
  
        axios.post('http://localhost:8081/upload2', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(response => {
          console.log('File uploaded successfully', response);
          this.files.splice(index, 1, null);
        })
        .catch(error => {
          console.error('Error uploading file', error);
        });
      }
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
  