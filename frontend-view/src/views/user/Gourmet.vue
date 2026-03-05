<template>
    <div class="text">
        <div>
            <div style="height: 200px; background-color: #f0f0f0; text-align: center;">
                <h2>recommendationes //TODO</h2>
            </div>
            <div style="text-align: left; display: block; margin: 20px 0;">
                <span style="font-size: 24px; font-weight: bold;"> Search </span>
                <el-input size="small" style="width: 166px;" v-model="gourmetQueryDto.title" placeholder="title"
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
            <div v-if="gourmetList.length === 0">
                <el-empty description="No data"></el-empty>
            </div>
            <div v-else>
                <div class="item-gourmet" v-for="(gourmet, index) in gourmetList" :key="index">
                    <div class="left">
                        <img :src="gourmet.cover">
                    </div>
                    <div class="right">
                        <div class="info">
                            <img style="width: 25px; height: 25px; border-radius: 50%;" :src="gourmet.userAvatar">
                            <span>{{ gourmet.userName }}</span>
                        </div>
                        <div class="title" @click="readGourmet(gourmet)">
                            {{ gourmet.title }}
                        </div>
                        <div class="detail">
                            {{ gourmet.detail }}
                        </div>
                        <div class="detail">
                            <span> {{ gourmet.createTime }}</span>
                            <span> Views({{ gourmet.viewCount }})</span>
                            <span> Likes({{ gourmet.upvoteCount }})</span>
                            <span> Saves({{ gourmet.saveCount }})</span>
                            <span> Rating({{ gourmet.rating }})</span>
                        </div>
                    </div>

                </div>
            </div>
            <el-pagination style="margin:10px 0;float: right;" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[10, 20]"
                :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"></el-pagination>
        </div>

    </div>
</template>
<script>
export default {
    name: "Gourmet",
    data() {
        return {
            gourmetQueryDto: {},
            gourmetList: [],
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
        readGourmet(gourmet){
            sessionStorage.setItem('gourmetId',gourmet.id);
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
            this.gourmetQueryDto.categoryId = category.id;
            this.fetchGourmetData();
        },
        // 获取美食分类
        fetchCategoryData() {
            this.$axios.post('category/query', {}).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.categories = res.data.data;
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
                ...this.gourmetQueryDto
            };
            this.$axios.post('gourmet/queryList', queryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmetList = res.data.data;
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
