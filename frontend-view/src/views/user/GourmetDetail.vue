<template>
    <div class="detail-container">

        <!-- ── Main content + sidebar ── -->
        <div class="detail-layout">

            <!-- ── Left: article ── -->
            <div class="article-col">

                <!-- Sticky header -->
                <div class="article-header">
                    <h1 class="article-title">{{ gourmet.title }}</h1>
                    <div class="article-meta">
                        <img :src="gourmet.userAvatar" class="meta-avatar" />
                        <span class="meta-author">{{ gourmet.userName }}</span>
                        <span class="meta-sep">·</span> 
                        <span>{{ gourmet.createTime }}</span>
                        <span class="meta-sep">·</span>
                        <span><i class="el-icon-view"></i> {{ gourmet.viewCount }}</span>
                        <span><i class="el-icon-thumb"></i> {{ gourmet.upvoteCount }}</span>
                        <span><i class="el-icon-collection"></i> {{ gourmet.saveCount }}</span>
                        <span><i class="el-icon-star-off"></i> {{ gourmet.rating }}</span>
                    </div>
                </div>

                <div class="article-divider"></div>

                <!-- Article body -->
                <div class="article-body" v-html="gourmet.content"></div>

                <!-- Like / Save / Rating actions -->
                <div class="action-bar">
                    <el-tooltip :content="upvoteStatus ? 'Cancel like' : 'Like'" placement="bottom">
                        <span class="action-btn" :class="{ active: upvoteStatus }" @click="operationUpvote()">
                            <i class="el-icon-medal-1"></i>
                            <span>{{ gourmet.upvoteCount }} likes</span>
                        </span>
                    </el-tooltip>

                    <el-tooltip :content="saveStatus ? 'Cancel save' : 'Save'" placement="bottom">
                        <span class="action-btn" :class="{ active: saveStatus }" @click="operationSave()">
                            <i class="el-icon-trophy"></i>
                            <span>{{ gourmet.saveCount }} saves</span>
                        </span>
                    </el-tooltip>

                    <span class="action-btn no-hover">
                        <i class="el-icon-star-off"></i>
                        <span>{{ gourmet.rating }} points</span>
                    </span>
                </div>

                <!-- Rating -->
                <div class="rating-section">
                    <div v-if="!ratingStatus" class="rating-wrap">
                        <span class="rating-label">Your Rating</span>
                        <el-rate
                            @change="ratingEvent"
                            v-model="ratingScore"
                            :texts="['oops', 'disappointed', 'normal', 'good', 'great']"
                            show-text
                        />
                    </div>
                    <div v-else class="rating-done">
                        You have already rated this article ✓
                    </div>
                </div>

                <!-- Comments -->
                <div class="comments-section">
                    <Evaluations v-if="gourmet.id" :contentId="gourmet.id" contentType="GOURMET" />
                </div>

            </div>

            <!-- ── Right: sidebar ── -->
            <div class="sidebar-col">
                <h3 class="sidebar-title">Recommendations</h3>

                <div
                    class="rec-item"
                    v-for="(item, index) in recommendGourmet"
                    :key="index"
                    @click="readGourmet(item)"
                >
                    <img :src="item.cover" class="rec-img" />
                    <div class="rec-info">
                        <div class="rec-user">
                            <img :src="item.userAvatar" class="rec-avatar" />
                            <span>{{ item.userName }}</span>
                        </div>
                        <div class="rec-title">{{ item.title }}</div>
                        <div class="rec-meta">
                            <span><i class="el-icon-view"></i> {{ item.viewCount }}</span>
                            <span><i class="el-icon-thumb"></i> {{ item.upvoteCount }}</span>
                            <span><i class="el-icon-star-off"></i> {{ item.rating }}</span>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
import Evaluations from "@/components/Evaluations"
export default {
    components: { Evaluations },
    name: "GourmetDetail",
    data() {
        return {
            gourmetId: null,
            gourmet: {},
            ratingScore: 0,
            recommendGourmet: [],
            upvoteStatus: false,
            saveStatus: false,
            ratingStatus: false
        }
    },
    created() {
        this.loadGourmetId();
    },
    methods: {
        ratingEvent() {
            this.$axios.post(`/interaction/ratingOperation/${this.gourmetId}/${this.ratingScore}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet = data.data[0];
                    this.dealRating(this.gourmet);
                    this.$notify({ duration: 1000, title: 'rating', message: 'rated successfully', type: 'success' });
                    this.ratingStatus = true;
                } else {
                    this.$message(data.msg);
                }
            }).catch(error => { console.log("Error", error); });
        },
        fetchRatingOperation(contentId) {
            this.$axios.get(`/interaction/ratingStatus/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) this.ratingStatus = data.data > 0;
            }).catch(error => { console.log("Error", error); });
        },
        fetchUpvoteOperation(contentId) {
            this.$axios.get(`/interaction/upvoteStatus/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) this.upvoteStatus = data.data > 0;
            }).catch(error => { console.log("Error", error); });
        },
        fetchSaveOperation(contentId) {
            this.$axios.get(`/interaction/saveStatus/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) this.saveStatus = data.data > 0;
            }).catch(error => { console.log("Error", error); });
        },
        operationSave() {
            this.$axios.post(`/interaction/saveOperation/${this.gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet.saveCount += data.data > 0 ? 1 : -1;
                    this.saveStatus = data.data > 0;
                }
            }).catch(error => { console.log("Error", error); });
        },
        operationUpvote() {
            this.$axios.post(`/interaction/upvoteOperation/${this.gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet.upvoteCount += data.data > 0 ? 1 : -1;
                    this.upvoteStatus = data.data > 0;
                }
            }).catch(error => { console.log("Error", error); });
        },
        readGourmet(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
            this.gourmetId = gourmet.id;
            this.fetchGourmetById(gourmet.id);
            this.viewOperation(gourmet.id);
            this.fetchUpvoteOperation(gourmet.id);
            this.fetchSaveOperation(gourmet.id);
            this.fetchRatingOperation(gourmet.id);
            this.fetchRecommendations(gourmet.id);
        },
        loadGourmetId() {
            this.gourmetId = sessionStorage.getItem('gourmetId');
            this.fetchGourmetById(this.gourmetId);
            this.viewOperation(this.gourmetId);
            this.fetchUpvoteOperation(this.gourmetId);
            this.fetchSaveOperation(this.gourmetId);
            this.fetchRatingOperation(this.gourmetId);
             this.fetchRecommendations(this.gourmetId);
        },
        fetchGourmetById(gourmetId) {
            this.$axios.get(`/gourmet/${gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet = data.data[0];
                    this.dealRating(this.gourmet);
                }
            }).catch(error => { console.log("Error", error); });
        },
        dealRating(gourmet) {
            this.gourmet.rating = gourmet.rating === null ? 0 : gourmet.rating;
        },
        viewOperation(contentId) {
            this.$axios.post(`/interaction/viewOperation/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) console.log("View operation recorded successfully");
            }).catch(error => { console.log("Error", error); });
        },
        fetchRecommendations(gourmetId) {
            this.$axios.get(`/gourmet/${gourmetId}/recommend?topN=4`).then(res => {
                const { data } = res;
                if (data.code === 200) this.recommendGourmet = data.data;
            }).catch(error => { console.log("Error", error); });
        }
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

/* ─── Page wrapper ───────────────────────────────────── */
.detail-container {
    max-width: 1100px;
    margin: 0 auto;
    padding: 32px 24px 60px;
    font-family: 'DM Sans', sans-serif;
}

/* ─── Two-column layout ──────────────────────────────── */
.detail-layout {
    display: flex;
    gap: 32px;
    align-items: flex-start;
}

/* ─── Article column ─────────────────────────────────── */
.article-col {
    flex: 1;
    min-width: 0;
}

/* Sticky header */
.article-header {
    position: sticky;
    top: 58px; /* height of the top nav bar */
    background-color: #f5f0e8;
    z-index: 10;
    padding-bottom: 12px;
}

.article-title {
    font-family: 'Klee One', cursive;
    font-size: 32px;
    font-weight: 600;
    color: #2a2018;
    margin: 0 0 10px 0;
    line-height: 1.3;
}

.article-meta {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 8px;
    font-family: 'DM Sans', sans-serif;
    font-size: 13px;
    color: #8a7d6e;
}

.meta-avatar {
    width: 22px;
    height: 22px;
    border-radius: 50%;
    object-fit: cover;
}

.meta-author {
    font-weight: 500;
    color: #3a3028;
}

.meta-sep {
    color: #c0b09e;
}

.article-divider {
    height: 1.5px;
    background-color: #e8ddd0;
    margin-bottom: 24px;
}

/* Article rich-text body */
.article-body {
    font-family: 'DM Sans', sans-serif;
    font-size: 16px;
    line-height: 1.8;
    color: #3a3028;

    ::v-deep img {
        max-width: 100%;
        border-radius: 4px;
        margin: 12px 0;
    }

    ::v-deep h1, ::v-deep h2, ::v-deep h3 {
        font-family: 'Klee One', cursive;
        color: #2a2018;
        margin-top: 28px;
    }

    ::v-deep p {
        margin: 12px 0;
    }
}

/* ─── Action bar ─────────────────────────────────────── */
.action-bar {
    display: flex;
    justify-content: center;
    gap: 12px;
    margin: 40px 0 28px;
}

.action-btn {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 8px 20px;
    border-radius: 24px;
    border: 1.5px solid #d6c9b8;
    background-color: rgba(255,255,255,0.5);
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #3a3028;
    cursor: pointer;
    transition: background-color 0.15s, border-color 0.15s, color 0.15s;

    i { font-size: 20px; }

    &:hover:not(.no-hover) {
        border-color: #c8392b;
        color: #c8392b;
        background-color: rgba(200,57,43,0.05);
    }

    &.active {
        background-color: #c8392b;
        border-color: #c8392b;
        color: #fdf8f2;
    }

    &.no-hover {
        cursor: default;
    }
}

/* ─── Rating section ─────────────────────────────────── */
.rating-section {
    display: flex;
    justify-content: center;
    margin-bottom: 36px;
}

.rating-wrap {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 14px 24px;
    background-color: rgba(255,255,255,0.55);
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
}

.rating-label {
    font-family: 'Klee One', cursive;
    font-size: 15px;
    font-weight: 600;
    color: #3a3028;
    white-space: nowrap;
}

.rating-done {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #8a7d6e;
    padding: 14px 24px;
    background-color: rgba(255,255,255,0.55);
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
}

/* ─── Comments section ───────────────────────────────── */
.comments-section {
    border-top: 1.5px solid #e8ddd0;
    padding-top: 24px;
}

/* ─── Sidebar ────────────────────────────────────────── */
.sidebar-col {
    width: 260px;
    flex-shrink: 0;
    position: sticky;
    top: 80px;
}

.sidebar-title {
    font-family: 'Klee One', cursive;
    font-size: 20px;
    font-weight: 600;
    color: #c8392b;
    margin: 0 0 16px 0;
}

.rec-item {
    display: flex;
    gap: 10px;
    margin-bottom: 16px;
    cursor: pointer;
    padding: 10px;
    border-radius: 4px;
    border: 1.5px solid transparent;
    transition: background-color 0.15s, border-color 0.15s;

    &:hover {
        background-color: rgba(255,255,255,0.6);
        border-color: #e8ddd0;
    }
}

.rec-img {
    width: 90px;
    height: 70px;
    object-fit: cover;
    border-radius: 4px;
    flex-shrink: 0;
}

.rec-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.rec-user {
    display: flex;
    align-items: center;
    gap: 5px;
    font-family: 'DM Sans', sans-serif;
    font-size: 12px;
    color: #8a7d6e;
}

.rec-avatar {
    width: 18px;
    height: 18px;
    border-radius: 50%;
    object-fit: cover;
}

.rec-title {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #2a2018;
    line-height: 1.3;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;

    &:hover {
        color: #c8392b;
    }
}

.rec-meta {
    display: flex;
    gap: 8px;
    font-family: 'DM Sans', sans-serif;
    font-size: 11px;
    color: #9a8d7e;
    margin-top: auto;
}

/* ─── Responsive ─────────────────────────────────────── */
@media (max-width: 768px) {
    .sidebar-col { display: none; }
    .article-title { font-size: 24px; }
    .action-bar { flex-wrap: wrap; }
}
</style>