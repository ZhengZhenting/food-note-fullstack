<template>
  <div class="cookbook-container">

    <!-- ── Page header ── -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">Cookbook Nutriment</h2>
        <p class="page-subtitle">{{ totalItems }} records found</p>
      </div>

      <div class="header-right">
        <el-input
          size="small"
          style="width: 180px;"
          v-model="cookbookNutrimentQueryDto.name"
          placeholder="Search…"
          clearable
          @clear="handleFilterClear"
        >
          <el-button slot="append" @click="handleFilter" icon="el-icon-search" />
        </el-input>

        <el-select
          style="width: 160px;"
          @change="fetchFreshData"
          size="small"
          v-model="cookbookNutrimentQueryDto.cookbookId"
          placeholder="Cookbook"
          clearable
        >
          <el-option v-for="item in cookbookItems" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>

        <el-select
          style="width: 160px;"
          @change="fetchFreshData"
          size="small"
          v-model="cookbookNutrimentQueryDto.nutrimentId"
          placeholder="Nutriment"
          clearable
        >
          <el-option v-for="item in nutrimentItems" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>

        <span class="add-btn" @click="add()">
          <i class="el-icon-plus"></i> Add Nutriment
        </span>
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
        <el-table-column prop="cookbookId" sortable label="Cookbook ID" min-width="100" />
        <el-table-column prop="cookbookName" label="Cookbook" min-width="140" />
        <el-table-column prop="nutrimentId" sortable label="Nutriment ID" min-width="110" />
        <el-table-column prop="nutrimentName" label="Nutriment Name" min-width="140" />
        <el-table-column prop="nutrimentUnit" label="Unit" min-width="90" />
        <el-table-column prop="g100Value" label="/100 Unit" min-width="100" />

        <el-table-column label="Actions" width="130">
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
      :visible.sync="dialogCategoryOperaion"
      width="40%"
      custom-class="food-dialog"
    >
      <div class="dialog-body">
        <h3 class="dialog-title">
          {{ isOperation ? 'Edit Nutriment' : 'Add Nutriment' }}
        </h3>

        <div class="dialog-section">
          <label class="dialog-label">Choose Cookbook</label>
          <el-select
            size="small"
            v-model="data.cookbookId"
            placeholder="Select cookbook"
            clearable
            style="width: 220px;"
          >
            <el-option v-for="item in cookbookItems" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </div>

        <div class="dialog-section">
          <label class="dialog-label">Choose Nutriment</label>
          <el-select
            size="small"
            v-model="data.nutrimentId"
            placeholder="Select nutriment"
            clearable
            style="width: 220px;"
          >
            <el-option v-for="item in nutrimentItems" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </div>

        <div class="dialog-section">
          <label class="dialog-label">/100 Unit</label>
          <input
            class="dialog-input"
            v-model="data.g100Value"
            placeholder="Enter value"
          />
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <span class="cancel-btn" @click="cannel()">Cancel</span>
        <span v-if="!isOperation" class="confirm-btn" @click="addOperation()">Create</span>
        <span v-else class="confirm-btn" @click="updateOperation()">Save</span>
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
        fontSize: '12px',
        fontWeight: '600',
        color: '#8a7d6e',
        backgroundColor: '#f5f0e8',
        borderBottom: '1.5px solid #e8ddd0',
        textTransform: 'uppercase',
      };
    },

    cellStyle() {
      return {
        fontFamily: "'DM Sans', sans-serif",
        fontSize: '13px',
        color: '#3a3028',
        borderBottom: '1px solid #f0ebe0',
      };
    },

    async fetchCookBookListItem() {
      const res = await this.$axios.get(`/cookbook/querySelectedItems`);
      if (res.data.code === 200) {
        this.cookbookItems = res.data.data;
        this.cookbookItems.unshift({ id: null, name: 'All' });
      }
    },

    async fetchNutrimentListItem() {
      const res = await this.$axios.get(`/nutriment/querySelectedItems`);
      if (res.data.code === 200) {
        this.nutrimentItems = res.data.data;
        this.nutrimentItems.unshift({ id: null, name: 'All' });
      }
    },

    cannel() {
      this.data = {};
      this.dialogCategoryOperaion = false;
      this.isOperation = false;
    },

    async batchDelete() {
      if (!this.delectedRows.length) return;

      const confirmed = await this.$swalConfirm({
        title: 'Delete',
        text: 'This action cannot be undone',
        icon: 'warning',
      });

      if (confirmed) {
        const ids = this.delectedRows.map(e => e.id);
        const res = await this.$axios.post(`/cookbookNutriment/batchDelete`, ids);
        if (res.data.code === 200) {
          this.fetchFreshData();
        }
      }
    },

    async updateOperation() {
      const res = await this.$axios.put('/cookbookNutriment/update', this.data);
      if (res.data.code === 200) {
        this.cannel();
        this.fetchFreshData();
      }
    },

    async addOperation() {
      const res = await this.$axios.post('/cookbookNutriment/save', this.data);
      if (res.data.code === 200) {
        this.cannel();
        this.fetchFreshData();
      }
    },

    async fetchFreshData() {
      const params = {
        current: this.currentPage,
        size: this.pageSize,
        key: this.filterText,
        ...this.cookbookNutrimentQueryDto
      };

      const res = await this.$axios.post('/cookbookNutriment/query', params);
      this.tableData = res.data.data;
      this.totalItems = res.data.total;
    },

    add() {
      this.dialogCategoryOperaion = true;
    },

    handleFilter() {
      this.currentPage = 1;
      this.fetchFreshData();
    },

    handleFilterClear() {
      this.filterText = '';
      this.handleFilter();
    },

    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
      this.fetchFreshData();
    },

    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchFreshData();
    },

    handleEdit(row) {
      this.dialogCategoryOperaion = true;
      this.isOperation = true;
      this.data = { ...row };
    },

    handleDelete(row) {
      this.delectedRows.push(row);
      this.batchDelete();
    }
  }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.cookbook-container {
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
    color: #c8392b;
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
    gap: 8px;
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

    ::v-deep .el-select .el-input__inner { border-radius: 4px; }

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
    white-space: nowrap;
    transition: background-color 0.15s;

    &:hover { background-color: #b03226; }
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
    padding: 10px 14px;
    text-align: right;

    ::v-deep .el-pager li.active { color: #c8392b; }
    ::v-deep .el-pager li:hover  { color: #c8392b; }
}

/* ─── Dialog ─────────────────────────────────────────── */
::v-deep .food-dialog {
    border-radius: 4px;
    border: 1.5px solid #e8ddd0;

    .el-dialog__header { display: none; }
    .el-dialog__body   { padding: 0; }
    .el-dialog__footer { border-top: 1.5px solid #e8ddd0; padding: 14px 20px; }
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

.dialog-section { margin-bottom: 18px; }

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

.editor-wrap {
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    overflow: hidden;
}

.dialog-section {
    ::v-deep .el-input__inner {
        border-color: #d6c9b8;
        border-radius: 4px;
        font-family: 'DM Sans', sans-serif;
        font-size: 13px;
        color: #3a3028;
        background-color: #ffffff;
        &:focus { border-color: #c8392b; }
    }
}

.dialog-footer { display: flex; justify-content: flex-end; gap: 10px; }

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