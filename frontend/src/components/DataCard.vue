<script setup>
defineProps({
  item: {
    type: Object,
    required: true,
  },
});
</script>

<template>
  <div class="data-card">
    <div>
      <span class="data-title">{{ item.title }}</span>
    </div>
    <div class="data-body">
      <div v-for="(value, key) in item.metadata" :key="key">
        <span class="data-info"
          ><span class="data-info-label">{{ key }}</span>
          {{ value || "Unknown" }}</span
        >
      </div>
    </div>
  </div>
</template>

<style scoped>
.data-card {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  text-align: left;
  transition: transform 0.2s ease;
}

.data-card:hover {
  border-color: #cbd5e1;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.data-body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
  color: #64748b;
}

.data-title {
  display: block;
  font-weight: bold;
  color: #2c3e50;
  font-size: 1rem;
}

.data-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
  color: #64748b;
}

.data-info-label {
  font-weight: 500;
  color: #94a3b8;
}
</style>
