<template>
    <div class="line-main">
        <div class="chart-header">
            <span class="tag">{{ tag }}</span>
            <div class="time-select">
                <span class="time-label">Period</span>
                <el-select size="mini" style="width: 100px;" v-model="selectedValue" placeholder="Range">
                    <el-option v-for="item in options" :key="item.num" :label="item.name" :value="item.num" />
                </el-select>
            </div>
        </div>
        <div ref="chart" :style="{ width: width, height: height }"></div>
    </div>
</template>

<script>
import * as echarts from 'echarts';
export default {
    name: 'DialogLine',
    props: {
        tag: { type: String, default: 'Chart' },
        values: { type: Array, required: true },
        date:   { type: Array, required: true },
        height: { type: String, default: '220px' },
        width:  { type: String, default: '100%' },
    },
    watch: {
        selectedValue(v) { this.$emit('on-selected', v); },
        values()         { this.initChart(); }
    },
    data() {
        return {
            chart: null,
            options: [
                { num: 7,  name: '7 days' },
                { num: 30, name: '30 days' },
                { num: 60, name: '60 days' },
            ],
            selectedValue: '',
        }
    },
    methods: {
        initChart() {
            this.chart = echarts.init(this.$refs.chart);
            this.chart.setOption({
                grid: { left: 36, right: 16, top: 24, bottom: 32 },
                tooltip: {
                    trigger: 'axis',
                    formatter: '{b}：{c}',
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
                    type: 'line',
                    smooth: true,
                    data: this.values,
                    areaStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0,   color: 'rgba(200, 57, 43, 0.18)' },
                            { offset: 1,   color: 'rgba(200, 57, 43, 0.02)' },
                        ])
                    },
                    lineStyle: { color: '#c8392b', width: 2.5 },
                    itemStyle: {
                        color: '#ffffff',
                        borderColor: '#c8392b',
                        borderWidth: 2.5,
                    },
                    emphasis: {
                        itemStyle: {
                            color: '#c8392b',
                            borderColor: '#ffffff',
                            borderWidth: 2,
                            shadowBlur: 6,
                            shadowColor: 'rgba(200,57,43,0.3)'
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
        },
    },
    mounted() {
        // Resize observer so chart fills container width responsively
        this._ro = new ResizeObserver(() => {
            this.chart && this.chart.resize();
        });
        this._ro.observe(this.$refs.chart);
    },
    beforeDestroy() {
        this._ro && this._ro.disconnect();
        this.chart && this.chart.dispose();
    },
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.line-main {
    width: 100%;
}

/* ─── Header row ─────────────────────────────────────── */
.chart-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;
    flex-wrap: wrap;
    gap: 10px;
}

.tag {
    font-family: 'Klee One', cursive;
    font-size: 15px;
    font-weight: 600;
    color: #3a3028;
    display: inline-block;
}

/* Time selector */
.time-select {
    display: flex;
    align-items: center;
    gap: 8px;
}

.time-label {
    font-family: 'Klee One', cursive;
    font-size: 12px;
    font-weight: 600;
    color: #8a7d6e;
    white-space: nowrap;
}

/* Override Element UI select */
.time-select {
    ::v-deep .el-input__inner {
        border-color: #d6c9b8;
        border-radius: 20px;
        font-family: 'DM Sans', sans-serif;
        font-size: 12px;
        color: #3a3028;
        background-color: rgba(255,255,255,0.7);
        height: 26px;
        line-height: 26px;
        padding: 0 24px 0 10px;

        &:focus { border-color: #c8392b; }
    }

    ::v-deep .el-input__suffix {
        line-height: 26px;
    }
}
</style>