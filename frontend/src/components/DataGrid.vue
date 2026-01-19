<script setup>
import DataCard from "./DataCard.vue";

defineProps({
  items: {
    type: Array,
    default: () => [],
  },
});
</script>

<template>
  <div class="data-container">
    <h3>Total: {{ items.length }}</h3>
    <div class="data-grid">
      <DataCard v-for="item in items" :key="item.title" :item="item" />
    </div>
  </div>
</template>

<style scoped>
.data-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30px;
  width: 100%;
  box-sizing: border-box;
}

.data-grid {
  display: grid;
  justify-content: center;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
  width: 100%;
}
</style>
