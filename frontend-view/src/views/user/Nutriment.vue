<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 5px;">
            <el-row style="display: flex;justify-content: left;gap: 6px;">
                <el-date-picker style="width: 216px;" @change="fetchFreshData" size="small" v-model="searchTime"
                    type="daterange" range-separator="to" start-placeholder="Strat Time" end-placeholder="End Time">
                </el-date-picker>
                <el-input size="small" style="width: 166px;" v-model="dietHistoryQueryDto.cookbookId"
                    placeholder="cookbookId" clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
            </el-row>
        </el-row>
        <el-row style="margin: 0 22px;border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData" style="width: 100%">
                <el-table-column prop="cookbookCover" width="80" label="Cover">
                    <template slot-scope="scope">
                        <img :src="scope.row.cookbookCover" style="width:40px; height: 40px; border-radius: 5px;">
                    </template>
                </el-table-column>
                <el-table-column prop="cookbookTitle" label="Cookbook Name"></el-table-column>
                <el-table-column prop="detail" label="Notes" width="150"></el-table-column>
                <el-table-column prop="value" label="Value/g" width="80"></el-table-column>
                <el-table-column prop="createTime" label="Time" width="180"></el-table-column>
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
        <div style="padding-inline: 20px">
            <el-row>
                <span class="dialog-hover">Choose Nutriment</span>
                <el-select style="width: 250px;" @change="loadNutrimentData" size="small" v-model="dietQueryDto.nutrimentId"
                    placeholder="nutriment" clearable>
                    <el-option v-for="item in nutrimentItems" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
            </el-row>
            <LineChart tag="Nutriment Intake" @on-selected="onSelected" :values="values" :date="dates" />
        </div>
        <!-- 操作面板 -->
        <el-dialog :show-close="false" :visible.sync="dialogDietOperaion" width="25%">
            <div style="padding:16px 20px;">
                <p>Choose Cookbook</p>
                <el-select style="width: 100%;" size="small" v-model="data.cookbookId" placeholder="choose cookbook">
                    <el-option v-for="item in cookbooks" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <p>Value/g</p>
                <input class="dialog-input" v-model="data.value" placeholder="input">
                <p>Notes</p>
                <el-input style="margin-top: 10px;" type="textarea" rows="3" placeholder="add notes"
                    v-model="data.detail"></el-input>
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    cancle
                </span>
                <span class="edit-button" @click="updateOperation()">
                    confirm
                </span>
            </span>
        </el-dialog>
    </el-row>
</template>

<script>
import LineChart from "@/components/LineChart"
export default {
    components: { LineChart },
    data() {
        return {
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 20,
            totalItems: 0,
            dialogDietOperaion: false, // 开关
            tableData: [],
            delectedRows: [],
            dietHistoryQueryDto: {}, // 搜索条件
            cookbooks: [],
            dietHistory: {},
            searchTime: [],
            dietQueryDto: { day: 365 },
            nutrimentUseList: [],
            values: [],
            dates: [],
            nutrimentItems: []
        };
    },
    created() {
        this.fetchFreshData();
        this.fetchCookbookList();
        this.loadNutrimentData();
        this.fetchNutrimentListItem();
    },
    methods: {
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
        onSelected(day) {
            this.dietQueryDto.day = day;
            this.loadNutrimentData();
        },
        //查询营养摄入情况
        loadNutrimentData() {
            this.$axios.post(`/dietHistory/queryDietNutrimentInfo`, this.dietQueryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.nutrimentUseList = data.data;
                    this.dates = this.nutrimentUseList.map(entity => entity.time);
                    this.values = this.nutrimentUseList.map(entity => entity.value);
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        fetchCookbookList() {
            this.$axios.get(`/cookbook/querySelectedItemsAll`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.cookbooks = data.data;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        cannel() {
            this.data = {};
            this.dialogDietOperaion = false;
            this.isOperation = false;
        },
        // 批量删除数据
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
            console.log("update data:", this.data);
            try {
                const response = await this.$axios.put('/dietHistory/update', this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'edit',
                        message: 'edited successfully',
                        type: 'success'
                    });
                    this.cannel();
                    this.fetchFreshData();
                }
            } catch (error) {
                console.error('Error:', error);
                this.$message.error('update failed, please try later');
            }
        },
        // 信息新增
        async addOperation() {
            try {
                const response = await this.$axios.post('/dietHistory/save', this.data);
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'add',
                        message: 'added successfully',
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
        // 信息查询
        async fetchFreshData() {
            try {
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
                    ...this.dietHistoryQueryDto
                };
                const response = await this.$axios.post('/dietHistory/queryUser', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                this.$message.error("Error:", error);
                console.error('Error:', error);
            }
        },
        add() {
            this.dialogDietOperaion = true;
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
            this.dialogDietOperaion = true;

            this.data = {
                id: row.id,
                cookbookId: row.cookbookId,
                value: row.value,
                detail: row.detail
            };
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