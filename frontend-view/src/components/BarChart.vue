<template>
    <div class="bar-main">
        <div v-if="tag" class="chart-header">
            <span class="tag">{{ tag }}</span>
        </div>
        <div ref="chart" :style="{ width: '100%', height: height }"></div>
    </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
    name: 'BarChart',
    props: {
        height: { type: String, default: '300px' },
        tag:    { type: String, default: '' },
        values: { type: Array, required: true },
        date:   { type: Array, required: true }
    },
    data() {
        return { chart: null }
    },
    mounted() {
        this.init();
        this._ro = new ResizeObserver(() => { this.chart && this.chart.resize(); });
        this._ro.observe(this.$refs.chart);
    },
    watch: {
        values() { this.init(); }
    },
    methods: {
        init() {
            if (!this.chart) {
                this.chart = echarts.init(this.$refs.chart);
            }

            // Warm red-toned palette matching the site
            const palette = [
                '#c8392b',   // primary red
                '#e8825a',   // warm orange-red
                '#d4a574',   // warm tan
                '#b5855a',   // medium brown
                '#8a7d6e',   // warm grey-brown
                '#e8c4a0',   // pale peach
            ];

            this.chart.setOption({
                grid: { left: 36, right: 12, top: 16, bottom: 32 },
                tooltip: {
                    trigger: 'axis',
                    backgroundColor: 'rgba(253,250,245,0.96)',
                    borderColor: '#e8ddd0',
                    borderWidth: 1,
                    textStyle: { color: '#3a3028', fontFamily: 'DM Sans, sans-serif', fontSize: 13 },
                    extraCssText: 'border-radius:4px; box-shadow:0 2px 8px rgba(0,0,0,0.08);'
                },
                xAxis: {
                    data: this.date,
                    axisLine:  { show: false },
                    axisTick:  { show: false },
                    splitLine: { show: false },
                    axisLabel: {
                        color: '#9a8d7e',
                        fontSize: 12,
                        fontFamily: 'DM Sans, sans-serif',
                        interval: 0,
                    },
                },
                yAxis: {
                    axisLine:  { show: false },
                    axisTick:  { show: false },
                    splitLine: {
                        show: true,
                        lineStyle: { color: '#f0ebe0', width: 1 }
                    },
                    axisLabel: {
                        color: '#9a8d7e',
                        fontSize: 12,
                        fontFamily: 'DM Sans, sans-serif',
                    },
                },
                series: [{
                    type: 'bar',
                    data: this.values,
                    barMaxWidth: 40,
                    itemStyle: {
                        color: (params) => palette[params.dataIndex % palette.length],
                        borderRadius: [3, 3, 0, 0],
                    },
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 8,
                            shadowColor: 'rgba(200,57,43,0.25)',
                        }
                    },
                    label: {
                        show: true,
                        position: 'top',
                        color: '#8a7d6e',
                        fontSize: 11,
                        fontFamily: 'DM Sans, sans-serif',
                    },
                }]
            });
        }
    },
    beforeDestroy() {
        this._ro && this._ro.disconnect();
        this.chart && this.chart.dispose();
    }
}
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

.bar-main {
    width: 100%;
}

.chart-header {
    margin-bottom: 10px;
}

.tag {
    font-family: 'Klee One', cursive;
    font-size: 15px;
    font-weight: 600;
    color: #3a3028;
    display: inline-block;
}
</style>