<template>
    <div class="list-box">
      <!-- 왼쪽 테이블: 전체 리스트 -->
      <table border="1" width="500">
        <caption>전체 리스트</caption>
        <thead>
          <tr>
            <th>id</th>
            <th>productName</th>
            <th>노출</th>
            <th>넘버링</th>
            <th>등록일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="lis in list" :key="lis.id" @click="selectItem(lis, 'left')">
            <td>{{ lis.id }}</td>
            <td>{{ lis.productName }}</td>
            <td>{{ lis.appear }}</td>
            <td>{{ lis.numbering }}</td>
            <td>{{ lis.createdAt }}</td>
          </tr>
        </tbody>
      </table>
  
      <div style="margin:20px;">
        <button @click="moveSelectedItem('top')">Top</button><br/>
        <button @click="moveSelectedItem('up')">↑</button>
        <button @click="moveSelectedItem('down')">↓</button><br/>
        <button @click="moveSelectedItem('last')">Last</button><br/>
        <button @click="moveSelectedItem('right')">→</button><br/>
        <button @click="moveSelectedItem('left')">←</button><br/>
        <button @click="saveSelectedItems">Save</button>
      </div>
  
      <!-- 오른쪽 테이블: 선택된 리스트 -->
      <table border="1" width="500">
        <caption>선택된 리스트</caption>
        <thead>
          <tr>
            <th>id</th>
            <th>productName</th>
            <th>노출</th>
            <th>넘버링</th>
            <th>등록일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in selectedItems" :key="item.id" @click="selectItem(item, 'right', index)" :class="{ selected: currentItem && currentItem.id === item.id }">
            <td>{{ item.id }}</td>
            <td>{{ item.productName }}</td>
            <td>{{ item.appear }}</td>
            <td>{{ item.numbering }}</td>
            <td>{{ item.createdAt }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  export default {
    props: ['list'],
    data() {
      return {
        selectedItems: [],
        currentItem: null,
        currentTable: null,
        currentIndex: null,
      };
    },
    methods: {
      selectItem(item, table = 'left', index = null) {
        this.currentItem = item;
        this.currentTable = table;
        this.currentIndex = index;
      },
      moveSelectedItem(direction) {
        if (!this.currentItem) return;
  
        if (direction === 'right' && !this.selectedItems.some(item => item.id === this.currentItem.id)) {
          this.selectedItems.push({ ...this.currentItem, numbering: this.selectedItems.length + 1 });
        } else if (direction === 'left' && this.currentTable === 'right') {
          this.selectedItems = this.selectedItems.filter(item => item.id !== this.currentItem.id);
          this.renumberItems();
          this.currentItem = null;
          this.currentIndex = null;
          return;
        } else if (direction === 'up' && this.currentTable === 'right') {
          this.moveUp();
        } else if (direction === 'down' && this.currentTable === 'right') {
          this.moveDown();
        } else if (direction === 'top' && this.currentTable === 'right') {
          this.moveToTop();
        } else if (direction === 'last' && this.currentTable === 'right') {
          this.moveToLast();
        }
      },
      moveUp() {
        if (this.currentIndex > 0) {
          const item = this.selectedItems.splice(this.currentIndex, 1)[0];
          this.selectedItems.splice(this.currentIndex - 1, 0, item);
          this.currentIndex--;
          this.renumberItems();
        }
      },
      moveDown() {
        if (this.currentIndex < this.selectedItems.length - 1) {
          const item = this.selectedItems.splice(this.currentIndex, 1)[0];
          this.selectedItems.splice(this.currentIndex + 1, 0, item);
          this.currentIndex++;
          this.renumberItems();
        }
      },
      moveToTop() {
        if (this.currentIndex !== null) {
          const item = this.selectedItems.splice(this.currentIndex, 1)[0];
          this.selectedItems.unshift(item);
          this.currentIndex = 0;
          this.renumberItems();
        }
      },
      moveToLast() {
        if (this.currentIndex !== null) {
          const item = this.selectedItems.splice(this.currentIndex, 1)[0];
          this.selectedItems.push(item);
          this.currentIndex = this.selectedItems.length -1 ;
          //alert(this.selectedItems.length)
          //alert(this.currentIndex);
          this.renumberItems();
        }
      },
      renumberItems() {
        this.selectedItems.forEach((item, index) => {
          item.numbering = index + 1;
        });
      },
      saveSelectedItems() {
        const sortedItems = [...this.selectedItems].sort((a, b) => {
          if (a.numbering === b.numbering) {
            return new Date(a.createdAt) - new Date(b.createdAt);
          }
          return a.numbering - b.numbering;
        });
        this.$emit('save-items', sortedItems);
      }
    },
  };
  </script>
  
  <style scoped>
  .list-box {
    display: flex;
    align-items: center;
  }
  table {
    margin: 0 10px;
  }
  tr {
    cursor: pointer;
  }
  tr:hover {
    background-color: #f0f0f0;
  }
  .selected {
    background-color: #d3d3d3;
  }
  </style>
  