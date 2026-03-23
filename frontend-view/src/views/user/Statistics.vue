<template>
    <div class="statistics-container">

        <!-- ── Section title ── -->
        <h3 class="section-title">Statistics</h3>

        <!-- ── Type filter pills ── -->
        <div class="type-bar">
            <span
                v-for="(typeItem, index) in interactionTypes"
                :key="index"
                class="type-pill"
                :class="{ active: item.id === typeItem.id }"
                @click="typeSelected(typeItem)"
            >
                {{ typeItem.name }}
            </span>
        </div>

        <!-- ── Chart ── -->
        <div class="chart-wrap">
            <LineChart
                @on-selected="onSelected"
                tag="Streaming Data"
                height="420px"
                :values="values"
                :date="dates"
            />
        </div>

    </div>
</template>

<script>
import LineChart from "@/components/LineChart.vue"
export default {
    components: { LineChart },
    name: "Statistics",
    data() {
        return {
            interactionQueryDto: { day: 365, type: null },
            values: [],
            dates: [],
            interactionTypes: [],
            item: {}
        }
    },
    created() {
        this.fetchStatisticDay();
        this.fetchInteractionTypes();
    },
    methods: {
        typeSelected(typeItem) {
            this.item = typeItem;
            this.interactionQueryDto.type = typeItem.id;
            this.fetchStatisticDay();
        },
        fetchInteractionTypes() {
            this.$axios.get('/interaction/types').then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.interactionTypes = data.data;
                    if (this.interactionTypes.length > 0) {
                        this.item = this.interactionTypes[0];
                        this.interactionQueryDto.type = this.interactionTypes[0].id;
                    }
                    this.fetchStatisticDay();
                }
            }).catch(error => { console.log('Error', error); });
        },
        onSelected(day) {
            this.interactionQueryDto.day = day;
            this.fetchStatisticDay();
        },
        fetchStatisticDay() {
            this.$axios.post('/interaction/daysQuery', this.interactionQueryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.values = data.data.map(e => e.count);
                    this.dates  = data.data.map(e => e.name);
                }
            }).catch(error => { console.log('Error', error); });
        }
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.statistics-container {
    font-family: 'DM Sans', sans-serif;
    padding: 4px 0;
}

/* ─── Section title ──────────────────────────────────── */
.section-title {
    font-family: 'Caveat', cursive;
    font-weight: 700;
    font-size: 36px;
    color: #c8392b;
    margin: 0 0 18px 0;
    line-height: 1;
    transform: rotate(-0.6deg);
    display: inline-block;
}

/* ─── Type filter pills ──────────────────────────────── */
.type-bar {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 20px;
}

.type-pill {
    display: inline-block;
    padding: 5px 16px;
    background-color: rgba(255,255,255,0.6);
    border: 1.5px solid #d6c9b8;
    border-radius: 20px;
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #3a3028;
    cursor: pointer;
    transition: background-color 0.15s, border-color 0.15s, color 0.15s;

    &:hover {
        border-color: #c8392b;
        color: #c8392b;
    }

    &.active {
        background-color: #c8392b;
        border-color: #c8392b;
        color: #fdf8f2;
    }
}

/* ─── Chart wrapper ──────────────────────────────────── */
.chart-wrap {
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    background-color: rgba(255,255,255,0.55);
    padding: 16px;
}
</style>