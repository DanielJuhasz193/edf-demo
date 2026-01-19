<script setup>
import { ref, onMounted } from "vue";
import LoadingSpinner from "./LoadingSpinner.vue";
import { Line } from "vue-chartjs";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
} from "chart.js";

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  CategoryScale,
  LinearScale,
  PointElement,
);

const props = defineProps({
  item: {
    type: Object,
    default: () => {},
  },
});
const chartData = ref({
  labels: [],
  datasets: [],
});
const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: true,
  scales: {
    y: {
      display: false,
      beginAtZero: false,
      bounds: "data",
      grid: { display: true },
    },
    x: {
      grid: { display: true },
      ticks: {
        maxTicksLimit: 10, // Prevents X-axis from getting crowded
      },
    },
  },
});
const isLoading = ref(true);
const error = ref(false);

onMounted(async () => {
  try {
    const sampleRate = props.item.data.channels[0].sampleNumber;
    const desiredPoints = 150;
    const maxRecording = 3;
    const detailPromises = props.item.data.channels.map(async (channel, i) => {
      const detailRes = await fetch(
        `http://localhost:8080/edf/data?fileName=${encodeURIComponent(
          props.item.name,
        )}&fromRecording=0&toRecording=${maxRecording}&signal=${
          channel.name
        }&precision=${Math.min(
          desiredPoints / maxRecording / sampleRate,
          1,
        ).toFixed(2)}`,
      );
      if (!detailRes.ok) throw new Error();
      const data = await detailRes.json();
      const normalized = data.map((d) => d / 3 + i * 100);

      return {
        label: channel.name,
        data: normalized,
        pointRadius: 0,
        borderColor: "#2563eb",
        borderWidth: 1,
      };
    });

    const results = await Promise.all(detailPromises);

    const pointsCount = results[0].data.length;
    const maxTime = props.item.data.recordingLength / maxRecording;
    const step = maxTime / pointsCount;

    const labels = Array.from(
      { length: pointsCount },
      (_, i) => `+${+(i * step).toFixed(2)} s`,
    );

    chartData.value = {
      labels: labels,
      datasets: results,
    };
  } catch (err) {
    error.value = true;
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <div class="content-area">
    <Transition name="fade" mode="out-in">
      <div v-if="isLoading" key="loading">
        <LoadingSpinner />
      </div>
      <div v-else-if="error" key="error" class="error-box">
        Something went wrong
      </div>
      <div v-else key="chart" class="chart-wrapper">
        <Line id="my-chart-id" :options="chartOptions" :data="chartData" />
      </div>
    </Transition>
  </div>
</template>

<style scoped>
.error-box {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #e74c3c;
  background: #fff5f5;
  border-radius: 12px;
  font-weight: 500;
  padding: 16px;
  text-align: center;
}

.chart-wrapper {
  height: 100%;
  width: 100%;
  position: relative;
}

.chart-wrapper canvas {
  height: 100% !important;
  width: 100% !important;
}
</style>
