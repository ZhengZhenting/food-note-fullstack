<template>
    <div class="detail-container">

        <!-- Article header -->
        <div class="article-header">
            <h1 class="article-title">{{ cookbook.title }}</h1>
            <div class="article-meta">
                <span class="meta-tag">
                    <i class="el-icon-collection-tag"></i> {{ cookbook.categoryName }}
                </span>
                <span class="meta-sep">·</span>
                <span>{{ cookbook.createTime }}</span>
            </div>
        </div>

        <div class="article-divider"></div>

        <!-- Article body -->
        <div class="article-body" v-html="cookbook.content"></div>

        <!-- Comments -->
        <div class="comments-section">
            <Evaluations v-if="cookbook.id" :contentId="cookbook.id" contentType="COOKBOOK" />
        </div>

    </div>
</template>

<script>
import Evaluations from "@/components/Evaluations"
export default {
    components: { Evaluations },
    name: "CookbookDetail",
    data() {
        return {
            cookbook: {}
        }
    },
    created() {
        this.loadCookbookDetail();
    },
    methods: {
        loadCookbookDetail() {
            const jsonCookbook = sessionStorage.getItem('cookbookInfo');
            this.cookbook = JSON.parse(jsonCookbook);
        },
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.detail-container {
    max-width: 820px;
    margin: 0 auto;
    padding: 36px 24px 60px;
    font-family: 'DM Sans', sans-serif;
}

/* ─── Header ─────────────────────────────────────────── */
.article-header {
    margin-bottom: 14px;
}

.article-title {
    font-family: 'Klee One', cursive;
    font-size: 34px;
    font-weight: 600;
    color: #2a2018;
    margin: 0 0 12px 0;
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

.meta-tag {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    padding: 3px 12px;
    background-color: rgba(200, 57, 43, 0.08);
    border: 1px solid rgba(200, 57, 43, 0.2);
    border-radius: 20px;
    color: #c8392b;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
}

.meta-sep { color: #c0b09e; }

/* ─── Divider ────────────────────────────────────────── */
.article-divider {
    height: 1.5px;
    background-color: #e8ddd0;
    margin-bottom: 28px;
}

/* ─── Article body ───────────────────────────────────── */
.article-body {
    font-family: 'DM Sans', sans-serif;
    font-size: 16px;
    line-height: 1.85;
    color: #3a3028;

    ::v-deep img {
        max-width: 100%;
        border-radius: 4px;
        margin: 14px 0;
        display: block;
    }

    ::v-deep h1, ::v-deep h2, ::v-deep h3 {
        font-family: 'Klee One', cursive;
        color: #2a2018;
        margin-top: 32px;
        margin-bottom: 10px;
    }

    ::v-deep h1 { font-size: 26px; }
    ::v-deep h2 { font-size: 22px; }
    ::v-deep h3 { font-size: 18px; }

    ::v-deep p { margin: 12px 0; }

    ::v-deep ul, ::v-deep ol {
        padding-left: 24px;
        margin: 12px 0;
    }

    ::v-deep li { margin-bottom: 6px; }

    ::v-deep blockquote {
        border-left: 3px solid #c8392b;
        margin: 16px 0;
        padding: 8px 16px;
        background-color: rgba(200, 57, 43, 0.04);
        color: #5a5045;
        border-radius: 0 4px 4px 0;
    }
}

/* ─── Comments ───────────────────────────────────────── */
.comments-section {
    margin-top: 48px;
    border-top: 1.5px solid #e8ddd0;
    padding-top: 28px;
}
</style>