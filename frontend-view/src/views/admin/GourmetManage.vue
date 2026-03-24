<template>
  <div class="cookbook-container">

    <!-- ───────── HEADER ───────── -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">Gourmet</h2>
        <p class="page-subtitle">{{ totalItems }} records found</p>
      </div>

      <div class="header-right">
        <el-select
          style="width: 120px;"
          size="small"
          v-model="gourmetQueryDto.categoryId"
          placeholder="Category"
          clearable
          @change="fetchFreshData"
        >
          <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>

        <el-input
          size="small"
          style="width: 180px;"
          v-model="gourmetQueryDto.title"
          placeholder="Search title…"
          clearable
          @clear="handleFilterClear"
        >
          <el-button slot="append" icon="el-icon-search" @click="handleFilter" />
        </el-input>

        <el-select
          style="width: 140px;"
          size="small"
          v-model="gourmetQueryDto.isAudit"
          placeholder="Audit"
          clearable
          @change="fetchFreshData"
        >
          <el-option v-for="item in auditStatuList" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>

        <el-select
          style="width: 140px;"
          size="small"
          v-model="gourmetQueryDto.isPublish"
          placeholder="Publish"
          clearable
          @change="fetchFreshData"
        >
          <el-option v-for="item in publishStatuList" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>

        <el-date-picker
          style="width: 220px;"
          size="small"
          v-model="searchTime"
          type="daterange"
          range-separator="→"
          start-placeholder="Start"
          end-placeholder="End"
          @change="fetchFreshData"
        />
      </div>
    </div>

    <!-- ───────── TABLE ───────── -->
    <div class="table-wrap">
      <el-table
        :data="tableData"
        style="width: 100%"
        :header-cell-style="headerStyle"
        :cell-style="cellStyle"
      >
        <el-table-column label="Cover" min-width="80">
          <template slot-scope="scope">
            <img :src="scope.row.cover" class="cover-img" />
          </template>
        </el-table-column>

        <el-table-column prop="title" label="Title" min-width="160" />
        <el-table-column prop="categoryId" label="Category ID" min-width="150" sortable />
        <el-table-column prop="categoryName" label="Category" min-width="120" />
        <el-table-column prop="userId" label="User ID" min-width="110" sortable />
        <el-table-column prop="userName" label="User" min-width="90" />
        <el-table-column prop="createTime" label="Create Time" min-width="170" sortable />

        <el-table-column label="Audit" min-width="90">
          <template slot-scope="scope">
            <span :class="scope.row.isAudit ? 'badge-yes' : 'badge-no'">
              {{ scope.row.isAudit ? 'Yes' : 'No' }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="Publish" min-width="100">
          <template slot-scope="scope">
            <span :class="scope.row.isPublish ? 'badge-yes' : 'badge-no'">
              {{ scope.row.isPublish ? 'Yes' : 'No' }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="Actions" width="100">
          <template slot-scope="scope">
            <span
              class="text-btn"
              v-if="!scope.row.isAudit"
              @click="handleEdit(scope.row)"
            >
              Audit
            </span>
            <span class="text-btn delete" @click="handleDelete(scope.row)">
              Delete
            </span>
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

    <!-- ───────── DIALOG ───────── -->
    <el-dialog :visible.sync="dialogGourmetOperaion" width="420px">
      <div class="dialog-body">
        Confirm to audit "{{ data.title }}" ?
      </div>

      <span slot="footer" class="dialog-footer">
        <span class="text-btn" @click="cannel()">Cancel</span>
        <span class="text-btn primary" @click="auditOperation()">Confirm</span>
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
            dialogGourmetOperaion: false, // 开关
            isOperation: false, // 默认新增
            tableData: [],
            delectedRows: [],
            searchTime: [], // 搜索时间范围
            gourmetQueryDto: {}, // 搜索条件
            categories:[], // 分类列表
            auditStatuList: [{ value: null, label: 'all' }, { value: 0, label: 'No' }, { value: 1, label: 'Yes' }],
            auditStatuList: [{ value: null, label: 'all' }, { value: 0, label: 'No' }, { value: 1, label: 'Yes' }],
            publishStatuList: [{ value: null, label: 'all' }, { value: 0, label: 'No' }, { value: 1, label: 'Yes' }]
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchFreshCategory();
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
                fontSize: '14px',
                color: '#3a3028',
                backgroundColor: 'transparent',
                borderBottom: '1px solid #f0ebe0',
            };
        },
        // 查询分类信息
        fetchFreshCategory() {
            this.$axios.post('/category/query', {}).then(response => {
                if(response.data.code === 200) {
                    this.categories = response.data.data;
                    this.categories.unshift({id: null, name: 'All'})
                }
            }).catch(error => {
                console.log('Error:', error );
            });
        },
        cannel() {
            this.data = {};
            this.dialogGourmetOperaion = false;
            this.isOperation = false;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.delectedRows.length) {
                this.$message(`no data selected`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: 'Delete Confirmation',
                text: `File is unrecoverable, continue？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.delectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/gourmet/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$notify({
                            duration: 1000,
                            title: 'delete operation',
                            message: 'success',
                            type: 'success'
                        });
                        this.fetchFreshData();
                        return;
                    }
                } catch (error) {
                    this.$message.error("Error：", error);
                    console.error(`Error：`, error);
                }
            }
        },
        // 修改信息
        async updateOperation() {
            try {
                const response = await this.$axios.put('/gourmet/update', this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'update operation',
                        message: 'success',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('Error', error);
            }
        },
        // 审核信息
        async auditOperation() {
            try {
                const response = await this.$axios.put(`/gourmet/audit/${this.data.id}`, this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'audit operation',
                        message: 'success',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('Error:', error);
                this.$message.error('Failed, please try later！');
            }
        },
        // 信息查询
        async fetchFreshData() {
            try {
                this.tableData = [];
                let startTime = null;
                let endTime = null;
                if (this.searchTime != null && this.searchTime.length === 2) {
                    const [startDate, endDate] = await Promise.all(this.searchTime.map(date => date.toISOString()));
                    startTime = `${startDate.split('T')[0]}T00:00:00`;
                    endTime = `${endDate.split('T')[0]}T23:59:59`;
                }
                // 请求参数
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    key: this.filterText,
                    startTime: startTime,
                    endTime: endTime,
                    ...this.gourmetQueryDto
                };
                const response = await this.$axios.post('/gourmet/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                this.$message.error("Error:", error);
                console.error('Error:', error);
            }
        },
        add() {
            this.dialogGourmetOperaion = true;
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
        // 美食做法修改按钮点击事件 
        handleEdit(row) {
            this.dialogGourmetOperaion = true;
            this.isOperation = true;
            this.data = { ...row }
        },
        // 美食做法修改按钮删除事件 
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

/* HEADER */
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
  gap: 2px;
}

.page-title {
  font-family: 'Caveat', cursive;
  font-size: 38px;
  color: #c8392b;
  margin: 0;
  line-height: 1;
}

.page-subtitle {
  font-size: 13px;
  color: #b0a898;
  margin: 0;
}

.header-right {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  align-items: center;

  ::v-deep .el-input__inner {
    border-color: #d6c9b8;
  }

  ::v-deep .el-input-group__append {
    background: #c8392b;
    color: white;
  }

  ::v-deep .el-range-editor {
    border-color: #d6c9b8;
  }
}

/* TABLE */
.table-wrap {
  background: rgba(255,255,255,0.7);
  border: 1.5px solid #e8ddd0;
  border-radius: 4px;
  overflow: hidden;
}

/* IMAGE */
.cover-img {
  width: 50px;
  height: 50px;
  border-radius: 4px;
  object-fit: cover;
}

/* TAG */
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

/* BUTTON */
.text-btn {
  font-family: 'Klee One', cursive;
  font-size: 12px;
  cursor: pointer;
  margin-right: 8px;
}

.text-btn.delete {
  color: #8a7d6e;
}

.text-btn.primary {
  color: #c8392b;
}

/* DIALOG */
.dialog-body {
  padding: 20px;
  font-size: 14px;
}

/* PAGINATION */
.pagination {
  padding: 10px;
  text-align: right;
}
</style>