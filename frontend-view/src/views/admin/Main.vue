<template>
    <div class="overview-container">

        <!-- ── Stats row ── -->
        <div class="stats-row">
            <div class="stat-card" v-for="(item, index) in staticData" :key="index">
                <div class="stat-value">{{ item.count.toLocaleString() }}</div>
                <div class="stat-label">{{ item.name }}</div>
            </div>
        </div>

        <!-- ── Main layout ── -->
        <div class="main-layout">

            <!-- Left: charts -->
            <div class="chart-col">
                <div class="chart-card">
                    <LineChart
                        height="260px"
                        tag="Content Sharing"
                        @on-selected="netItemSelected"
                        :values="shareValues"
                        :date="shareDates"
                    />
                </div>
                <div class="chart-card">
                    <LineChart
                        height="260px"
                        tag="Gourmets"
                        @on-selected="gourmetItemSelected"
                        :values="gourmetValues"
                        :date="gourmetDates"
                    />
                </div>
            </div>

            <!-- Right: popular gourmets -->
            <div class="sidebar-col">
                <h3 class="sidebar-title">Popular Gourmets</h3>

                <div
                    class="gourmet-item"
                    v-for="(gourmet, index) in gourmetList"
                    :key="index"
                    @click="readGourmet(gourmet)"
                >
                    <img :src="gourmet.cover" class="gourmet-cover" />
                    <div class="gourmet-info">
                        <div class="gourmet-user">
                            <img :src="gourmet.userAvatar" class="user-avatar" />
                            <span class="user-name">{{ gourmet.userName }}</span>
                        </div>
                        <div class="gourmet-title">{{ gourmet.title }}</div>
                        <div class="gourmet-meta">
                            <span class="meta-category">{{ gourmet.categoryName }}</span>
                            <span><i class="el-icon-thumb"></i> {{ gourmet.upvoteCount }}</span>
                            <span><i class="el-icon-view"></i> {{ gourmet.viewCount }}</span>
                            <span><i class="el-icon-collection"></i> {{ gourmet.saveCount }}</span>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
import LineChart from "@/components/LineChart"
import { timeAgo } from "@/utils/data"
export default {
    components: { LineChart },
    data() {
        return {
            staticData: [],
            gourmetList: [],
            shareValues: [],
            shareDates: [],
            gourmetValues: [],
            gourmetDates: [],
            defaultQueryDays: 365,
        }
    },
    created() {
        this.fentchStaticCount();
        this.fentchgourmet();
        this.netItemSelected(this.defaultQueryDays);
        this.gourmetItemSelected(this.defaultQueryDays);
    },
    methods: {
        timeOut(time) { return timeAgo(time); },
        gourmetItemSelected(day) {
            this.$axios.get(`/gourmet/daysQuery/${day}`).then(res => {
                if (res.data.code === 200) {
                    this.gourmetValues = res.data.data.map(d => d.count);
                    this.gourmetDates  = res.data.data.map(d => d.name);
                }
            });
        },
        netItemSelected(day) {
            this.$axios.post('/contentNet/daysQuery', { day }).then(res => {
                if (res.data.code === 200) {
                    this.shareValues = res.data.data.map(d => d.count);
                    this.shareDates  = res.data.data.map(d => d.name);
                }
            });
        },
        readGourmet(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
            this.$router.push('/seeGourmetDetail');
        },
        fentchgourmet() {
            this.$axios.post('/gourmet/queryByView', { current: 1, size: 5 }).then(res => {
                if (res.data.code === 200) this.gourmetList = res.data.data;
            });
        },
        fentchStaticCount() {
            this.$axios.get('/views/staticControls').then(res => {
                if (res.data.code === 200) this.staticData = res.data.data;
            });
        },
    },
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.overview-container {
    padding: 24px;
    font-family: 'DM Sans', sans-serif;
    display: flex;
    flex-direction: column;
    gap: 24px;
}

/* ─── Stats row ──────────────────────────────────────── */
.stats-row {
    display: flex;
    gap: 16px;
    flex-wrap: wrap;
}

.stat-card {
    flex: 1;
    min-width: 140px;
    padding: 18px 20px;
    background-color: rgba(255,255,255,0.7);
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    transition: border-color 0.15s, box-shadow 0.15s;

    &:hover {
        border-color: #c8392b;
        box-shadow: 0 4px 14px rgba(200,57,43,0.08);
    }
}

.stat-value {
    font-family: 'Caveat', cursive;
    font-size: 36px;
    font-weight: 700;
    color: #c8392b;
    line-height: 1;
    margin-bottom: 6px;
}

.stat-label {
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #8a7d6e;
}

/* ─── Main layout ────────────────────────────────────── */
.main-layout {
    display: flex;
    gap: 24px;
    align-items: flex-start;
}

/* ─── Chart column ───────────────────────────────────── */
.chart-col {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.chart-card {
    background-color: rgba(255,255,255,0.55);
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    padding: 16px 20px;
}

/* ─── Sidebar column ─────────────────────────────────── */
.sidebar-col {
    width: 280px;
    flex-shrink: 0;
}

.sidebar-title {
    font-family: 'Klee One', cursive;
    font-size: 18px;
    font-weight: 600;
    color: #c8392b;
    margin: 0 0 16px 0;
}

/* ─── Gourmet item ───────────────────────────────────── */
.gourmet-item {
    display: flex;
    gap: 12px;
    padding: 12px 10px;
    border-radius: 4px;
    border-bottom: 1px solid #f0ebe0;
    cursor: pointer;
    transition: background-color 0.15s;

    &:hover { background-color: rgba(255,255,255,0.6); }
    &:last-child { border-bottom: none; }
}

.gourmet-cover {
    width: 100px;
    height: 68px;
    object-fit: cover;
    border-radius: 4px;
    flex-shrink: 0;
}

.gourmet-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 4px;
    min-width: 0;
}

.gourmet-user {
    display: flex;
    align-items: center;
    gap: 5px;
}

.user-avatar {
    width: 18px;
    height: 18px;
    border-radius: 50%;
    object-fit: cover;
}

.user-name {
    font-family: 'DM Sans', sans-serif;
    font-size: 12px;
    color: #8a7d6e;
}

.gourmet-title {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #2a2018;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.3;
    transition: color 0.15s;

    .gourmet-item:hover & { color: #c8392b; }
}

.gourmet-meta {
    display: flex;
    align-items: center;
    gap: 8px;
    font-family: 'DM Sans', sans-serif;
    font-size: 11px;
    color: #9a8d7e;
    flex-wrap: wrap;

    i { font-size: 11px; }
}

.meta-category {
    display: inline-block;
    padding: 1px 8px;
    border-radius: 20px;
    background-color: rgba(200,57,43,0.08);
    border: 1px solid rgba(200,57,43,0.2);
    color: #c8392b;
    font-size: 10px;
    font-family: 'Klee One', cursive;
    font-weight: 600;
}

/* ─── Responsive ─────────────────────────────────────── */
@media (max-width: 900px) {
    .main-layout { flex-direction: column; }
    .sidebar-col { width: 100%; }
}
</style>