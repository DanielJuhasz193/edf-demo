<script setup>
import { ref, onMounted } from "vue";
import FileTable from "./components/FileTable.vue";
import LoadingSpinner from "./components/LoadingSpinner.vue";

const fileEntries = ref([]);
const isLoading = ref(true);

onMounted(async () => {
  try {
    const res = await fetch("/edf/file-names");
    const names = await res.json();

    const detailPromises = names.map(async (name) => {
      const detailRes = await fetch(
        `/edf/files?fileName=${encodeURIComponent(name)}`,
      );
      if (!detailRes.ok) return { name, data: null, error: "Invalid File" };
      const data = await detailRes.json();

      return { name, data, error: null };
    });

    let results = await Promise.all(detailPromises);

    // Sort by time
    results.sort((a, b) => {
      const timeA = a.data?.recordingDateTime || "";
      const timeB = b.data?.recordingDateTime || "";
      return timeB.localeCompare(timeA);
    });

    fileEntries.value = results;
  } catch (err) {
    console.error("Something happened:", err);
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <main class="dashboard-layout">
    <header class="dashboard-header">
      <h1>Medical Records</h1>
    </header>
    <div class="content-area">
      <Transition name="fade" mode="out-in">
        <div v-if="isLoading" key="loading">
          <LoadingSpinner />
        </div>
        <div v-else key="table">
          <FileTable :entries="fileEntries" />
        </div>
      </Transition>
    </div>
  </main>
</template>

<style scoped>
/* Main Layout Wrapper */
.dashboard-layout {
  max-width: 1300px;
  margin: 0 auto;
  padding: 40px 32px;
  min-height: 100vh;
  font-family: "Inter", sans-serif;
}

/* Header Styling */
.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #e2e8f0;
}

.dashboard-header h1 {
  margin: 0;
  font-size: 1.75rem;
  font-weight: 700;
  color: #1e293b;
  letter-spacing: -0.02em;
}

/* Content Area */
.content-area {
  width: 100%;
}

.loader-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 100px 0;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
