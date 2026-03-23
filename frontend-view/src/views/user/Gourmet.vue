<template>
    <div class="gourmet-container">

        <!-- ── Page header row: title + search ── -->
        <div class="page-header">
            <h2 class="page-title">Gourmet</h2>
            <div class="search-wrap">
                <el-input
                    size="small"
                    style="width: 220px;"
                    v-model="gourmetQueryDto.title"
                    placeholder="Search recipes…"
                    clearable
                    @clear="handleFilterClear"
                >
                    <el-button slot="append" @click="fetchGourmetData" icon="el-icon-search"></el-button>
                </el-input>
            </div>
        </div>

        <!-- ── Main layout ── -->
        <div class="main-layout">

            <!-- Content column -->
            <div class="content-col">

                <!-- Category pills -->
                <div class="category-bar">
                    <span
                        v-for="(category, index) in categories"
                        :key="index"
                        class="item-category"
                        :class="{ active: categoryClick.id === category.id }"
                        @click="categorySelected(category)"
                    >
                        {{ category.name }}
                    </span>
                </div>

                <!-- Empty state -->
                <div v-if="gourmetList.length === 0" class="empty-wrap">
                    <el-empty description="No recipes found"></el-empty>
                </div>

                <!-- Gourmet list -->
                <div v-else>
                    <div
                        class="item-gourmet"
                        v-for="(gourmet, index) in gourmetList"
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

                <!-- Pagination -->
                <el-pagination
                    class="pagination"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalItems"
                />
            </div>

            <!-- Sidebar -->
            <div class="sidebar-col">
                <div class="sidebar-card">
                    <h3 class="sidebar-title">Recommendations</h3>
                    <p class="sidebar-placeholder">Coming soon…</p>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
export default {
    name: "Gourmet",
    data() {
        return {
            gourmetQueryDto: {},
            gourmetList: [],
            categories: [],
            filterText: '',
            currentPage: 1,
            pageSize: 10,
            totalItems: null,
            categoryClick: { id: null, name: 'All' }
        }
    },
    created() {
        this.fetchCategoryData();
        this.fetchGourmetData();
    },
    methods: {
        readGourmet(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
            this.$router.push('/gourmetDetail');
        },
        handleFilterClear() {
            this.filterText = '';
            this.fetchGourmetData();
        },
        handleSizeChange(val) {
            this.pageSize = val;
            this.currentPage = 1;
            this.fetchGourmetData();
        },
        handleCurrentChange(val) {
            this.currentPage = val;
            this.fetchGourmetData();
        },
        categorySelected(category) {
            this.categoryClick = category;
            this.gourmetQueryDto.categoryId = category.id;
            this.fetchGourmetData();
        },
        fetchCategoryData() {
            this.$axios.post('category/query', {}).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.categories = res.data.data;
                    this.categories.unshift(this.categoryClick);
                }
            }).catch(error => {
                console.log(error);
            });
        },
        fetchGourmetData() {
            const queryDto = {
                current: this.currentPage,
                size: this.pageSize,
                key: this.filterText,
                ...this.gourmetQueryDto
            };
            this.$axios.post('gourmet/queryList', queryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmetList = res.data.data;
                    this.totalItems = data.total;
                }
            }).catch(error => {
                console.log(error);
            });
        }
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

/* ─── Page wrapper ───────────────────────────────────── */
.gourmet-container {
    max-width: 1100px;
    margin: 0 auto;
    padding: 28px 24px 48px;
    font-family: 'DM Sans', sans-serif;
}

/* ─── Page header ────────────────────────────────────── */
.page-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
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

/* Search input styled to match page tone */
.search-wrap {
    ::v-deep .el-input__inner {
        background-color: rgba(255,255,255,0.7);
        border-color: #d6c9b8;
        border-radius: 20px 0 0 20px;
        font-family: 'DM Sans', sans-serif;
        color: #2a2018;

        &::placeholder { color: #c0b09e; }
        &:focus { border-color: #c8392b; }
    }
    ::v-deep .el-input-group__append {
        background-color: #c8392b;
        border-color: #c8392b;
        color: #fdf8f2;
        border-radius: 0 20px 20px 0;
        transition: background-color 0.15s;

        &:hover { background-color: #b03226; }
    }
}

/* ─── Main layout ────────────────────────────────────── */
.main-layout {
    display: flex;
    gap: 28px;
    align-items: flex-start;
}

.content-col {
    flex: 1;
    min-width: 0;
}

/* ─── Category pills ─────────────────────────────────── */
.category-bar {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 20px;
}

.item-category {
    display: inline-block;
    padding: 5px 16px;
    background-color: rgba(255,255,255,0.6);
    border: 1.5px solid #d6c9b8;
    border-radius: 20px;
    font-family: 'Klee One', cursive;
    font-size: 14px;
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

/* ─── Empty state ────────────────────────────────────── */
.empty-wrap { margin-top: 60px; }

/* ─── Gourmet card ───────────────────────────────────── */
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

/* ─── Pagination ─────────────────────────────────────── */
.pagination {
    margin: 20px 0 4px;
    text-align: right;

    ::v-deep .el-pager li.active { color: #c8392b; }
    ::v-deep .el-pager li:hover  { color: #c8392b; }
}

/* ─── Sidebar ────────────────────────────────────────── */
.sidebar-col {
    width: 240px;
    flex-shrink: 0;
}

.sidebar-card {
    background-color: rgba(255,255,255,0.55);
    border: 1.5px solid #d6c9b8;
    border-radius: 4px;
    padding: 20px 18px;
}

.sidebar-title {
    font-family: 'Klee One', cursive;
    font-size: 18px;
    font-weight: 600;
    color: #c8392b;
    margin: 0 0 10px 0;
}

.sidebar-placeholder {
    font-family: 'DM Sans', sans-serif;
    font-size: 13px;
    color: #b0a898;
    margin: 0;
}

/* ─── Responsive ─────────────────────────────────────── */
@media (max-width: 768px) {
    .sidebar-col { display: none; }
    .left img { width: 160px; height: 110px; }
    .title { font-size: 16px; }
    .page-header { flex-direction: column; align-items: flex-start; gap: 12px; }
}
</style>