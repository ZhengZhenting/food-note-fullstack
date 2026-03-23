<template>
    <div class="nutriment-page">

        <!-- ── Toolbar ── -->
        <div class="toolbar">
            <el-date-picker
                style="width: 220px;"
                @change="fetchFreshData"
                size="small"
                v-model="searchTime"
                type="daterange"
                range-separator="→"
                start-placeholder="Start"
                end-placeholder="End"
            />
            <el-input
                size="small"
                style="width: 200px;"
                v-model="dietHistoryQueryDto.cookbookId"
                placeholder="Search by cookbook ID…"
                clearable
                @clear="handleFilterClear"
            >
                <el-button slot="append" @click="handleFilter" icon="el-icon-search" />
            </el-input>
        </div>

        <!-- ── Table ── -->
        <div class="table-wrap">
            <el-table
                :data="tableData"
                style="width: 100%;"
                :header-cell-style="headerStyle"
                :cell-style="cellStyle"
            >
                <el-table-column prop="cookbookCover" width="88" label="Cover">
                    <template slot-scope="scope">
                        <img :src="scope.row.cookbookCover" class="cover-thumb" />
                    </template>
                </el-table-column>
                <el-table-column prop="cookbookTitle" label="Cookbook" min-width="140" />
                <el-table-column prop="detail" label="Notes" min-width="150" />
                <el-table-column prop="value" label="Value / g" width="120" />
                <el-table-column prop="createTime" label="Time" width="170" />
                <el-table-column label="Actions" width="110">
                    <template slot-scope="scope">
                        <span class="text-btn edit" @click="handleEdit(scope.row)">Edit</span>
                        <span class="text-btn delete" @click="handleDelete(scope.row)">Delete</span>
                    </template>
                </el-table-column>
            </el-table>

            <el-pagination
                class="pagination"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[20, 50]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"
            />
        </div>

        <!-- ── Nutriment chart section ── -->
        <div class="chart-section">
            <div class="chart-header">
                <h3 class="chart-title">Nutriment Intake</h3>
                <div class="chart-filter">
                    <label class="filter-label">Nutriment</label>
                    <el-select
                        style="width: 200px;"
                        @change="loadNutrimentData"
                        size="small"
                        v-model="dietQueryDto.nutrimentId"
                        placeholder="Select nutriment"
                        clearable
                    >
                        <el-option v-for="item in nutrimentItems" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </div>
            </div>
            <div class="chart-wrap">
                <LineChart tag="Nutriment Intake" @on-selected="onSelected" :values="values" :date="dates" />
            </div>
        </div>

        <!-- ── Edit dialog ── -->
        <el-dialog
            :show-close="false"
            :visible.sync="dialogDietOperaion"
            width="36%"
            custom-class="food-dialog"
        >
            <div class="dialog-body">
                <h3 class="dialog-title">Edit Diet Record</h3>

                <div class="dialog-section">
                    <label class="dialog-label">Choose Cookbook</label>
                    <el-select style="width: 100%;" size="small" v-model="data.cookbookId" placeholder="Select a cookbook">
                        <el-option v-for="item in cookbooks" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Value / g</label>
                    <input class="dialog-input" v-model="data.value" placeholder="Enter grams" />
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Notes</label>
                    <el-input type="textarea" :rows="3" placeholder="Add notes (optional)" v-model="data.detail" />
                </div>
            </div>

            <span slot="footer" class="dialog-footer">
                <span class="cancel-btn" @click="cannel()">Cancel</span>
                <span class="confirm-btn" @click="updateOperation()">Save Changes</span>
            </span>
        </el-dialog>

    </div>
</template>

<script>
import LineChart from "@/components/LineChart"
export default {
    components: { LineChart },
    data() {
        return {
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 20,
            totalItems: 0,
            dialogDietOperaion: false,
            tableData: [],
            delectedRows: [],
            dietHistoryQueryDto: {},
            cookbooks: [],
            dietHistory: {},
            searchTime: [],
            dietQueryDto: { day: 365 },
            nutrimentUseList: [],
            values: [],
            dates: [],
            nutrimentItems: []
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchCookbookList();
        this.loadNutrimentData();
        this.fetchNutrimentListItem();
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
        async fetchNutrimentListItem() {
            try {
                const response = await this.$axios.get('/nutriment/querySelectedItems');
                if (response.data.code === 200) {
                    this.nutrimentItems = response.data.data;
                    this.nutrimentItems.unshift({ id: null, name: 'All' });
                }
            } catch (error) { console.log(error); }
        },
        onSelected(day) { this.dietQueryDto.day = day; this.loadNutrimentData(); },
        loadNutrimentData() {
            this.$axios.post('/dietHistory/queryDietNutrimentInfo', this.dietQueryDto).then(res => {
                if (res.data.code === 200) {
                    this.nutrimentUseList = res.data.data;
                    this.dates = this.nutrimentUseList.map(e => e.time);
                    this.values = this.nutrimentUseList.map(e => e.value);
                }
            }).catch(error => { console.log('Error', error); });
        },
        fetchCookbookList() {
            this.$axios.get('/cookbook/querySelectedItemsAll').then(res => {
                if (res.data.code === 200) this.cookbooks = res.data.data;
            }).catch(error => { console.log('Error', error); });
        },
        cannel() { this.data = {}; this.dialogDietOperaion = false; this.isOperation = false; },
        async batchDelete() {
            if (!this.delectedRows.length) { this.$message('No data selected'); return; }
            const confirmed = await this.$swalConfirm({ title: 'Delete', text: 'Unrecoverable, continue?', icon: 'warning' });
            if (confirmed) {
                try {
                    const ids = this.delectedRows.map(e => e.id);
                    const response = await this.$axios.post('/dietHistory/batchDelete', ids);
                    if (response.data.code === 200) {
                        this.$notify({ duration: 1000, title: 'Delete', message: 'Deleted successfully', type: 'success' });
                        this.fetchFreshData();
                    }
                } catch (error) { console.error('Error:', error); }
            }
        },
        async updateOperation() {
            try {
                const response = await this.$axios.put('/dietHistory/update', this.data);
                if (response.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Edit', message: 'Updated successfully', type: 'success' });
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) { console.error('Error:', error); }
        },
        async addOperation() {
            try {
                const response = await this.$axios.post('/dietHistory/save', this.data);
                if (response.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Add', message: 'Added successfully', type: 'success' });
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) { console.error('Error:', error); }
        },
        async fetchFreshData() {
            try {
                let startTime = null, endTime = null;
                if (this.searchTime && this.searchTime.length === 2) {
                    const [s, e] = await Promise.all(this.searchTime.map(d => d.toISOString()));
                    startTime = `${s.split('T')[0]}T00:00:00`;
                    endTime = `${e.split('T')[0]}T23:59:59`;
                }
                const params = { current: this.currentPage, size: this.pageSize, key: this.filterText, startTime, endTime, ...this.dietHistoryQueryDto };
                const response = await this.$axios.post('/dietHistory/queryUser', params);
                this.tableData = response.data.data;
                this.totalItems = response.data.total;
            } catch (error) { console.error('Error:', error); }
        },
        handleFilter() { this.currentPage = 1; this.fetchFreshData(); },
        handleFilterClear() { this.filterText = ''; this.handleFilter(); },
        handleSizeChange(val) { this.pageSize = val; this.currentPage = 1; this.fetchFreshData(); },
        handleCurrentChange(val) { this.currentPage = val; this.fetchFreshData(); },
        handleEdit(row) {
            this.dialogDietOperaion = true;
            this.data = { id: row.id, cookbookId: row.cookbookId, value: row.value, detail: row.detail };
        },
        handleDelete(row) { this.delectedRows.push(row); this.batchDelete(); }
    },
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.nutriment-page {
    font-family: 'DM Sans', sans-serif;
    padding: 4px 0;
    display: flex;
    flex-direction: column;
    gap: 24px;
}

/* ─── Toolbar ────────────────────────────────────────── */
.toolbar {
    display: flex;
    align-items: center;
    gap: 10px;
    flex-wrap: wrap;

    ::v-deep .el-input__inner {
        border-color: #d6c9b8;
        border-radius: 4px;
        font-family: 'DM Sans', sans-serif;
        font-size: 13px;
        color: #3a3028;
        background-color: rgba(255,255,255,0.7);

        &::placeholder { color: #c0b09e; }
        &:focus { border-color: #c8392b; }
    }

    ::v-deep .el-input-group__append {
        background-color: #c8392b;
        border-color: #c8392b;
        color: #fdf8f2;
        transition: background-color 0.15s;
        &:hover { background-color: #b03226; }
    }

    ::v-deep .el-range-editor {
        border-color: #d6c9b8 !important;
        border-radius: 4px !important;
        background-color: rgba(255,255,255,0.7);
    }

    ::v-deep .el-range-separator { color: #8a7d6e; }

    ::v-deep .el-range-input {
        font-family: 'DM Sans', sans-serif;
        font-size: 13px;
        color: #3a3028;
        background: transparent;
        &::placeholder { color: #c0b09e; }
    }
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

.cover-thumb {
    width: 38px;
    height: 38px;
    object-fit: cover;
    border-radius: 4px;
    display: block;
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

.pagination {
    padding: 10px 12px;
    text-align: right;

    ::v-deep .el-pager li.active { color: #c8392b; }
    ::v-deep .el-pager li:hover  { color: #c8392b; }
}

/* ─── Chart section ──────────────────────────────────── */
.chart-section {
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    background-color: rgba(255,255,255,0.55);
    padding: 20px 20px 16px;
}

.chart-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;
    flex-wrap: wrap;
    gap: 12px;
}

.chart-title {
    font-family: 'Caveat', cursive;
    font-weight: 700;
    font-size: 28px;
    color: #c8392b;
    margin: 0;
    transform: rotate(-0.6deg);
    display: inline-block;
    line-height: 1;
}

.chart-filter {
    display: flex;
    align-items: center;
    gap: 10px;

    ::v-deep .el-input__inner {
        border-color: #d6c9b8;
        border-radius: 4px;
        font-family: 'DM Sans', sans-serif;
        font-size: 13px;
        color: #3a3028;
        background-color: rgba(255,255,255,0.7);

        &:focus { border-color: #c8392b; }
    }
}

.filter-label {
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #5a5045;
    white-space: nowrap;
}

.chart-wrap {
    width: 100%;
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