<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 5px;">
            <el-row style="display: flex;justify-content: left;gap: 6px;">
                <span class="edit-button" @click="add()">
                    New Cookbook
                </span>
                <el-select style="width: 100px;" @change="fetchFreshData" size="small"
                    v-model="cookbookQueryDto.categoryId" placeholder="category" clearable>
                    <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <el-date-picker style="width: 216px;" @change="fetchFreshData" size="small" v-model="searchTime"
                    type="daterange" range-separator="to" start-placeholder="Start Time" end-placeholder="End Time">
                </el-date-picker>
                <el-input size="small" style="width: 226px;" v-model="cookbookQueryDto.title" placeholder="title"
                    clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
            </el-row>
        </el-row>
        <el-row style="margin: 0 22px;border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData" style="width: 100%">
                <el-table-column prop="userAvatar" width="68" label="Avatar">
                    <template slot-scope="scope">
                        <el-avatar :size="25" :src="scope.row.userAvatar" style="margin-top: 10px;"></el-avatar>
                    </template>
                </el-table-column>
                <el-table-column prop="userId" :sortable="true" label="User ID" min-width="68"></el-table-column>
                <el-table-column prop="userName" label="User Name" min-width="88"></el-table-column>
                <el-table-column prop="title" label="Cookbook Name" mind-width="118"></el-table-column>
                <el-table-column prop="id" :sortable="true" label="Cookbook ID" min-width="88"></el-table-column>
                <el-table-column prop="categoryName" label="Category" min-width="68"></el-table-column>
                <el-table-column prop="isPublish" label="publish" min-width="68">
                    <template slot-scope="scope">
                        <span>{{ scope.row.isPublish ? 'Public' : 'Private' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="Create Time" :sortable="true"
                    min-width="128"></el-table-column>
                <el-table-column label="Operation" min-width="100">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleEdit(scope.row)">Edit</span>
                        <span class="text-button" @click="handleDelete(scope.row)">Delete</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;float: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[20, 50]"
                :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"></el-pagination>
        </el-row>
        <el-dialog :show-close="false" :visible.sync="dialogCookbookOperaion" width="50%">
            <div style="padding:16px 20px; max-height: 550px; overflow-y: auto;">
                <p> {{ isOperation ? 'edit cookbook' : 'add cookbook' }}</p>
                 <el-row>
                    <p>cover</p>
                    <el-upload class="avatar-uploader"
                        action="http://localhost:21090/api/food-share-sys/v1.0/file/upload" :show-file-list="false"
                        :on-success="handleCoverSuccess">
                        <img v-if="cover" :src="cover" style="width: 200px; height: 150px">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-row>
                <div>
                    <input class="dialog-input" v-model="data.title" placeholder="Title" />
                </div>
                <div>
                    <Editor :receiveContent="data.content" @on-receive="onReceive" />
                </div>
                <div style="margin-block: 10px">
                    <span class="dialog-hober">Category</span>
                    <el-select style="width: 100px;" @change="fetchFreshData" size="small" v-model="data.categoryId"
                        placeholder="category" clearable>
                        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </div>

            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    cancle
                </span>
                <span v-if="!isOperation" class="edit-button" @click="addOperation()">
                    yes
                </span>
                <span v-else class="edit-button" @click="updateOperation()">
                    confirm
                </span>
            </span>
        </el-dialog>
    </el-row>
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
            searchTime: [], // 搜索时间范围
            categories: [], // 分类列表
            dialogCookbookOperaion: false, // 弹窗
            isOperation: false, // 默认新增
            cookbookQueryDto: {}, // 搜索条件
            cover: null,
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchFreshCategories();
    },
    methods: {
        handleCoverSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: 'Cover Upload',
                message: res.code === 200 ? 'Upload Success' : 'Upload Failed',
                type: res.code === 200 ? 'success' : 'error'
            });
              // 上传成功则更新封面
            if (res.code === 200) {
                this.cover = res.data;
            }
        },
        cannel() {
            this.data = {};
            this.cover=this.cover;
            this.dialogCookbookOperaion = false;
            this.isOperation = false;
        },
        addOperation() {
            this.data.cover=this.cover;
            this.$axios.post('/cookbook/userSave', this.data).then(response => {
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'add new cookbook',
                        message: 'successful',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            }).catch(error => {
                console.log('Error:', error);
            });
        },
        updateOperation() {
            this.data.cover=this.cover;
            this.$axios.put('/cookbook/update', this.data).then(response => {
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'add new cookbook',
                        message: 'successful',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            }).catch(error => {
                console.log('Error:', error);
            });
        },
        onReceive(content) {
            // 接收 Editor 组件传递的内容
            this.data.content = content;
        },
        add() {
            this.dialogCookbookOperaion = true;
        },
        fetchFreshCategories() {
            this.$axios.post('/category/query', {}).then(response => {
                if (response.data.code === 200) {
                    this.categories = response.data.data;
                    this.categories.unshift({ id: null, name: 'All' })
                }
            }).catch(error => {
                console.log('Error:', error);
            });
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.delectedRows.length) {
                this.$message(`未选中任何数据`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: 'Detele Confirmation',
                text: `Cotent unrecoverable, continue？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.delectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/cookbook/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$notify({
                            duration: 1000,
                            title: 'delete',
                            message: 'Deleted successfully',
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
        // 信息查询
        async fetchFreshData() {
            try {
                // 请求参数
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
                    ...this.cookbookQueryDto
                };
                const response = await this.$axios.post('/cookbook/queryUser', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                this.$message.error("Error:", error);
                console.error('Error:', error);
            }
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
            this.cover=row.cover;
            this.data = row;
            this.dialogCookbookOperaion = true;
            this.isOperation = true;
        },
        // 修改按钮删除事件 
        handleDelete(row) {
            this.delectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss"></style>