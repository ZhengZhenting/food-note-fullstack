<template>
  <div class="cookbook-container">

    <!-- ── Page header ── -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">Evaluations</h2>
        <p class="page-subtitle">{{ totalItems }} records found</p>
      </div>

      <div class="header-right">
        <el-date-picker
          style="width: 220px;"
          v-model="searchTime"
          @change="fetchFreshData"
          size="small"
          type="daterange"
          range-separator="→"
          start-placeholder="Start"
          end-placeholder="End"
        />

        <el-input
          size="small"
          style="width: 200px;"
          v-model="evalustionsQueryDto.content"
          placeholder="Search content…"
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
        <!-- 内容 -->
        <el-table-column prop="content" label="Content" min-width="220">
          <template slot-scope="scope">
            <el-tooltip effect="dark" :content="scope.row.content" placement="bottom">
              <div class="cell-name">{{ scope.row.content }}</div>
            </el-tooltip>
          </template>
        </el-table-column>

        <!-- 点赞 -->
        <el-table-column label="Likes" width="80">
          <template slot-scope="scope">
            <span class="like-count">
              {{ scope.row.upvoteList ? scope.row.upvoteList.split(',').length : 0 }}
            </span>
          </template>
        </el-table-column>

        <!-- 类型 -->
        <el-table-column prop="contentType" label="Source" width="110" />

        <!-- 用户 -->
        <el-table-column prop="userName" label="User" width="140" />

        <!-- 被回复 -->
        <el-table-column prop="replierName" label="Reply To" width="140" />

        <!-- 时间 -->
        <el-table-column prop="createTime" sortable label="Time" min-width="160" />

        <!-- 层级 -->
        <el-table-column label="Level" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.parentId === null" class="badge-parent">
              Parent
            </span>
            <span v-else class="badge-child">
              Child
            </span>
          </template>
        </el-table-column>

        <!-- 操作 -->
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
      filterText: '',
      tableData: [],
      currentPage: 1,
      pageSize: 20,
      totalItems: 0,
      searchTime: [],
      delectedRows: [],
      evalustionsQueryDto: {},
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
        this.$message(`未选中任何数据`);
        return;
      }

      const confirmed = await this.$swalConfirm({
        title: '删除评论数据',
        text: `删除后不可恢复，是否继续？`,
        icon: 'warning',
      });

      if (confirmed) {
        try {
          let ids = this.delectedRows.map(entity => entity.id);
          const response = await this.$axios.post(`/evaluations/batchDelete`, ids);

          if (response.data.code === 200) {
            this.$notify({
              duration: 1000,
              title: '删除',
              message: '删除成功',
              type: 'success'
            });
            this.fetchFreshData();
          }
        } catch (e) {
          console.error(`删除异常：`, e);
        }
      }
    },

    async fetchFreshData() {
      try {
        let startTime = '';
        let endTime = '';

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
          ...this.evalustionsQueryDto,
        };

        let response = await this.$axios.post('/evaluations/query', params);
        const { data } = response;

        this.tableData = data.data;
        this.totalItems = data.total;

      } catch (error) {
        console.error('查询异常:', error);
      }
    },

    handleFilter() {
      this.currentPage = 1;
      this.fetchFreshData();
    },

    handleFilterClear() {
      this.evalustionsQueryDto.content = '';
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

    handleDelete(row) {
      this.delectedRows.push(row);
      this.batchDelete();
    }
  },
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

.cookbook-container {
  padding: 28px;
  font-family: 'DM Sans', sans-serif;
}

/* ─── header ─── */
.page-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-bottom: 24px;
  gap: 16px;
  flex-wrap: wrap;
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 2px;   // ⭐ 关键：控制标题和副标题间距
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;

  /* 输入框 */
  ::v-deep .el-input__inner {
    border-color: #d6c9b8;
    border-radius: 4px;
    font-family: 'DM Sans', sans-serif;
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
    color: #fdf8f2;

    &:hover {
      background-color: #b03226;
    }
  }

  /* 日期选择器整体 */
  ::v-deep .el-range-editor {
    border-color: #d6c9b8 !important;
    border-radius: 4px !important;
    background-color: rgba(255,255,255,0.8);
  }

  /* 日期分隔符 */
  ::v-deep .el-range-separator {
    color: #8a7d6e;
  }

  /* 日期输入 */
  ::v-deep .el-range-input {
    font-family: 'DM Sans', sans-serif;
    font-size: 13px;
    color: #3a3028;
    background: transparent;

    &::placeholder {
      color: #c0b09e;
    }
  }
}

/* 标题 */
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

/* 表格 */
.table-wrap {
  background: rgba(255,255,255,0.7);
  border: 1.5px solid #e8ddd0;
  border-radius: 4px;
}

/* 点赞 */
.like-count {
  font-weight: bold;
  font-size: 14px;
  color: #c8392b;
}

/* 层级 */
.badge-parent {
  padding: 2px 10px;
  border-radius: 20px;
  background: rgba(200,57,43,0.08);
  color: #c8392b;
  font-size: 11px;
}

.badge-child {
  padding: 2px 10px;
  border-radius: 20px;
  background: rgba(90,80,69,0.08);
  color: #8a7d6e;
  font-size: 11px;
}

/* 操作按钮 */
.text-btn {
  font-family: 'Klee One', cursive;
  font-size: 12px;
  cursor: pointer;
}

.text-btn.delete {
  color: #8a7d6e;
}

.text-btn.delete:hover {
  background: rgba(90,80,69,0.08);
}

/* 分页 */
.pagination {
  padding: 10px;
  text-align: right;
}
</style>