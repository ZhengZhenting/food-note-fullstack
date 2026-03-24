<template>
  <div class="cookbook-container">

    <!-- ── Page header ── -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">Diet History</h2>
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
          style="width: 220px;"
          v-model="dietHistoryQueryDto.userId"
          placeholder="Search User ID…"
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
        <el-table-column prop="cookbookId" label="Cookbook ID" min-width="110" />
        <el-table-column prop="cookbookTitle" label="Cookbook Name" min-width="160" />
        <el-table-column prop="value" label="Value / g" min-width="100" />
        <el-table-column prop="userName" label="User Name" min-width="120" />
        <el-table-column prop="userId" label="User ID" min-width="100" />
        <el-table-column prop="detail" label="Notes" min-width="160" />
        <el-table-column prop="createTime" label="Time" min-width="160" />

        <el-table-column label="Actions" width="110">
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
      currentPage: 1,
      pageSize: 20,
      totalItems: 0,
      tableData: [],
      delectedRows: [],
      searchTime: [],
      dietHistoryQueryDto: [],
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

    async batchDelete() {
      if (!this.delectedRows.length) {
        this.$message(`no data selected`);
        return;
      }

      const confirmed = await this.$swalConfirm({
        title: 'delete',
        text: `unrecoverble continue？`,
        icon: 'warning',
      });

      if (confirmed) {
        try {
          let ids = this.delectedRows.map(entity => entity.id);
          const response = await this.$axios.post(`/dietHistory/batchDelete`, ids);

          if (response.data.code === 200) {
            this.$notify({
              duration: 1000,
              title: 'delete',
              message: 'deleted successully',
              type: 'success'
            });
            this.fetchFreshData();
          }
        } catch (error) {
          this.$message.error("Error：", error);
        }
      }
    },

    async fetchFreshData() {
      try {
        this.tableData = [];

        let startTime = null;
        let endTime = null;

        if (this.searchTime && this.searchTime.length === 2) {
          const [startDate, endDate] = await Promise.all(
            this.searchTime.map(date => date.toISOString())
          );

          startTime = `${startDate.split('T')[0]}T00:00:00`;
          endTime   = `${endDate.split('T')[0]}T23:59:59`;
        }

        const params = {
          current: this.currentPage,
          size: this.pageSize,
          startTime,
          endTime,
          ...this.dietHistoryQueryDto
        };

        const response = await this.$axios.post('/dietHistory/query', params);
        const { data } = response;

        this.tableData = data.data;
        this.totalItems = data.total;

      } catch (error) {
        this.$message.error("Error:", error);
      }
    },

    handleDelete(row) {
      this.delectedRows.push(row);
      this.batchDelete();
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
  },
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

.cookbook-container {
  padding: 28px 28px 48px;
  font-family: 'DM Sans', sans-serif;
}

/* ───────── HEADER ───────── */

.page-header {
  display: flex;
  align-items: flex-end;         
  justify-content: space-between;
  margin-bottom: 24px;
  gap: 16px;
  flex-wrap: wrap;
}

/* 左边标题区域 */
.header-left {
  display: flex;
  flex-direction: column;
  gap: 2px;                    
}

/* 标题 */
.page-title {
  font-family: 'Caveat', cursive;
  font-size: 38px;
  font-weight: 700;
  color: #c8392b;
  margin: 0;
  line-height: 1;             
}

/* 副标题 */
.page-subtitle {
  font-size: 13px;
  color: #b0a898;
  margin: 0;                 
}

/* 右侧搜索区域 */
.header-right {
  display: flex;
  align-items: center;
  gap: 8px;                 
  flex-wrap: wrap;

  /* 输入框 */
  ::v-deep .el-input__inner {
    border-color: #d6c9b8;
    border-radius: 4px;
    font-size: 13px;
    color: #3a3028;
    background-color: rgba(255,255,255,0.8);

    &::placeholder {
      color: #c0b09e;
    }

    &:focus {
      border-color: #c8392b;
    }
  }

  /* 搜索按钮 */
  ::v-deep .el-input-group__append {
    background-color: #c8392b;
    border-color: #c8392b;
    color: #fff;

    &:hover {
      background-color: #b03226;
    }
  }

  /* 日期选择器 */
  ::v-deep .el-range-editor {
    border-color: #d6c9b8 !important;
    border-radius: 4px !important;
    background-color: rgba(255,255,255,0.8);
  }

  ::v-deep .el-range-input {
    font-size: 13px;
    color: #3a3028;

    &::placeholder {
      color: #c0b09e;
    }
  }

  ::v-deep .el-range-separator {
    color: #8a7d6e;
  }
}

/* ───────── TABLE ───────── */

.table-wrap {
  background: rgba(255,255,255,0.7);
  border: 1.5px solid #e8ddd0;
  border-radius: 4px;
  overflow: hidden;
}

/* ───────── BUTTON ───────── */

.text-btn {
  font-family: 'Klee One', cursive;
  font-size: 12px;
  cursor: pointer;
  padding: 3px 8px;
}

.text-btn.delete {
  color: #8a7d6e;
}

.text-btn.delete:hover {
  background: rgba(90,80,69,0.08);
}

/* ───────── PAGINATION ───────── */

.pagination {
  padding: 10px;
  text-align: right;
}
</style>