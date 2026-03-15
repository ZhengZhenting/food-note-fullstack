<template>
    <div>
        <div class="record" @click="recordGourmet">
            <span style="font-size:22px;font-weight: bold;">
                <i class="el-icon-chicken" style="margin-right: 10px;"> </i>Add Cookbook
            </span>
        </div>
        <el-dialog :show-close="false" :visible.sync="dialogGourmetOperaion" width="25%">
            <div style="padding:16px 20px;">
                <p>Choose Cookbook</p>
                <el-select style="width: 100%;" size="small" v-model="dietHistory.cookbookId"
                    placeholder="choose cookbook">
                    <el-option v-for="item in cookbooks" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
                <p>Value/g</p>
                <input class="dialog-input" v-model="dietHistory.value" placeholder="input">
                <p>Notes</p>
                <el-input style="margin-top: 10px;" type="textarea" rows="3" placeholder="add notes"
                    v-model="dietHistory.detail"></el-input>
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    cancle
                </span>
                <span class="edit-button" @click="addOperation()">
                    confirm cookbook
                </span>
            </span>
        </el-dialog>
        
        <h3> Diet History</h3>
        <ul>
            <li v-for="(item, index) in dietHistoryList" :key="index">
                <div style="font-size: 18px; font-weight: bold;">{{ getCookbookName(item) }}</div>
                <div style="margin-bottom: 10px;">{{ item.detail }} - {{ item.value }}g - Delete
                    <span @click="removeCookbook(item)" style="cursor: pointer;"><i class="el-icon-circle-close"></i></span>
                </div>
            </li>
        </ul>
    </div>
</template>
<script>
export default {
    name: "DietHistory",
    data() {
        return {
            dietHistory: {},
            dialogGourmetOperaion: false,
            cookbooks: [],
            dietHistoryList: [],
        }
    },
    created() {
        this.fetchCookbookList();
        this.fetchDietHistoryList();
    },
    methods: {
        removeCookbook(dietHistory) {
            this.$axios.post(`/dietHistory/batchDelete`, [dietHistory.id]).then(res => {  // item.id → dietHistory.id
                const { data } = res;
                if (data.code === 200) {
                    this.fetchDietHistoryList();
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        fetchDietHistoryList() {
            this.$axios.post(`/dietHistory/queryUser`, {}).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.dietHistoryList = data.data;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        getCookbookName(dietHistory) {
            const cookbook = this.cookbooks.find(entity => entity.id === dietHistory.cookbookId);
            return cookbook ? cookbook.name : 'Unknown';
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
        addOperation() {
            this.$axios.post(`/dietHistory/save`, this.dietHistory).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.fetchDietHistoryList();
                    this.cannel();
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        cannel() {
            this.dietHistory = {};
            this.dialogGourmetOperaion = false;
        },
        recordGourmet() {
            this.dialogGourmetOperaion = true;
        },

    }
};
</script>
<style scoped lang="scss">
.record {
    display: flex;
    justify-content: left;
    align-items: left;
    cursor: pointer;
}
.record:hover {
    background-color: rgb(200, 200, 200);
}
</style>
