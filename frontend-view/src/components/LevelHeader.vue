<template>
    <div class="level-header">

        <!-- ── Sidebar toggle ── -->
        <span class="toggle-btn" @click="operation">
            <i :class="showFlag ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
        </span>

        <!-- ── Breadcrumb ── -->
        <span class="breadcrumb">
            <span class="breadcrumb-root">Administration</span>
            <span class="breadcrumb-sep">/</span>
            <span class="breadcrumb-current">{{ tag === '' ? 'Dashboard' : tag }}</span>
        </span>

        <!-- ── User dropdown ── -->
        <div class="user-block">
            <el-dropdown class="user-dropdown">
                <span class="user-trigger">
                    <el-avatar :size="28" :src="userInfo.url" />
                    <span class="user-name">{{ userInfo.name }}</span>
                    <i class="el-icon-arrow-down"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item icon="el-icon-user-solid" @click.native="userCenterPanel">My Profile</el-dropdown-item>
                    <el-dropdown-item icon="el-icon-switch-button" @click.native="loginOut">Log Out</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>

    </div>
</template>

<script>
export default {
    name: "LevelHeader",
    data() {
        return {
            showFlag: sessionStorage.getItem('flag') === 'true',
        };
    },
    props: {
        tag:      { type: String, required: true, default: '' },
        userInfo: { type: Object, required: true, default: () => ({}) },
        bag:      { type: String, default: '' },
    },
    methods: {
        userCenterPanel() { this.$emit('eventListener', 'center'); },
        loginOut()        { this.$emit('eventListener', 'loginOut'); },
        operation() {
            this.showFlag = !this.showFlag;
            sessionStorage.setItem('flag', this.showFlag);
            this.$emit('selectOperation', this.showFlag);
        },
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

/* ─── Header bar ─────────────────────────────────────── */
.level-header {
    width: 100%;
    height: 52px;
    padding: 0 24px;
    background-color: #ffffff;
    border-bottom: 1.5px solid #e8ddd0;
    display: flex;
    align-items: center;
    gap: 14px;
    position: relative;
}

/* ─── Sidebar toggle ─────────────────────────────────── */
.toggle-btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    border-radius: 4px;
    cursor: pointer;
    color: #5a5045;
    font-size: 18px;
    flex-shrink: 0;
    transition: background-color 0.15s, color 0.15s;
    user-select: none;

    &:hover {
        background-color: #f5f0e8;
        color: #c8392b;
    }
}

/* ─── Breadcrumb ─────────────────────────────────────── */
.breadcrumb {
    display: flex;
    align-items: center;
    gap: 6px;
    font-family: 'DM Sans', sans-serif;
    font-size: 13px;
}

.breadcrumb-root {
    color: #b0a898;
    font-weight: 400;
}

.breadcrumb-sep {
    color: #d6c9b8;
    font-size: 12px;
}

.breadcrumb-current {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #3a3028;
}

/* ─── User block ─────────────────────────────────────── */
.user-block {
    position: absolute;
    right: 24px;
    top: 50%;
    transform: translateY(-50%);
}

.user-trigger {
    display: flex;
    align-items: center;
    gap: 7px;
    cursor: pointer;
    padding: 4px 10px;
    border-radius: 20px;
    transition: background-color 0.15s;
    user-select: none;

    &:hover { background-color: #f5f0e8; }
}

.user-name {
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #3a3028;
    max-width: 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.user-trigger i {
    color: #9a8d7e;
    font-size: 12px;
}

/* Override Element dropdown */
::v-deep .el-dropdown-menu {
    border: 1.5px solid #e8ddd0 !important;
    border-radius: 4px !important;
    box-shadow: 0 4px 16px rgba(0,0,0,0.08) !important;

    .el-dropdown-menu__item {
        font-family: 'Klee One', cursive;
        font-size: 13px;
        color: #3a3028;

        &:hover {
            background-color: #fdf8f2 !important;
            color: #c8392b !important;
        }
    }
}
</style>