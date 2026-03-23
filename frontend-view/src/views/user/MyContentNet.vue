<template>
    <div class="contentnet-container">

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
                style="width: 220px;"
                v-model="contentNetQueryDto.accessCode"
                placeholder="Search access code…"
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
                <el-table-column prop="gourmetTitle" label="Content" min-width="140" />
                <el-table-column prop="gourmetId" sortable label="Gourmet ID" min-width="110" />
                <el-table-column prop="accessCode" label="Access Code" min-width="110" />
                <el-table-column prop="accessPassword" label="Password" min-width="110" />
                <el-table-column prop="validDay" sortable label="Valid Days" min-width="100" />
                <el-table-column prop="passwordAuth" label="Pwd Auth" min-width="90">
                    <template slot-scope="scope">
                        <span :class="scope.row.passwordAuth ? 'badge-yes' : 'badge-no'">
                            {{ scope.row.passwordAuth ? 'Yes' : 'No' }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" sortable label="Created At" min-width="130" />
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

        <!-- ── Edit dialog ── -->
        <el-dialog
            :visible.sync="dialogShareOperaion"
            :show-close="false"
            width="36%"
            custom-class="food-dialog"
        >
            <div class="dialog-body">
                <h3 class="dialog-title">Edit Content Sharing</h3>

                <div class="dialog-section">
                    <label class="dialog-label">Valid Days</label>
                    <div class="radio-group">
                        <span
                            v-for="opt in validDayOptions"
                            :key="opt"
                            class="radio-btn"
                            :class="{ active: contentNet.validDay === opt }"
                            @click="contentNet.validDay = opt"
                        >
                            {{ opt }}
                        </span>
                    </div>
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Password Authentication</label>
                    <div class="toggle-wrap">
                        <el-switch
                            v-model="contentNet.passwordAuth"
                            active-color="#c8392b"
                            inactive-color="#d6c9b8"
                        />
                        <span class="toggle-label">{{ contentNet.passwordAuth ? 'Enabled' : 'Disabled' }}</span>
                    </div>
                </div>

                <div class="dialog-section" v-if="contentNet.passwordAuth">
                    <label class="dialog-label">Set Password</label>
                    <input
                        class="dialog-input"
                        type="password"
                        v-model="contentNet.accessPassword"
                        placeholder="Enter password"
                    />
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
export default {
    data() {
        return {
            dialogShareOperaion: false,
            contentNet: {},
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 20,
            totalItems: 0,
            tableData: [],
            delectedRows: [],
            searchTime: [],
            contentNetQueryDto: {},
            validDayOptions: ['3 Days', '7 Days', '30 Days', 'Always'],
        };
    },
    created() {
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
        updateOperation() {
            const dayList = [this.contentNet.validDay];
            const validDayList = dayList.map(text => {
                const match = text.match(/\d+/g);
                return match ? match : [];
            });
            if (this.contentNet.passwordAuth) {
                this.contentNet.accessPassword = this.$md5(this.contentNet.accessPassword);
            }
            const updateEntity = {
                id: this.contentNet.id,
                validDay: validDayList[0].length === 0 ? -1 : validDayList[0][0],
                passwordAuth: this.contentNet.passwordAuth,
                accessPassword: this.contentNet.accessPassword
            };
            this.$axios.put('/contentNet/update', updateEntity).then(res => {
                if (res.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Edit Operation', message: 'success', type: 'success' });
                    this.cannel();
                    this.fetchFreshData();
                }
            }).catch(error => { console.log('Error', error); });
        },
        cannel() {
            this.dialogShareOperaion = false;
            this.contentNet = {};
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
                if (this.searchTime && this.searchTime.length === 2) {
                    const [s, e] = await Promise.all(this.searchTime.map(d => d.toISOString()));
                    startTime = `${s.split('T')[0]}T00:00:00`;
                    endTime = `${e.split('T')[0]}T23:59:59`;
                }
                const params = { current: this.currentPage, size: this.pageSize, key: this.filterText, startTime, endTime, ...this.contentNetQueryDto };
                const response = await this.$axios.post('/contentNet/queryUser', params);
                this.tableData = response.data.data;
                this.totalItems = response.data.total;
            } catch (error) { console.error('Error:', error); }
        },
        handleFilter() { this.currentPage = 1; this.fetchFreshData(); },
        handleFilterClear() { this.filterText = ''; this.handleFilter(); },
        handleSizeChange(val) { this.pageSize = val; this.currentPage = 1; this.fetchFreshData(); },
        handleCurrentChange(val) { this.currentPage = val; this.fetchFreshData(); },
        handleEdit(row) {
            this.contentNet = { ...row };
            this.contentNet.validDay = row.validDay === -1 ? 'Always' : row.validDay + ' Days';
            this.dialogShareOperaion = true;
        },
        handleDelete(row) { this.delectedRows.push(row); this.batchDelete(); }
    },
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.contentnet-container {
    font-family: 'DM Sans', sans-serif;
    padding: 4px 0;
}

/* ─── Toolbar ────────────────────────────────────────── */
.toolbar {
    display: flex;
    align-items: center;
    gap: 10px;
    flex-wrap: wrap;
    margin-bottom: 16px;

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
    margin-bottom: 8px;
}

/* Custom radio group replacing el-radio-button */
.radio-group {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
}

.radio-btn {
    display: inline-block;
    padding: 5px 14px;
    border: 1.5px solid #d6c9b8;
    border-radius: 4px;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #5a5045;
    cursor: pointer;
    transition: border-color 0.15s, color 0.15s, background-color 0.15s;

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

.toggle-wrap {
    display: flex;
    align-items: center;
    gap: 10px;
}

.toggle-label {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #5a5045;
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