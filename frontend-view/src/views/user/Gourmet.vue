<template>
    <div class="text">
        <div>
            <div style="height: 200px; background-color: #f0f0f0; text-align: center;">
                <h2>recommendationes //TODO</h2>
            </div>
            <div style="margin-block: 20px;">
                <span :style="{
                    backgroundColor: categoryClick.id === category.id ? '#0d559e' : '',
                    color: categoryClick.id === category.id ? 'rgb(245,245,245)' : ''
                }"
                    @click="categorySelected(category)" class="item-category" v-for="(category, index) in categories"
                    :key="index">
                    {{ category.name }}
                </span>
            </div>
            <div class="item-gourmet" v-for="(gourmet, index) in gourmetList" :key="index">
                <div class="left">
                    <img :src="gourmet.cover">
                </div>
                <div class="right">
                    <div  class="info">
                        <img style="width: 25px; height: 25px; border-radius: 50%;" :src="gourmet.userAvatar">
                        <span>{{ gourmet.userName }}</span>
                    </div>
                    <div  class="title">
                        {{ gourmet.title }}
                    </div>
                    <div class="detail"> 
                        {{ gourmet.detail }}
                    </div>
                    <div class="detail"> 
                        <span> {{ gourmet.createTime }}</span>
                    </div>
                </div>

            </div>
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
            categoryClick: { is: null, name: 'All' }
        }
    },
    created() {
        this.fetchCategoryData();
        this.fetchGourmetData();
    },
    methods: {
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
            this.$axios.post('gourmet/queryList', this.gourmetQueryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmetList = res.data.data;

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

.item-gourmet:hover{
    background-color: rgb(245,245,245);
}

.item-gourmet {
    display: flex;
    justify-content: left;
    gap: 10px;
    padding: 10px;
    border-radius: 5px;

    .left {
        img{
            width:220px;
            border-radius: 5px;
            height: 150px;
        }

    }
    .right{
        text-align: left; 
        .title{
            padding: 4px 6px;
            font-size: 20px;
            font-weight: bold;
        }
        .detail{
            margin-top: 10px;
            padding: 4px 6px;
            font-size: 15px;
            color: #333232;
        }
        .info{
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
