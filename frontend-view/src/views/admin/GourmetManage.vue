<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 5px;">
            <el-row style="display: flex;justify-content: left;gap: 6px;">
                <el-select style="width: 100px;" @change="fetchFreshData" size="small" v-model="gourmetQueryDto.categoryId"
                    placeholder="category" clearable>
                    <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <el-input size="small" style="width: 186px;" v-model="gourmetQueryDto.title" placeholder="search for title" clearable
                    @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
                <el-select style="width: 150px;" @change="fetchFreshData" size="small" v-model="gourmetQueryDto.isAudit"
                    placeholder="Audit Status" clearable>
                    <el-option v-for="item in auditStatuList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-select style="width: 150px;" @change="fetchFreshData" size="small" v-model="gourmetQueryDto.isPublish"
                    placeholder="Publish Status" clearable>
                    <el-option v-for="item in publishStatuList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-date-picker style="width: 216px;" @change="fetchFreshData" size="small" v-model="searchTime"
                    type="daterange" range-separator="to" start-placeholder="Start Time" end-placeholder="End Time">
                </el-date-picker>
            </el-row>
        </el-row>
        <el-row style="margin: 0 22px;border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData" style="width: 100%" :fit="true">
                <el-table-column prop="cover" label="Cover" min-width="70">
                    <template slot-scope="scope">
                        <img :src="scope.row.cover"  style="width: 50px; height: 50px;"/>
                    </template>
                </el-table-column>
                <el-table-column prop="title" label="Title" min-width="155"></el-table-column>
                <el-table-column prop="categoryId" label="Category ID" :sortable ="true" min-width="118"></el-table-column>
                <el-table-column prop="categoryName" label="Category" min-width="118"></el-table-column>
                <el-table-column prop="userId" label="User ID" :sortable ="true" min-width="98"></el-table-column>
                <el-table-column prop="userName" label="User" min-width="118"></el-table-column>
                <el-table-column prop="createTime" sortable="true" label="Create Time" min-width="168"></el-table-column>
                <el-table-column prop="isAudit" label="Audit Status" min-width="98">
                    <template slot-scope="scope">
                        <i v-if="!scope.row.isAudit" style="margin-right: 5px;" class="el-icon-warning"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-success"></i>
                        <el-tooltip v-if="!scope.row.isAudit" class="item" effect="dark"
                            content="Contact Admin for free pass" placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">No</span>
                        </el-tooltip>
                        <span v-else>Yes</span>
                    </template>
                </el-table-column>
                <el-table-column prop="isPublish" label="Publish Status" min-width="118">
                    <template slot-scope="scope">
                        <i v-if="!scope.row.isPublish" style="margin-right: 5px;" class="el-icon-warning"></i>
                        <i v-else style="margin-right: 5px;color: rgb(253, 199, 50);" class="el-icon-success"></i>
                        <el-tooltip v-if="!scope.row.isPublish" class="item" effect="dark"
                            content="Unpublished content is not accessible to public" placement="bottom-end">
                            <span style="text-decoration: underline;text-decoration-style: dashed;">No</span>
                        </el-tooltip>
                        <span v-else>Yes</span>
                    </template>
                </el-table-column>
                <el-table-column label="Operation" width="120">
                    <template slot-scope="scope">
                        <span class="text-button" v-if="!scope.row.isAudit" @click="handleEdit(scope.row)">Audit</span>
                        <span class="text-button" @click="handleDelete(scope.row)">Delete</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;float: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[20, 50]"
                :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"></el-pagination>
        </el-row>
        <!-- 操作面板 -->
        <el-dialog :show-close="false" :visible.sync="dialogGourmetOperaion" width="18%">
            <div style="padding:16px 20px;"> 
                confirm to audit "{{ data.title }}"?
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    cancle
                </span>
                <span  class="edit-button" @click="auditOperation()">
                    confirm
                </span>
            </span>
        </el-dialog>
    </el-row>
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
<style scoped lang="scss"></style>