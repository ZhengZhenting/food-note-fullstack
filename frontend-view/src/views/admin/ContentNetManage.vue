<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 5px;">
            <el-row style="display: flex;justify-content: left;gap: 6px;">
                <el-date-picker style="width: 216px;" @change="fetchFreshData" size="small" v-model="searchTime"
                    type="daterange" range-separator="to" start-placeholder="Start Time" end-placeholder="End Time">
                </el-date-picker>
                 <el-input size="small" style="width: 226px;" v-model="contentNetQueryDto.accessCode" placeholder="access code" clearable
                    @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
            </el-row>
        </el-row>
        <el-row style="margin: 0 22px;border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData" style="width: 100%">
                <el-table-column prop="gourmetTitle" label="Content" mind-width="118"></el-table-column>
                <el-table-column prop="gourmetId" :sortable="true" label="Gourmet ID" min-width="108"></el-table-column>
                <el-table-column prop="userId" :sortable="true" label="User ID" min-width="88"></el-table-column>
                <el-table-column prop="userName" label="User Name" min-width="98"></el-table-column>
                <el-table-column prop="accessCode" label="Access Code" min-width="98"></el-table-column>
                <el-table-column prop="accessPassword" label="Access Password" min-width="118"></el-table-column>
                <el-table-column prop="validDay" :sortable="true" label="Valid Days" min-width="98"></el-table-column>
                <el-table-column prop="passwordAuth" label="Password Auth" min-width="98">
                    <template slot-scope="scope">
                        <span>{{ scope.row.passwordAuth ? 'Yes' : 'No' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="Create Time" :sortable="true"  min-width="128"></el-table-column>
                <el-table-column label="Operation" width="110">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleDelete(scope.row)">Delete</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin:10px 0;float: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[20, 50]"
                :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"></el-pagination>
        </el-row>
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
            tableData: [],
            delectedRows: [],
            searchTime: [], // 搜索时间范围
            contentNetQueryDto: {}, // 搜索条件
        };
    },
    created() {
        this.fetchFreshData();
    },
    methods: {
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
                    const response = await this.$axios.post(`/contentNet/batchDelete`, ids);
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
                    ...this.contentNetQueryDto
                };
                const response = await this.$axios.post('/contentNet/query', params);
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
        // 修改按钮删除事件 
        handleDelete(row) {
            this.delectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss"></style>