<script setup>
import { ref } from "vue";
import DataGrid from "./DataGrid.vue";
import ChannelChart from "./ChannelChart.vue";

const props = defineProps({
  entries: Object,
});

const expandedChannels = ref(null);
const expandedAnnotations = ref(null);
const expandedSummary = ref(null);
const mappedItems = ref(null);

const toggleChannels = (entry) => {
  expandedAnnotations.value = null;
  expandedSummary.value = null;
  mappedItems.value = [];

  if (expandedChannels.value === entry.name) {
    expandedChannels.value = null;
    mappedItems.value = [];
  } else {
    expandedChannels.value = entry.name;
    mappedItems.value = entry.data.channels.map((channel) => ({
      title: channel.name,
      metadata: { "Type:": channel.type, "Samples:": channel.sampleNumber },
    }));
  }
};

const toggleSummary = (entry) => {
  expandedAnnotations.value = null;
  expandedChannels.value = null;
  mappedItems.value = [];

  if (expandedSummary.value === entry.name) {
    expandedSummary.value = null;
  } else {
    expandedSummary.value = entry.name;
  }
};

const toggleAnnotations = (entry) => {
  expandedSummary.value = null;
  expandedChannels.value = null;
  mappedItems.value = [];

  if (expandedAnnotations.value === entry.name) {
    expandedAnnotations.value = null;
    mappedItems.value = [];
  } else {
    expandedAnnotations.value = entry.name;
    mappedItems.value = entry.data.annotations
      .sort((a, b) => {
        const timeA = a.data?.timeStamp || "";
        const timeB = b.data?.timeStamp || "";
        return timeB.localeCompare(timeA);
      })
      .map((annotation) => ({
        title: annotation.description,
        metadata: { "Timestamp:": `${annotation.timeStamp} s` },
      }));
  }
};
</script>

<template>
  <div class="table-container">
    <table>
      <thead>
        <tr>
          <th>File Name</th>
          <th>Identifier</th>
          <th class="date-column">Date</th>
          <th>Patient</th>
          <th>Length</th>
          <th>Actions</th>
        </tr>
      </thead>

      <tbody v-for="entry in entries" :key="entry.name">
        <tr
          :class="{
            'row-active':
              expandedChannels === entry.name ||
              expandedAnnotations === entry.name,
            'row-error': entry?.error,
          }"
        >
          <td>
            <span class="file-name">{{ entry.name }}</span>
          </td>

          <template v-if="entry?.error">
            <td colspan="4" class="error-cell">
              <strong>Invalid File</strong>
            </td>
          </template>

          <template v-else>
            <td>{{ entry.data.identifier }}</td>
            <td class="date-column">{{ entry.data.recordingDateTime }}</td>
            <td>{{ entry.data.patientName }}</td>
            <td>{{ entry.data.recordingLength }} s</td>
          </template>

          <td>
            <div class="actions">
              <button
                @click="toggleChannels(entry)"
                class="btn-channels"
                :disabled="!!entry.error || entry.data.channels.length === 0"
              >
                {{ expandedChannels === entry.name ? "Hide" : "Channels" }}
              </button>
              <button
                @click="toggleAnnotations(entry)"
                class="btn-channels"
                :disabled="!!entry.error || entry.data.annotations.length === 0"
              >
                {{
                  expandedAnnotations === entry.name ? "Hide" : "Annotations"
                }}
              </button>
              <button
                class="btn-channels"
                @click="toggleSummary(entry)"
                :disabled="!!entry.error"
              >
                {{ expandedSummary === entry.name ? "Hide" : "Preview" }}
              </button>
            </div>
          </td>
        </tr>
        <tr v-if="expandedChannels === entry.name" class="expansion-row">
          <td colspan="100">
            <DataGrid :items="mappedItems" />
          </td>
        </tr>
        <tr
          v-else-if="expandedAnnotations === entry.name"
          class="expansion-row"
        >
          <td colspan="100">
            <DataGrid :items="mappedItems" />
          </td>
        </tr>
        <tr v-else-if="expandedSummary === entry.name" class="expansion-row">
          <td colspan="100">
            <ChannelChart :item="entry" />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
/* Layout Containers */
.table-container {
  padding: 20px;
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

/* Standard Cell Styling */
th,
td {
  text-align: left;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

th {
  background: #f8f9fa;
  font-size: 0.75rem;
  font-weight: 600;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  border-bottom: 2px solid #eee;
}

td {
  font-size: 0.9rem;
  font-variant-numeric: tabular-nums;
}

/* Specific column alignment */
th:last-child,
td:last-child {
  text-align: center;
}

/* Row States & Effects */
tbody tr:not(.expansion-row) {
  transition: background-color 0.15s ease;
}

tbody tr:not(.expansion-row):hover {
  background-color: #f1f5f9;
  cursor: pointer;
}

.date-column {
  white-space: nowrap;
}

.row-active {
  background-color: #f1f5f9;
}

.row-error {
  background: #fffafa;
}

tbody tr.row-error:hover {
  background-color: #fff1f1;
}

.expansion-row td {
  background-color: #f8fafc;
  border-bottom: 2px solid #e2e8f0;
  padding: 0;
}

/* Text & Content Styles */
.file-name {
  font-weight: 500;
  color: var(--text-main);
}

.error-cell {
  color: #e74c3c;
  background: #fff5f5;
}

/* Buttons & Actions */
.actions {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.btn-channels {
  font-family: inherit;
  font-weight: 500;
  padding: 8px 14px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.8rem;
  transition: all 0.2s ease;
  border: 1px solid #e2e8f0;
}

.btn-channels {
  background: white;
  color: var(--text-main);
}

.btn-channels:hover {
  background: #f1f5f9;
  border-color: #cbd5e1;
}

button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

/* Animations */
@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
