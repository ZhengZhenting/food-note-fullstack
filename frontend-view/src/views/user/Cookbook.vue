<template>
    <div class="text">
        <div>
            <div style="text-align: left; display: block; margin: 20px 0;">
                <span style="font-size: 24px; font-weight: bold;"> Search </span>
                <el-input size="small" style="width: 166px;" v-model="cookbookQueryDto.title" placeholder="title"
                    clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="fetchGourmetData" icon="el-icon-search"></el-button>
                </el-input>
            </div>
            <div style="margin-block: 20px; text-align: left; display: block;">
                <span :style="{
                    backgroundColor: categoryClick.id === category.id ? '#0d559e' : '',
                    color: categoryClick.id === category.id ? 'rgb(245,245,245)' : ''
                }" @click="categorySelected(category)" class="item-category" v-for="(category, index) in categories"
                    :key="index">
                    {{ category.name }}
                </span>
            </div>
            <div v-if="cookbookList.length === 0">
                <el-empty description="No data"></el-empty>
            </div>
            <div v-else>
                <el-row>
                    <el-col class="cookbook-item" style="margin-bottom: 10px" :span="6"
                        v-for="(cookbook, index) in cookbookList" :key="index">
                        <div style="padding: 10px">
                            <div>
                                <img style="width: 100%; height: 150px; border-radius: 5px;" :src="cookbook.cover"
                                    alt="">
                            </div>
                            <div @click="readDetail(cookbook)" style="cursor: pointer; font-size: 20px; font-weight: 900;">
                                {{cookbook.title }}</div>
                            <div style="font-size: 16px;">{{ cookbook.createTime }}</div>
                        </div>
                    </el-col>
                </el-row>
            </div>
            <el-pagination style="margin:10px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[10, 20]" :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="totalItems"></el-pagination>
        </div>
    </div>
</template>
<script>

export default {
    name: "Gourmet",
    data() {
        return {
            cookbookQueryDto: {},
            cookbookList: [],
            categories: [],
            filterText: '',
            currentPage: 1,
            pageSize: 10,
            totalItems: null,
            categoryClick: { id: null, name: 'All' }
        }
    },
    created() {
        this.fetchCategoryData();
        this.fetchGourmetData();
    },
    methods: {
        readDetail(cookbook) {
            sessionStorage.setItem('cookbookInfo', JSON.stringify(cookbook));
            this.$router.push('/cookbookDetail');
        },

        readGourmet(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
            this.$router.push('/gourmetDetail'); // 跳转到美食做法详情页
        },
        handleFilterClear() {
            this.filterText = '';
            this.fetchGourmetData();
        },
        // 页面大小改变
        handleSizeChange(val) {
            this.pageSize = val;
            this.currentPage = 1;
            this.fetchGourmetData();
        },
        // 当前页切换
        handleCurrentChange(val) {
            this.currentPage = val;
            this.fetchGourmetData();
        },
        // 选择分类
        categorySelected(category) {
            this.categoryClick = category;
            this.cookbookQueryDto.categoryId = category.id;
            this.fetchGourmetData();
        },
        // 获取美食分类
        fetchCategoryData() {
            this.$axios.post('category/query', {}).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.categories = data.data;
                    this.categories.unshift(this.categoryClick);
                }
            }).catch(error => {
                console.log(error);
            });
        },

        // 获取美食做法(既公开又审核通过的)
        fetchGourmetData() {
            // 请求参数
            const queryDto = {
                current: this.currentPage,
                size: this.pageSize,
                key: this.filterText,
                ...this.cookbookQueryDto
            };
            this.$axios.post('/cookbook/queryPublish', queryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.cookbookList = data.data;
                    this.totalItems = data.total;
                }
            }).catch(error => {
                console.log(error);
            });
        }
    }
};
</script>


<style scoped lang="scss">
.cookbook-item:hover {
    background-color: rgb(248, 248, 248);
}

.item-category {
    display: inline-block;
    padding: 4px 12px;
    margin-right: 8px;
    background-color: aliceblue;
    border-radius: 5px;
    font-size: 18px;
    cursor: pointer;
}

.item-gourmet:hover {
    background-color: rgb(245, 245, 245);
}

.item-gourmet {
    display: flex;
    justify-content: left;
    gap: 10px;
    padding: 10px;
    border-radius: 5px;
    cursor: pointer;

    .left {
        img {
            width: 220px;
            border-radius: 5px;
            height: 150px;
        }

    }

    .right {
        text-align: left;

        .title:hover {
            text-decoration: underline;
        }

        .title {
            padding: 4px 6px;
            font-size: 20px;
            font-weight: bold;

        }

        .detail {
            margin-top: 10px;
            padding: 4px 6px;
            font-size: 15px;
            color: #333232;
            display: flex;
            justify-content: left;
            gap: 10px;
        }

        .info {
            padding: 4px 6px;
            display: flex;
            justify-content: left;
            align-items: center;
            gap: 5px;
            font-size: 15px;
        }
    }
}
</style>
