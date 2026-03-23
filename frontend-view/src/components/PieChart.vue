<template>
    <div class="pie-main">
        <div v-if="tag" class="chart-header">
            <i class="el-icon-pie-chart header-icon"></i>
            <span class="tag">{{ tag }}</span>
        </div>
        <div ref="chart" :style="{ width: '100%', height: height }"></div>
    </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
    name: 'PieChart',
    props: {
        types:     { type: Array,  default: () => [] },
        values:    { type: Array,  default: () => [] },
        width:     { type: String, default: '100%' },
        tag:       { type: String, default: '' },
        height:    { type: String, default: '260px' },
        bag:       { type: String, default: 'transparent' },
        fontColor: { type: String, default: '#3a3028' },
    },
    data() {
        return { chart: null }
    },
    watch: {
        types()  { this.initChart(); },
        values() { this.initChart(); },
    },
    mounted() {
        this.initChart();
        this._ro = new ResizeObserver(() => { this.chart && this.chart.resize(); });
        this._ro.observe(this.$refs.chart);
    },
    methods: {
        initChart() {
            if (!this.chart) this.chart = echarts.init(this.$refs.chart);

            // Warm palette matching the site
            const palette = [
                '#c8392b',
                '#e8825a',
                '#d4a574',
                '#b5855a',
                '#8a7d6e',
                '#e8c4a0',
                '#c8392b99',
            ];

            this.chart.setOption({
                tooltip: {
                    trigger: 'item',
                    formatter: '{b}: {c} ({d}%)',
                    backgroundColor: 'rgba(253,250,245,0.96)',
                    borderColor: '#e8ddd0',
                    borderWidth: 1,
                    textStyle: { color: '#3a3028', fontFamily: 'DM Sans, sans-serif', fontSize: 13 },
                    extraCssText: 'border-radius:4px; box-shadow:0 2px 8px rgba(0,0,0,0.08);'
                },
                legend: { show: false },
                series: [{
                    type: 'pie',
                    radius: ['30%', '68%'],   // donut style
                    center: ['50%', '50%'],
                    avoidLabelOverlap: true,
                    label: {
                        show: true,
                        position: 'outside',
                        formatter: '{b}\n{d}%',
                        color: '#8a7d6e',
                        fontSize: 12,
                        fontFamily: 'DM Sans, sans-serif',
                        lineHeight: 18,
                    },
                    labelLine: {
                        show: true,
                        lineStyle: { color: '#d6c9b8' }
                    },
                    emphasis: {
                        label: { show: true, fontSize: 13, fontWeight: '600', color: '#2a2018' },
                        itemStyle: {
                            shadowBlur: 10,
                            shadowColor: 'rgba(200,57,43,0.2)'
                        }
                    },
                    data: this.values.map((value, index) => ({
                        name: this.types[index] || `Item ${index + 1}`,
                        value,
                    })),
                    itemStyle: {
                        color: (params) => palette[params.dataIndex % palette.length],
                        borderColor: '#fdfaf5',
                        borderWidth: 2,
                        borderRadius: 3,
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

.pie-main {
    width: 100%;
}

.chart-header {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-bottom: 4px;
}

.header-icon {
    font-size: 15px;
    color: #c8392b;
}

.tag {
    font-family: 'Klee One', cursive;
    font-size: 15px;
    font-weight: 600;
    color: #3a3028;
    display: inline-block;
}
</style>