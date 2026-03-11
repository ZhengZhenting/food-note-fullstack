<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 5px;">
            <el-row style="display: flex;justify-content: left;gap: 6px;">
                <span class="edit-button" @click="add()">
                    Add Cookbook Nuriment
                </span>
                <el-input size="small" style="width: 166px;" v-model="cookbookNutrimentQueryDto.name"
                    placeholder="search..." clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
                <el-select style="width: 160px;" @change="fetchFreshData" size="small"
                    v-model="cookbookNutrimentQueryDto.cookbookId" placeholder="cookbook" clearable>
                    <el-option v-for="item in cookbookItems" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <el-select style="width: 160px;" @change="fetchFreshData" size="small"
                    v-model="cookbookNutrimentQueryDto.nutrimentId" placeholder="nutriment" clearable>
                    <el-option v-for="item in nutrimentItems" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
            </el-row>
        </el-row>
        <el-row style="margin: 0 22px;border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData" style="width: 100%">
                <el-table-column prop="cookbookId" :sortable="true" label="Cookbook ID"></el-table-column>
                <el-table-column prop="cookbookName" label="Cookbook"></el-table-column>
                <el-table-column prop="nutrimentId" :sortable="true" label="Nutriment ID"></el-table-column>
                <el-table-column prop="nutrimentName" label="Nutriment Name"></el-table-column>
                <el-table-column prop="nutrimentUnit" label="Nutriment Unit"></el-table-column>
                <el-table-column prop="g100Value" label="/ 100 Unit"></el-table-column>
                <el-table-column label="Operation" width="110">
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
        <!-- 操作面板 -->
        <el-dialog :show-close="false" :visible.sync="dialogCategoryOperaion" width="40%">
            <div style="padding:16px 20px;">
                <p> {{ isOperation ? 'edit Nutriment' : 'add Nutriment' }}</p>
                <el-row>
                    <span class="dialog-hover">Choose Cookbook</span>
                    <el-select style="width: 250px;" @change="fetchFreshData" size="small" v-model="data.cookbookId"
                        placeholder="cookbook" clearable>
                        <el-option v-for="item in cookbookItems" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-row>
                <el-row>
                    <span class="dialog-hover">Choose Nutriment</span>
                    <el-select style="width: 250px;" @change="fetchFreshData" size="small" v-model="data.nutrimentId"
                        placeholder="nutriment" clearable>
                        <el-option v-for="item in nutrimentItems" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-row>
                <el-row>
                    <span class="dialog-hover"> /100 Unit </span>
                    <input class="dialog-input" v-model="data.g100Value" placeholder="input" />
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    取消操作
                </span>
                <span v-if="!isOperation" class="edit-button" @click="addOperation()">
                    确定新增
                </span>
                <span v-else class="edit-button" @click="updateOperation()">
                    确定修改
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
            dialogCategoryOperaion: false, // 开关
            isOperation: false, // 默认新增
            tableData: [],
            delectedRows: [],
            cookbookNutrimentQueryDto: {}, // 搜索条件
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
        // 食谱下拉选择器
        async fetchCookBookListItem() {
            try {
                const response = await this.$axios.get(`/cookbook/querySelectedItems`);
                if (response.data.code === 200) {
                    this.cookbookItems = response.data.data;
                    this.cookbookItems.unshift({ id: null, name: 'All' })
                }
            } catch (error) {
                console.log(error);
            }
        },
        // 营养素下拉选择器
        async fetchNutrimentListItem() {
            try {
                const response = await this.$axios.get(`/nutriment/querySelectedItems`);
                if (response.data.code === 200) {
                    this.nutrimentItems = response.data.data;
                    this.nutrimentItems.unshift({ id: null, name: 'All' })
                }
            } catch (error) {
                console.log(error);
            }
        },
        cannel() {
            this.data = {};
            this.dialogCategoryOperaion = false;
            this.isOperation = false;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.delectedRows.length) {
                this.$message(`未选中任何数据`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '删除美食类别数据',
                text: `删除后不可恢复，是否继续？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.delectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/cookbookNutriment/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$notify({
                            duration: 1000,
                            title: 'Delete',
                            message: 'Deleted',
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
                const response = await this.$axios.put('/cookbookNutriment/update', this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'Edit',
                        message: 'Edited',
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
            try {
                const response = await this.$axios.post('/cookbookNutriment/save', this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'Add',
                        message: 'Added',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                } else {
                    this.$notify({
                        duration: 1000,
                        title: 'Add',
                        message: response.data.msg,
                        type: 'error'
                    });
                }
            } catch (error) {
                console.error('Error:', error);
                this.$message.error('Error, please try later！');
            }
        },
        // 信息查询
        async fetchFreshData() {
            try {
                // 请求参数
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    key: this.filterText,
                    ...this.cookbookNutrimentQueryDto
                };
                const response = await this.$axios.post('/cookbookNutriment/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                this.$message.error("Error:", error);
                console.error('Error:', error);
            }
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
        // 美食类别修改按钮点击事件 
        handleEdit(row) {
            this.dialogCategoryOperaion = true;
            this.isOperation = true;
            this.data = { ...row }
        },
        // 美食类别修改按钮删除事件 
        handleDelete(row) {
            this.delectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss"></style>