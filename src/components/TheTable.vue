<template>
    <table border="1" width="500">
      <caption>표제목</caption>
      <thead>
        <tr>
          <th>id</th>
          <th>productName</th>
          <th>노출여부</th>
          <th>넘버링</th>
          <th>등록일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in items" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.productName }}</td>
          <td>{{ item.appear }}</td>
          <td>{{ item.numbering }}</td>
          <td>{{ item.createdAt }}</td>
        </tr>
      </tbody>
    </table>
  </template>
  
  <script>
  export default {
    props: {
      items: {
        type: Array,
        required: true,
      },
    },
  };
  </script>
  