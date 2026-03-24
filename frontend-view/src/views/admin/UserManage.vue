<template>
  <div class="contentnet-container">
    <!-- ── Page header ── -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">User Management</h2>
        <p class="page-subtitle">{{ totalItems }} records found</p>
      </div>
      <div class="header-right">
        <span class="edit-button" @click="add()">New User</span>
        <el-select style="width: 100px;" @change="fetchFreshData" size="small" v-model="userQueryDto.isLogin"
          placeholder="Status">
          <el-option v-for="item in loginStatuList" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
        <el-select style="width: 100px;" @change="fetchFreshData" size="small" v-model="userQueryDto.isWord"
          placeholder="Banned">
          <el-option v-for="item in wordStatuList" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
        <el-select style="width: 100px;" @change="fetchFreshData" size="small" v-model="userQueryDto.role"
          placeholder="Role">
          <el-option v-for="item in rolesList" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
        <el-date-picker style="width: 216px;" @change="fetchFreshData" size="small" v-model="searchTime"
          type="daterange" range-separator="→" start-placeholder="Registered start" end-placeholder="Registered end">
        </el-date-picker>
        <el-input size="small" style="width: 166px;" v-model="userQueryDto.userName" placeholder="User Name" clearable
          @clear="handleFilterClear">
          <el-button slot="append" @click="handleFilter" icon="el-icon-search" />
        </el-input>
      </div>
    </div>

    <!-- ── Table ── -->
    <div class="table-wrap">
      <el-table :data="tableData" style="width: 100%;" :header-cell-style="headerStyle" :cell-style="cellStyle" stripe>
        <el-table-column prop="userAvatar" width="68" label="Avatar">
          <template slot-scope="scope">
            <el-avatar :size="25" :src="scope.row.userAvatar" style="margin-top: 10px;" />
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="Name" min-width="70" />
        <el-table-column prop="userAccount" width="128" label="Account" />
        <el-table-column prop="userEmail" width="168" label="Email" />
        <el-table-column prop="userRole" width="68" label="Role">
          <template slot-scope="scope">
            <span>{{ scope.row.userRole === 1 ? 'Admin' : 'User' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="isLogin" width="150" label="Account suspended">
          <template slot-scope="scope">
            <span :class="scope.row.isLogin ? 'badge-no' : 'badge-yes'">
              {{ scope.row.isLogin ? 'Suspended' : 'Normal' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="isWord" width="108" label="Banned">
          <template slot-scope="scope">
            <span :class="scope.row.isWord ? 'badge-no' : 'badge-yes'">
              {{ scope.row.isWord ? 'Muted' : 'Normal' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" width="168" label="Registered at" sortable />
        <el-table-column label="Operation" width="170">
          <template slot-scope="scope">
            <span class="text-btn" @click="handleStatus(scope.row)">Status</span>
            <span class="text-btn" @click="handleEdit(scope.row)">Edit</span>
            <span class="text-btn delete" @click="handleDelete(scope.row)">Cancel</span>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="pagination" @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="currentPage" :page-sizes="[20, 50]" :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="totalItems" />
    </div>
  </div>
</template>

<script>
export default {
    data() {
        return {
            userPwd: '',
            userAvatar: '',
            data: {},
            filterText: '',
            isAdmin: false, // 是否是管理员标志，初始值为false
            currentPage: 1,
            pageSize: 20,
            totalItems: 0,
            dialogStatusOperation: false,
            dialogUserOperaion: false, // 开关
            isOperation: false, // 开关-标识新增或修改
            tableData: [],
            searchTime: [],
            selectedRows: [],
            status: null,
            userQueryDto: {}, // 搜索条件
            loginStatuList: [{ value: null, label: 'All' }, { value: 0, label: 'Normal' }, { value: 1, label: 'Banned' }],
            wordStatuList: [{ value: null, label: 'All' }, { value: 0, label: 'Normal' }, { value: 1, label: 'Muted' }],
            rolesList: [{ value: null, label: 'All' }, { value: 2, label: 'User' }, { value: 1, label: 'Admin' }]
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
                fontSize: '14px',
                color: '#3a3028',
                backgroundColor: 'transparent',
                borderBottom: '1px solid #f0ebe0',
            };
        },
        cannel() {
            this.data = {};
            this.userAvatar = '';
            this.userPwd = '';
            this.dialogUserOperaion = false;
            this.dialogStatusOperation = false;
            this.isOperation = false;
        },
        comfirmStatus() {
            const userUpdateDto = {
                id: this.data.id,
                isLogin: this.data.isLogin,
                userRole: this.isAdmin ? 1 : 2,
                isWord: this.data.isWord
            }
            this.$axios.put(`/user/backUpdate`, userUpdateDto).then(res => {
                if (res.data.code === 200) {
                    this.$notify({
                        duration: 1500,
                        title: 'Update Operation',
                        message: 'success',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            }).catch(error => {
                console.log("Error：" + error);
            })
        },
        // 修改用户状态
        handleStatus(data) {
            // 设置用户角色
            this.isAdmin = data.userRole === 1;
            this.dialogStatusOperation = true;
            this.data = data;
        },
        // 头像上传回调函数
        handleAvatarSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: 'Avatar Upload',
                message: res.code === 200 ? 'Upload Success' : 'Upload Failed',
                type: res.code === 200 ? 'success' : 'error'
            });
            // 上传成功则更新用户头像
            if (res.code === 200) {
                this.userAvatar = res.data;
            }
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.selectedRows.length) {
                this.$message(`no data selected`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: 'delete confirmation',
                text: `operation unrecoverable, continue？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/user/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$notify({
                            duration: 1000,
                            title: 'Delete Operation',
                            message: 'success',
                            type: 'success'
                        });
                        this.fetchFreshData();
                        return;
                    }
                } catch (error) {
                    this.$message.error("Error deleting user information：", error);
                    console.error(`Error deleting user information：`, error);
                }
            }
        },
        // 修改信息
        async updateOperation() {
            if (this.userPwd !== '') {
                const pwd = this.$md5(this.$md5(this.userPwd));
                this.data.userPwd = pwd;
            } else {
                this.data.userPwd = null;
            }
            this.data.userAvatar = this.userAvatar;
            try {
                const response = await this.$axios.put('/user/backUpdate', this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'Update Operation',
                        message: 'success',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('Error:', error);
            }
        },
        // 信息新增
        async addOperation() {
            //  密码处理
            if (this.userPwd !== '') {
                this.data.userPwd = this.$md5(this.$md5(this.userPwd));
            } else {
                this.data.userPwd = null;
            }
            this.data.userAvatar = this.userAvatar;
            try {
                const response = await this.$axios.post('/user/insert', this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'Add Operation',
                        message: 'success',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('Error:', error);
                this.$message.error('error, please try later！');
            }
        },
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
                    ...this.userQueryDto
                };
                const response = await this.$axios.post('/user/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                this.$message.error("Error:", error);
                console.error('Error:', error);
            }
        },
        add() {
            this.dialogUserOperaion = true;
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
            this.dialogUserOperaion = true;
            this.isOperation = true;
            row.userPwd = null;
            this.userAvatar = row.userAvatar;
            this.data = { ...row }
        },
        handleDelete(row) {
            this.selectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>


<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

.contentnet-container {
  padding: 28px 28px 48px;
  font-family: 'DM Sans', sans-serif;
  min-height: 100%;
}

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
  gap: 10px;
  flex-wrap: wrap;

  ::v-deep .el-input__inner {
    border-color: #d6c9b8;
    border-radius: 4px;
    font-family: 'DM Sans', sans-serif;
    font-size: 13px;
    color: #3a3028;
    background-color: rgba(255, 255, 255, 0.8);

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
    background-color: rgba(255, 255, 255, 0.8);
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

.table-wrap {
  background-color: rgba(255, 255, 255, 0.7);
  border: 1.5px solid #e8ddd0;
  border-radius: 4px;
  overflow: hidden;

  ::v-deep .el-table {
    background-color: transparent;
    tr:hover > td { background-color: rgba(200, 57, 43, 0.03) !important; }
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
    &:hover { background-color: rgba(90, 80, 69, 0.08); color: #5a5045; }
  }
}

.edit-button {
  background-color: #c8392b;
  color: #fdf8f2;
  padding: 3px 12px;
  border-radius: 4px;
  font-family: 'Klee One', cursive;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.15s;
  &:hover { background-color: #b03226; }
}

.pagination {
  padding: 10px 14px;
  text-align: right;

  ::v-deep .el-pager li.active { color: #c8392b; }
  ::v-deep .el-pager li:hover { color: #c8392b; }
}
</style>