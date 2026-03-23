<template>
    <div class="operation-container">

        <!-- Custom tab bar -->
        <div class="tab-bar">
            <span
                class="tab-item"
                :class="{ active: activeTab === 'cookbook' }"
                @click="activeTab = 'cookbook'"
            >
                <i class="el-icon-notebook-2"></i> My Cookbook
            </span>
            <span
                class="tab-item"
                :class="{ active: activeTab === 'nutriment' }"
                @click="activeTab = 'nutriment'"
            >
                <i class="el-icon-food"></i> My Nutriment
            </span>
        </div>

        <div class="tab-divider"></div>

        <!-- Tab content -->
        <div class="tab-content">
            <MyCookbook v-if="activeTab === 'cookbook'" />
            <MyCookbookNutriment v-if="activeTab === 'nutriment'" />
        </div>

    </div>
</template>

<script>
import MyCookbook from "@/views/user/MyCookbook"
import MyCookbookNutriment from "@/views/user/MyCookbookNutriment"
export default {
    components: { MyCookbook, MyCookbookNutriment },
    name: "CookbookOperation",
    data() {
        return {
            activeTab: 'cookbook'
        }
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.operation-container {
    max-width: 1100px;
    margin: 0 auto;
    padding: 28px 24px 48px;
    font-family: 'DM Sans', sans-serif;
}

/* ─── Tab bar ────────────────────────────────────────── */
.tab-bar {
    display: flex;
    gap: 6px;
    margin-bottom: 0;
}

.tab-item {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 9px 22px;
    font-family: 'Klee One', cursive;
    font-size: 15px;
    font-weight: 600;
    color: #8a7d6e;
    cursor: pointer;
    border: 1.5px solid transparent;
    border-bottom: none;
    border-radius: 4px 4px 0 0;
    background-color: transparent;
    transition: color 0.15s, background-color 0.15s;

    i { font-size: 15px; }

    &:hover {
        color: #c8392b;
        background-color: rgba(255,255,255,0.5);
    }

    &.active {
        color: #c8392b;
        background-color: rgba(255,255,255,0.75);
        border-color: #e8ddd0;
        border-bottom-color: transparent;
        position: relative;
        /* Cover the divider line beneath active tab */
        bottom: -1.5px;
        z-index: 1;
    }
}

/* ─── Divider below tabs ─────────────────────────────── */
.tab-divider {
    height: 1.5px;
    background-color: #e8ddd0;
    position: relative;
    z-index: 0;
}

/* ─── Content area ───────────────────────────────────── */
.tab-content {
    padding-top: 24px;
}
</style>