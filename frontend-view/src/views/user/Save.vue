<template>
    <div class="save-container">

        <div class="page-header">
            <h2 class="page-title">My Favorites</h2>
            <span v-if="collectionList.length > 0" class="count-badge">
                {{ collectionList.length }} articles
            </span>
        </div>

        <div v-if="collectionList.length === 0" class="empty-wrap">
            <el-empty description="No saved articles yet"></el-empty>
        </div>

        <div v-else>
            <div
                class="item-gourmet"
                v-for="(gourmet, index) in collectionList"
                :key="index"
            >
                <div class="left">
                    <img :src="gourmet.cover" />
                </div>
                <div class="right">
                    <div class="info">
                        <img :src="gourmet.userAvatar" class="avatar" />
                        <span class="username">{{ gourmet.userName }}</span>
                    </div>
                    <div class="title" @click="readGourmet(gourmet)">
                        {{ gourmet.title }}
                    </div>
                    <div class="detail desc">
                        {{ gourmet.detail }}
                    </div>
                    <div class="detail meta">
                        <span>{{ gourmet.createTime }}</span>
                        <span><i class="el-icon-view"></i> {{ gourmet.viewCount }}</span>
                        <span><i class="el-icon-thumb"></i> {{ gourmet.upvoteCount }}</span>
                        <span><i class="el-icon-collection"></i> {{ gourmet.saveCount }}</span>
                        <span><i class="el-icon-star-off"></i> {{ gourmet.rating }}</span>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
export default {
    name: "Save",
    data() {
        return {
            collectionList: [],
        }
    },
    created() {
        this.fetchCollectionData();
    },
    methods: {
        readGourmet(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
            this.$router.push('/gourmetDetail');
        },
        handleFilterClear() {
            this.filterText = '';
            this.fetchCollectionData();
        },
        fetchCollectionData() {
            this.$axios.post('interaction/queryCollectionList', {}).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.collectionList = res.data.data;
                }
            }).catch(error => { console.log(error); });
        }
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.save-container {
    max-width: 1100px;
    margin: 0 auto;
    padding: 28px 24px 48px;
    font-family: 'DM Sans', sans-serif;
}

/* ─── Page header ────────────────────────────────────── */
.page-header {
    display: flex;
    align-items: center;
    gap: 14px;
    margin-bottom: 24px;
}

.page-title {
    font-family: 'Caveat', cursive;
    font-weight: 700;
    font-size: 42px;
    color: #c8392b;
    margin: 0;
    line-height: 1;
    transform: rotate(-0.8deg);
    display: inline-block;
}

.count-badge {
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #c8392b;
    background-color: rgba(200, 57, 43, 0.08);
    border: 1px solid rgba(200, 57, 43, 0.2);
    border-radius: 20px;
    padding: 3px 12px;
}

/* ─── Empty ──────────────────────────────────────────── */
.empty-wrap { margin-top: 60px; }

/* ─── List item ──────────────────────────────────────── */
.item-gourmet {
    display: flex;
    gap: 16px;
    padding: 16px 14px;
    border-radius: 4px;
    cursor: pointer;
    border-bottom: 1px solid #e8ddd0;
    transition: background-color 0.15s;

    &:hover { background-color: rgba(255,255,255,0.55); }
    &:last-child { border-bottom: none; }
}

.left img {
    width: 280px;
    height: 190px;
    object-fit: cover;
    border-radius: 4px;
    display: block;
    flex-shrink: 0;
}

.right {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 6px;
    text-align: left;
}

.info {
    display: flex;
    align-items: center;
    gap: 7px;
}

.avatar {
    width: 24px;
    height: 24px;
    border-radius: 50%;
    object-fit: cover;
}

.username {
    font-family: 'Klee One', cursive;
    font-size: 13px;
    color: #8a7d6e;
}

.title {
    font-family: 'Klee One', cursive;
    font-size: 20px;
    font-weight: 600;
    color: #2a2018;
    padding: 2px 0;
    line-height: 1.3;
    transition: color 0.15s;

    &:hover {
        color: #c8392b;
        text-decoration: underline;
        text-underline-offset: 3px;
    }
}

.detail {
    font-family: 'DM Sans', sans-serif;
    font-size: 14px;
    color: #5a5045;
}

.desc {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    line-height: 1.5;
}

.meta {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    margin-top: auto;
    color: #9a8d7e;
    font-size: 13px;
}

/* ─── Responsive ─────────────────────────────────────── */
@media (max-width: 768px) {
    .left img { width: 160px; height: 110px; }
    .title { font-size: 16px; }
}
</style>