<template>
  <div class="loader-container">
    <div class="spinner"></div>
    <p>Loading...</p>
  </div>
</template>

<style scoped>
.loader-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px;
  min-height: 300px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f1f5f9;
  border-top: 3px solid var(--primary-color, #42b983);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

p {
  margin-top: 16px;
  font-size: 0.9rem;
  color: #64748b;
  font-weight: 500;
  letter-spacing: 0.02em;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
