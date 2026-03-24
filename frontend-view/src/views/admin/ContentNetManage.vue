<template>
    <div class="contentnet-container">

        <!-- ── Page header ── -->
        <div class="page-header">
            <div class="header-left">
                <h2 class="page-title">Content Sharing</h2>
                <p class="page-subtitle">{{ totalItems }} records found</p>
            </div>
            <div class="header-right">
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
                    style="width: 250px;"
                    v-model="contentNetQueryDto.accessCode"
                    placeholder="Search access code…"
                    clearable
                    @clear="handleFilterClear"
                >
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search" />
                </el-input>
            </div>
        </div>

        <!-- ── Table ── -->
        <div class="table-wrap">
            <el-table
                :data="tableData"
                style="width: 100%;"
                :header-cell-style="headerStyle"
                :cell-style="cellStyle"
            >
                <el-table-column prop="gourmetTitle" label="Content" min-width="120" />
                <el-table-column prop="gourmetId" sortable label="Gourmet ID" min-width="110" />
                <el-table-column prop="userId" sortable label="User ID" min-width="90" />
                <el-table-column prop="userName" label="User Name" min-width="110" />
                <el-table-column prop="accessCode" label="Access Code" min-width="120" />
                <el-table-column prop="accessPassword" label="Password" min-width="110" />
                <el-table-column prop="validDay" sortable label="Valid Days" min-width="110" />
                <el-table-column prop="passwordAuth" label="Pwd Auth" min-width="110">
                    <template slot-scope="scope">
                        <span :class="scope.row.passwordAuth ? 'badge-yes' : 'badge-no'">
                            {{ scope.row.passwordAuth ? 'Yes' : 'No' }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" sortable label="Created At" min-width="130" />
                <el-table-column label="Actions" width="100">
                    <template slot-scope="scope">
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
            tableData: [],
            delectedRows: [],
            searchTime: [],
            contentNetQueryDto: {},
        };
    },
    created() {
        this.fetchFreshData();
    },
    methods: {
        headerStyle() {
            return {
                fontFamily: "'Klee One', cursive",
                fontSize: '12px',
                fontWeight: '600',
                color: '#8a7d6e',
                backgroundColor: '#f5f0e8',
                borderBottom: '1.5px solid #e8ddd0',
                textTransform: 'uppercase',
                letterSpacing: '0.5px',
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
        async batchDelete() {
            if (!this.delectedRows.length) { this.$message('No data selected'); return; }
            const confirmed = await this.$swalConfirm({ title: 'Delete Confirmation', text: 'Content unrecoverable, continue?', icon: 'warning' });
            if (confirmed) {
                try {
                    const ids = this.delectedRows.map(e => e.id);
                    const response = await this.$axios.post('/contentNet/batchDelete', ids);
                    if (response.data.code === 200) {
                        this.$notify({ duration: 1000, title: 'Delete', message: 'Deleted successfully', type: 'success' });
                        this.fetchFreshData();
                    }
                } catch (error) { console.error('Error:', error); }
            }
        },
        async fetchFreshData() {
            try {
                this.tableData = [];
                let startTime = null, endTime = null;
                if (this.searchTime != null && this.searchTime.length === 2) {
                    const [startDate, endDate] = await Promise.all(this.searchTime.map(date => date.toISOString()));
                    startTime = `${startDate.split('T')[0]}T00:00:00`;
                    endTime   = `${endDate.split('T')[0]}T23:59:59`;
                }
                const params = { current: this.currentPage, size: this.pageSize, key: this.filterText, startTime, endTime, ...this.contentNetQueryDto };
                const response = await this.$axios.post('/contentNet/query', params);
                this.tableData  = response.data.data;
                this.totalItems = response.data.total;
            } catch (error) { console.error('Error:', error); }
        },
        handleFilter()      { this.currentPage = 1; this.fetchFreshData(); },
        handleFilterClear() { this.filterText = ''; this.handleFilter(); },
        handleSizeChange(val)    { this.pageSize = val; this.currentPage = 1; this.fetchFreshData(); },
        handleCurrentChange(val) { this.currentPage = val; this.fetchFreshData(); },
        handleDelete(row)        { this.delectedRows.push(row); this.batchDelete(); }
    },
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.contentnet-container {
    padding: 28px 28px 48px;
    font-family: 'DM Sans', sans-serif;
    min-height: 100%;
}

/* ─── Page header ────────────────────────────────────── */
.page-header {
    display: flex;
    align-items: flex-end;
    justify-content: space-between;
    margin-bottom: 24px;
    gap: 16px;
    flex-wrap: wrap;
}

.header-left { display: flex; flex-direction: column; gap: 2px; }

.page-title {
    font-family: 'Caveat', cursive;
    font-weight: 700;
    font-size: 38px;
    color:  #c8392b;
    margin: 0;
    line-height: 1;
    transform: rotate(-0.5deg);
    display: inline-block;
}

.page-subtitle {
    font-family: 'DM Sans', sans-serif;
    font-size: 13px;
    color: #b0a898;
    margin: 0;
}

.header-right {
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
        background-color: rgba(255,255,255,0.8);

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
        background-color: rgba(255,255,255,0.8);
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
    background-color: rgba(255,255,255,0.7);
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    overflow: hidden;

    ::v-deep .el-table {
        background-color: transparent;
        tr:hover > td { background-color: rgba(200,57,43,0.03) !important; }
        .el-table__row { background-color: transparent; }
    }
}

.badge-yes {
    display: inline-block;
    padding: 2px 10px;
    border-radius: 20px;
    font-size: 11px;
    font-family: 'Klee One', cursive;
    font-weight: 600;
    background-color: rgba(200,57,43,0.08);
    color: #c8392b;
    border: 1px solid rgba(200,57,43,0.2);
}

.badge-no {
    display: inline-block;
    padding: 2px 10px;
    border-radius: 20px;
    font-size: 11px;
    font-family: 'Klee One', cursive;
    font-weight: 600;
    background-color: rgba(90,80,69,0.07);
    color: #8a7d6e;
    border: 1px solid #e8ddd0;
}

.text-btn {
    font-family: 'Klee One', cursive;
    font-size: 12px;
    font-weight: 600;
    cursor: pointer;
    padding: 3px 8px;
    border-radius: 3px;
    transition: background-color 0.15s;

    &.delete {
        color: #8a7d6e;
        &:hover { background-color: rgba(90,80,69,0.08); color: #5a5045; }
    }
}

.pagination {
    padding: 10px 14px;
    text-align: right;

    ::v-deep .el-pager li.active { color: #c8392b; }
    ::v-deep .el-pager li:hover  { color: #c8392b; }
}
</style>