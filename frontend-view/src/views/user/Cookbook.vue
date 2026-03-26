<template>
    <div class="cookbook-container">

        <!-- ── Page header row: title + search ── -->
        <div class="page-header">
            <h2 class="page-title">Cookbook</h2>
            <div class="search-wrap">
                <el-input
                    size="small"
                    style="width: 220px;"
                    v-model="cookbookQueryDto.title"
                    placeholder="Search cookbooks…"
                    clearable
                    @clear="handleFilterClear"
                >
                    <el-button slot="append" @click="fetchGourmetData" icon="el-icon-search"></el-button>
                </el-input>
            </div>
        </div>

        <!-- ── Category pills ── -->
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

        <!-- ── Empty state ── -->
        <div v-if="cookbookList.length === 0" class="empty-wrap">
            <el-empty description="No cookbooks found"></el-empty>
        </div>

        <!-- ── Grid ── -->
        <div v-else class="cookbook-grid">
            <div
                class="cookbook-card"
                v-for="(cookbook, index) in cookbookList"
                :key="index"
                @click="readDetail(cookbook)"
            >
                <div class="card-cover">
                    <img :src="cookbook.cover" alt="" />
                </div>
                <div class="card-body">
                    <div class="card-title">{{ cookbook.title }}</div>
                    <div class="card-time">{{ cookbook.createTime }}</div>
                </div>
            </div>
        </div>

        <!-- ── Pagination ── -->
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
</template>

<script>
export default {
    name: "Cookbook",
    data() {
        return {
            cookbookQueryDto: {},
            cookbookList: [],
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
        readDetail(cookbook) {
            sessionStorage.setItem('cookbookInfo', JSON.stringify(cookbook));
            this.$router.push('/cookbookDetail');
        },
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
            this.cookbookQueryDto.categoryId = category.id;
            this.fetchGourmetData();
        },
        fetchCategoryData() {
            this.$axios.post('category/query', {}).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.categories = data.data;
                    this.categories.unshift(this.categoryClick);
                }
            }).catch(error => { console.log(error); });
        },
        fetchGourmetData() {
           const userInfo = JSON.parse(sessionStorage.getItem('userInfo'));
    const queryDto = {
        current: this.currentPage,
        size: this.pageSize,
        key: this.filterText,
        userId: userInfo && userInfo.id, 
        ...this.cookbookQueryDto
    };
    this.$axios.post('/cookbook/queryVisible', queryDto).then(res => {
        const { data } = res;
        if (data.code === 200) {
            this.cookbookList = data.data;
            this.totalItems = data.total;
        }
    }).catch(error => { console.log(error); });
        }
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

/* ─── Page wrapper ───────────────────────────────────── */
.cookbook-container {
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

/* ─── Category pills ─────────────────────────────────── */
.category-bar {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 24px;
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

/* ─── Empty ──────────────────────────────────────────── */
.empty-wrap { margin-top: 60px; }

/* ─── Cookbook grid ──────────────────────────────────── */
.cookbook-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
    gap: 20px;
    margin-bottom: 12px;
}

.cookbook-card {
    cursor: pointer;
    border-radius: 4px;
    border: 1.5px solid #e8ddd0;
    background-color: rgba(255,255,255,0.55);
    overflow: hidden;
    transition: background-color 0.15s, border-color 0.15s, transform 0.15s, box-shadow 0.15s;

    &:hover {
        background-color: rgba(255,255,255,0.9);
        border-color: #c8392b;
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(200, 57, 43, 0.1);
    }
}

.card-cover {
    width: 100%;
    aspect-ratio: 4 / 3;
    overflow: hidden;

    img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        display: block;
        transition: transform 0.3s;
    }
}

.cookbook-card:hover .card-cover img {
    transform: scale(1.04);
}

.card-body {
    padding: 12px 14px 14px;
}

.card-title {
    font-family: 'Klee One', cursive;
    font-size: 17px;
    font-weight: 600;
    color: #2a2018;
    line-height: 1.3;
    margin-bottom: 6px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.cookbook-card:hover .card-title {
    color: #c8392b;
}

.card-time {
    font-family: 'DM Sans', sans-serif;
    font-size: 12px;
    color: #9a8d7e;
}

/* ─── Pagination ─────────────────────────────────────── */
.pagination {
    margin: 20px 0 4px;
    text-align: right;

    ::v-deep .el-pager li.active { color: #c8392b; }
    ::v-deep .el-pager li:hover  { color: #c8392b; }
}

/* ─── Responsive ─────────────────────────────────────── */
@media (max-width: 768px) {
    .cookbook-grid { grid-template-columns: repeat(auto-fill, minmax(160px, 1fr)); }
    .page-header { flex-direction: column; align-items: flex-start; gap: 12px; }
}
</style>