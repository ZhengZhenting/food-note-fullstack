<template>
    <div class="diet-container">

        <!-- ── Header row ── -->
        <div class="page-header">
            <h2 class="page-title">Diet History</h2>
            <span class="add-btn" @click="recordGourmet">
                <i class="el-icon-plus"></i> Add New Diet
            </span>
        </div>

        <!-- ── History list ── -->
        <div v-if="dietHistoryList.length === 0" class="empty-wrap">
            <el-empty description="No diet records yet"></el-empty>
        </div>

        <div v-else class="history-list">
            <div
                class="history-item"
                v-for="(item, index) in dietHistoryList"
                :key="index"
            >
                <div class="item-left">
                    <i class="el-icon-food item-icon"></i>
                </div>
                <div class="item-body">
                    <div class="item-name">{{ getCookbookName(item) }}</div>
                    <div class="item-meta">
                        <span v-if="item.detail" class="meta-note">{{ item.detail }}</span>
                        <span class="meta-value">{{ item.value }}g</span>
                    </div>
                </div>
                <span class="delete-btn" @click="removeCookbook(item)">
                    <i class="el-icon-close"></i>
                </span>
            </div>
        </div>

        <!-- ── Add dialog ── -->
        <el-dialog
            :show-close="false"
            :visible.sync="dialogGourmetOperaion"
            width="36%"
            custom-class="food-dialog"
        >
            <div class="dialog-body">
                <h3 class="dialog-title">Add Cookbook to Diet</h3>

                <div class="dialog-section">
                    <label class="dialog-label">Choose Cookbook</label>
                    <el-select
                        style="width: 100%;"
                        size="small"
                        v-model="dietHistory.cookbookId"
                        placeholder="Select a cookbook"
                    >
                        <el-option v-for="item in cookbooks" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Value / g</label>
                    <input class="dialog-input" v-model="dietHistory.value" placeholder="Enter grams" />
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Notes</label>
                    <el-input
                        type="textarea"
                        :rows="3"
                        placeholder="Add notes (optional)"
                        v-model="dietHistory.detail"
                    />
                </div>
            </div>

            <span slot="footer" class="dialog-footer">
                <span class="cancel-btn" @click="cannel()">Cancel</span>
                <span class="confirm-btn" @click="addOperation()">Confirm</span>
            </span>
        </el-dialog>

    </div>
</template>

<script>
export default {
    name: "DietHistory",
    data() {
        return {
            dietHistory: {},
            dialogGourmetOperaion: false,
            cookbooks: [],
            dietHistoryList: [],
        }
    },
    created() {
        this.fetchCookbookList();
        this.fetchDietHistoryList();
    },
    methods: {
        removeCookbook(dietHistory) {
            this.$axios.post('/dietHistory/batchDelete', [dietHistory.id]).then(res => {
                if (res.data.code === 200) this.fetchDietHistoryList();
            }).catch(error => { console.log('Error', error); });
        },
        fetchDietHistoryList() {
            this.$axios.post('/dietHistory/queryUserDiet', {}).then(res => {
                if (res.data.code === 200) this.dietHistoryList = res.data.data;
            }).catch(error => { console.log('Error', error); });
        },
        getCookbookName(dietHistory) {
            const cookbook = this.cookbooks.find(e => e.id === dietHistory.cookbookId);
            return cookbook ? cookbook.name : 'Unknown';
        },
        fetchCookbookList() {
            this.$axios.get('/cookbook/querySelectedItemsAll').then(res => {
                if (res.data.code === 200) this.cookbooks = res.data.data;
            }).catch(error => { console.log('Error', error); });
        },
        addOperation() {
            this.$axios.post('/dietHistory/save', this.dietHistory).then(res => {
                if (res.data.code === 200) { this.fetchDietHistoryList(); this.cannel(); }
            }).catch(error => { console.log('Error', error); });
        },
        cannel() { this.dietHistory = {}; this.dialogGourmetOperaion = false; },
        recordGourmet() { this.dialogGourmetOperaion = true; },
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.diet-container {
    max-width: 720px;
    margin: 0 auto;
    padding: 28px 24px 48px;
    font-family: 'DM Sans', sans-serif;
}

/* ─── Page header ────────────────────────────────────── */
.page-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 28px;
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

.add-btn {
    display: inline-flex;
    align-items: center;
    gap: 5px;
    padding: 8px 18px;
    background-color: #c8392b;
    color: #fdf8f2;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.15s;
    white-space: nowrap;

    &:hover { background-color: #b03226; }
}

/* ─── Empty ──────────────────────────────────────────── */
.empty-wrap { margin-top: 40px; }

/* ─── History list ───────────────────────────────────── */
.history-list {
    display: flex;
    flex-direction: column;
    gap: 0;
}

.history-item {
    display: flex;
    align-items: center;
    gap: 14px;
    padding: 14px 10px;
    border-bottom: 1px solid #e8ddd0;
    transition: background-color 0.15s;

    &:last-child { border-bottom: none; }
    &:hover { background-color: rgba(255,255,255,0.55); }
}

.item-left {
    flex-shrink: 0;
}

.item-icon {
    font-size: 22px;
    color: #c8392b;
    opacity: 0.7;
}

.item-body {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.item-name {
    font-family: 'Klee One', cursive;
    font-size: 17px;
    font-weight: 600;
    color: #2a2018;
}

.item-meta {
    display: flex;
    align-items: center;
    gap: 12px;
    font-family: 'DM Sans', sans-serif;
    font-size: 13px;
    color: #8a7d6e;
}

.meta-note {
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.meta-value {
    font-weight: 500;
    color: #5a5045;
    white-space: nowrap;
}

.delete-btn {
    flex-shrink: 0;
    width: 28px;
    height: 28px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    border-radius: 4px;
    color: #b0a898;
    cursor: pointer;
    transition: color 0.15s, background-color 0.15s;
    font-size: 15px;

    &:hover {
        color: #c8392b;
        background-color: rgba(200,57,43,0.08);
    }
}

/* ─── Dialog ─────────────────────────────────────────── */
::v-deep .food-dialog {
    border-radius: 4px;
    border: 1.5px solid #e8ddd0;

    .el-dialog__header { display: none; }
    .el-dialog__body { padding: 0; }
    .el-dialog__footer {
        border-top: 1.5px solid #e8ddd0;
        padding: 14px 20px;
    }
}

.dialog-body {
    padding: 24px 24px 8px;
    background-color: #fdfaf5;
}

.dialog-title {
    font-family: 'Klee One', cursive;
    font-size: 20px;
    font-weight: 600;
    color: #c8392b;
    margin: 0 0 20px 0;
}

.dialog-section {
    margin-bottom: 18px;

    ::v-deep .el-input__inner,
    ::v-deep .el-textarea__inner {
        border-color: #d6c9b8;
        border-radius: 4px;
        font-family: 'DM Sans', sans-serif;
        font-size: 14px;
        color: #3a3028;
        background-color: #ffffff;

        &::placeholder { color: #c0b09e; }
        &:focus { border-color: #c8392b; outline: none; }
    }
}

.dialog-label {
    display: block;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #5a5045;
    margin-bottom: 7px;
}

.dialog-input {
    width: 100%;
    height: 40px;
    padding: 0 12px;
    font-family: 'DM Sans', sans-serif;
    font-size: 14px;
    color: #2a2018;
    background-color: #ffffff;
    border: 1.5px solid #d6c9b8;
    border-radius: 4px;
    transition: border-color 0.2s;

    &::placeholder { color: #c0b09e; }
    &:focus { outline: none; border-color: #c8392b; }
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
}

.cancel-btn {
    display: inline-block;
    padding: 7px 18px;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #8a7d6e;
    border: 1.5px solid #d6c9b8;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.15s;

    &:hover { background-color: #f5f0e8; }
}

.confirm-btn {
    display: inline-block;
    padding: 7px 18px;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #fdf8f2;
    background-color: #c8392b;
    border: 1.5px solid #c8392b;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.15s;

    &:hover { background-color: #b03226; }
}
</style>