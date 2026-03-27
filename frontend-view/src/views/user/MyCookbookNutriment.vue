<template>
    <div class="nutriment-container">

        <!-- ── Toolbar ── -->
        <div class="toolbar">
            <span class="add-btn" @click="add()">
                <i class="el-icon-plus"></i> Add Cookbook Nutriment
            </span>
        </div>

        <!-- ── Table ── -->
        <div class="table-wrap">
            <el-table
                :data="tableData"
                style="width: 100%;"
                :header-cell-style="headerStyle"
                :cell-style="cellStyle"
            >
                <el-table-column prop="cookbookId" sortable label="Cookbook ID" min-width="110" />
                <el-table-column prop="cookbookName" label="Cookbook" min-width="130" />
                <el-table-column prop="nutrimentId" label="Nutriment ID" min-width="110" />
                <el-table-column prop="nutrimentName" label="Nutriment Name" min-width="130" />
                <el-table-column prop="g100Value" label="/ 100 Unit" min-width="100" />
                <el-table-column prop="nutrimentUnit" label="Unit" min-width="80" />
                <el-table-column label="Actions" width="130">
                    <template slot-scope="scope">
                        <span class="text-btn edit" @click="handleEdit(scope.row)">Edit</span>
                        <span class="text-btn delete" @click="handleDelete(scope.row)">Delete</span>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- ── Dialog ── -->
        <el-dialog
            :show-close="false"
            :visible.sync="dialogCategoryOperaion"
            width="42%"
            custom-class="food-dialog"
        >
            <div class="dialog-body">
                <h3 class="dialog-title">{{ isOperation ? 'Edit Nutriment' : 'Add Nutriment' }}</h3>

                <div class="dialog-section">
                    <label class="dialog-label">Choose Cookbook</label>
                    <el-select
                        style="width: 100%;"
                        size="small"
                        v-model="data.cookbookId"
                        placeholder="Select a cookbook"
                        clearable
                    >
                        <el-option v-for="item in cookbookItems" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Choose Nutriment</label>
                    <el-select
                        style="width: 100%;"
                        size="small"
                        v-model="data.nutrimentId"
                        placeholder="Select a nutriment"
                        clearable
                    >
                        <el-option v-for="item in nutrimentItems" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Value per 100 Units</label>
                    <input class="dialog-input" v-model="data.g100Value" placeholder="Enter value" />
                </div>
            </div>

            <span slot="footer" class="dialog-footer">
                <span class="cancel-btn" @click="cannel()">Cancel</span>
                <span v-if="!isOperation" class="confirm-btn" @click="addOperation()">Create</span>
                <span v-else class="confirm-btn" @click="updateOperation()">Save Changes</span>
            </span>
        </el-dialog>

    </div>
</template>

<script>
export default {
    data() {
        return {
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 20,
            totalItems: 0,
            dialogCategoryOperaion: false,
            isOperation: false,
            tableData: [],
            delectedRows: [],
            cookbookNutrimentQueryDto: {},
            cookbookItems: [],
            nutrimentItems: [],
        };
    },
    created() {
        this.fetchCookBookListItem();
        this.fetchNutrimentListItem();
        this.fetchFreshData();
    },
    methods: {
        headerStyle() {
            return {
                fontFamily: "'Klee One', cursive",
                fontSize: '13px',
                fontWeight: '600',
                color: '#5a5045',
                backgroundColor: '#f5f0e8',
                borderBottom: '1.5px solid #e8ddd0',
            };
        },
        cellStyle() {
            return {
                fontFamily: "'DM Sans', sans-serif",
                fontSize: '13px',
                color: '#3a3028',
                backgroundColor: 'transparent',
                borderBottom: '1px solid #f0ebe0',
            };
        },
        async fetchCookBookListItem() {
            try {
                const response = await this.$axios.get('/cookbook/querySelectedItemsUser');
                if (response.data.code === 200) this.cookbookItems = response.data.data;
            } catch (error) { console.log(error); }
        },
        async fetchNutrimentListItem() {
            try {
                const response = await this.$axios.get('/nutriment/querySelectedItems');
                if (response.data.code === 200) {
                    this.nutrimentItems = response.data.data;
                    this.nutrimentItems.unshift({ id: null, name: 'All' });
                }
            } catch (error) { console.log(error); }
        },
        cannel() {
            this.data = {};
            this.dialogCategoryOperaion = false;
            this.isOperation = false;
        },
        async batchDelete() {
            if (!this.delectedRows.length) { this.$message('未选中任何数据'); return; }
            const confirmed = await this.$swalConfirm({
                title: 'Delete Confirmation',
                text: 'Content unrecoverable, continue?',
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    const ids = this.delectedRows.map(e => e.id);
                    const response = await this.$axios.post('/cookbookNutriment/batchDelete', ids);
                    if (response.data.code === 200) {
                        this.$notify({ duration: 1000, title: 'Delete', message: 'Deleted successfully', type: 'success' });
                        this.fetchFreshData();
                    }
                } catch (error) { console.error('Error:', error); }
            }
        },
        async updateOperation() {
            try {
                const response = await this.$axios.put('/cookbookNutriment/update', this.data);
                if (response.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Edit', message: 'Updated successfully', type: 'success' });
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) { console.error('Error:', error); }
        },
        async addOperation() {
            try {
                const response = await this.$axios.post('/cookbookNutriment/save', this.data);
                if (response.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Add', message: 'Added successfully', type: 'success' });
                    this.cannel();
                    this.fetchFreshData();
                } else {
                    this.$notify({ duration: 1000, title: 'Add', message: response.data.msg, type: 'error' });
                }
            } catch (error) { console.error('Error:', error); this.$message.error('Error, please try later!'); }
        },
        async fetchFreshData() {
            try {
                const response = await this.$axios.post('/cookbookNutriment/queryUser');
                this.tableData = response.data.data;
            } catch (error) { console.error('Error:', error); }
        },
        add() { this.dialogCategoryOperaion = true; },
        handleFilter() { this.currentPage = 1; this.fetchFreshData(); },
        handleFilterClear() { this.filterText = ''; this.handleFilter(); },
        handleSizeChange(val) { this.pageSize = val; this.currentPage = 1; this.fetchFreshData(); },
        handleCurrentChange(val) { this.currentPage = val; this.fetchFreshData(); },
        handleEdit(row) { this.dialogCategoryOperaion = true; this.isOperation = true; this.data = { ...row }; },
        handleDelete(row) { this.delectedRows.push(row); this.batchDelete(); }
    },
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.nutriment-container {
    font-family: 'DM Sans', sans-serif;
    padding: 4px 0;
    max-width: 1100px;
    margin: 0 auto;
}

/* ─── Toolbar ────────────────────────────────────────── */
.toolbar {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
    margin-top: 16px;
}

.add-btn {
    display: inline-flex;
    align-items: center;
    gap: 5px;
    padding: 7px 16px;
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

/* ─── Table ──────────────────────────────────────────── */
.table-wrap {
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    overflow: hidden;
    background-color: rgba(255,255,255,0.55);

    ::v-deep .el-table {
        background-color: transparent;
        tr:hover > td { background-color: rgba(200,57,43,0.03) !important; }
        .el-table__row { background-color: transparent; }
    }
}

.text-btn {
    font-family: 'Klee One', cursive;
    font-size: 12px;
    font-weight: 600;
    cursor: pointer;
    padding: 3px 8px;
    border-radius: 3px;
    margin-right: 4px;
    transition: background-color 0.15s;

    &.edit {
        color: #c8392b;
        &:hover { background-color: rgba(200,57,43,0.08); }
    }

    &.delete {
        color: #8a7d6e;
        &:hover { background-color: rgba(90,80,69,0.08); color: #5a5045; }
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
}

.dialog-label {
    display: block;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #5a5045;
    margin-bottom: 7px;
}

/* Style el-select inside dialog */
.dialog-section {
    ::v-deep .el-input__inner {
        border-color: #d6c9b8;
        border-radius: 4px;
        font-family: 'DM Sans', sans-serif;
        font-size: 14px;
        color: #3a3028;
        background-color: #ffffff;

        &::placeholder { color: #c0b09e; }
        &:focus { border-color: #c8392b; }
    }
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
    box-sizing: border-box;
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