<template>
    <div class="mycookbook-container">

        <!-- ── Toolbar ── -->
        <div class="toolbar">
            <span class="add-btn" @click="add()">
                <i class="el-icon-plus"></i> New Cookbook
            </span>
            <div class="filters">
                <el-select
                    style="width: 120px;"
                    @change="fetchFreshData"
                    size="small"
                    v-model="cookbookQueryDto.categoryId"
                    placeholder="Category"
                    clearable
                >
                    <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>

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
                    v-model="cookbookQueryDto.title"
                    placeholder="Search title…"
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
                style="width: 100%; background: transparent;"
                :header-cell-style="headerStyle"
                :cell-style="cellStyle"
                :stripe="false"
            >
                <el-table-column prop="title" label="Cookbook Name" min-width="130" />
                <el-table-column prop="id" sortable label="ID" min-width="68" />
                <el-table-column prop="categoryName" label="Category" min-width="90" />
                <el-table-column prop="isPublish" label="Visibility" min-width="88">
                    <template slot-scope="scope">
                        <span :class="scope.row.isPublish ? 'badge-public' : 'badge-private'">
                            {{ scope.row.isPublish ? 'Public' : 'Private' }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" sortable label="Created At" min-width="135" />
                <el-table-column label="Actions" min-width="110">
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

        <!-- ── Dialog ── -->
        <el-dialog
            :show-close="false"
            :visible.sync="dialogCookbookOperaion"
            width="52%"
            custom-class="food-dialog"
        >
            <div class="dialog-body">
                <h3 class="dialog-title">{{ isOperation ? 'Edit Cookbook' : 'New Cookbook' }}</h3>

                <div class="dialog-section">
                    <label class="dialog-label">Cover Image</label>
                    <el-upload
                        class="cover-uploader"
                        action="http://localhost:21090/api/food-share-sys/v1.0/file/upload"
                        :show-file-list="false"
                        :on-success="handleCoverSuccess"
                    >
                        <img v-if="cover" :src="cover" class="cover-preview" />
                        <div v-else class="cover-placeholder">
                            <i class="el-icon-plus"></i>
                            <span>Upload Cover</span>
                        </div>
                    </el-upload>
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Title</label>
                    <input class="dialog-input" v-model="data.title" placeholder="Enter cookbook title" />
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Content</label>
                    <Editor :receiveContent="data.content" @on-receive="onReceive" />
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Category</label>
                    <el-select size="small" v-model="data.categoryId" placeholder="Select category" clearable style="width: 160px;">
                        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
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
import Editor from "@/components/Editor"
export default {
    components: { Editor },
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
            categories: [],
            dialogCookbookOperaion: false,
            isOperation: false,
            cookbookQueryDto: {},
            cover: null,
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchFreshCategories();
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
        handleCoverSuccess(res) {
            this.$notify({
                duration: 1500,
                title: 'Cover Upload',
                message: res.code === 200 ? 'Upload Success' : 'Upload Failed',
                type: res.code === 200 ? 'success' : 'error'
            });
            if (res.code === 200) this.cover = res.data;
        },
        cannel() {
            this.data = {};
            this.cover = this.cover;
            this.dialogCookbookOperaion = false;
            this.isOperation = false;
        },
        addOperation() {
            this.data.cover = this.cover;
            this.$axios.post('/cookbook/userSave', this.data).then(response => {
                if (response.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'New Cookbook', message: 'Created successfully', type: 'success' });
                    this.cannel();
                    this.fetchFreshData();
                }
            }).catch(error => { console.log('Error:', error); });
        },
        updateOperation() {
            this.data.cover = this.cover;
            this.$axios.put('/cookbook/update', this.data).then(response => {
                if (response.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Update Cookbook', message: 'Saved successfully', type: 'success' });
                    this.cannel();
                    this.fetchFreshData();
                }
            }).catch(error => { console.log('Error:', error); });
        },
        onReceive(content) { this.data.content = content; },
        add() { this.dialogCookbookOperaion = true; },
        fetchFreshCategories() {
            this.$axios.post('/category/query', {}).then(response => {
                if (response.data.code === 200) {
                    this.categories = response.data.data;
                    this.categories.unshift({ id: null, name: 'All' });
                }
            }).catch(error => { console.log('Error:', error); });
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
                    const response = await this.$axios.post('/cookbook/batchDelete', ids);
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
                const params = { current: this.currentPage, size: this.pageSize, key: this.filterText, startTime, endTime, ...this.cookbookQueryDto };
                const response = await this.$axios.post('/cookbook/queryUser', params);
                this.tableData = response.data.data;
                this.totalItems = response.data.total;
            } catch (error) { console.error('Error:', error); }
        },
        handleFilter() { this.currentPage = 1; this.fetchFreshData(); },
        handleFilterClear() { this.filterText = ''; this.handleFilter(); },
        handleSizeChange(val) { this.pageSize = val; this.currentPage = 1; this.fetchFreshData(); },
        handleCurrentChange(val) { this.currentPage = val; this.fetchFreshData(); },
        handleEdit(row) { this.cover = row.cover; this.data = row; this.dialogCookbookOperaion = true; this.isOperation = true; },
        handleDelete(row) { this.delectedRows.push(row); this.batchDelete(); }
    },
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.mycookbook-container {
    font-family: 'DM Sans', sans-serif;
    padding: 4px 0;
    max-width: 1100px;
    margin: 0 auto;
}

/* ─── Toolbar ────────────────────────────────────────── */
.toolbar {
    display: flex;
    align-items: center;
    gap: 12px;
    flex-wrap: wrap;
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

.filters {
    display: flex;
    align-items: center;
    gap: 8px;
    flex-wrap: wrap;

    /* Style Element UI inputs to match theme */
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

    ::v-deep .el-select .el-input__inner { border-radius: 4px; }
    ::v-deep .el-range-editor { border-color: #d6c9b8 !important; border-radius: 4px !important; }
    ::v-deep .el-range-separator { color: #8a7d6e; }
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

        /* Remove default stripe, use custom bottom border */
        .el-table__row { background-color: transparent; }
    }
}

/* Visibility badges */
.badge-public {
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

.badge-private {
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

/* Action buttons in table */
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

/* ─── Pagination ─────────────────────────────────────── */
.pagination {
    padding: 10px 12px;
    text-align: right;
    background-color: transparent;

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
    max-height: 560px;
    overflow-y: auto;
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
    letter-spacing: 0.3px;
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

/* Cover uploader */
.cover-uploader {
    ::v-deep .el-upload {
        border: 1.5px dashed #d6c9b8;
        border-radius: 4px;
        cursor: pointer;
        overflow: hidden;
        transition: border-color 0.15s;
        display: block;

        &:hover { border-color: #c8392b; }
    }
}

.cover-preview {
    width: 200px;
    height: 148px;
    object-fit: cover;
    display: block;
}

.cover-placeholder {
    width: 200px;
    height: 148px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 8px;
    color: #b0a898;
    font-family: 'Klee One', cursive;
    font-size: 13px;

    i { font-size: 26px; }
}

/* Dialog footer buttons */
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