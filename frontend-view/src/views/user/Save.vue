<template>
    <div>
        <div>
            <div v-if="collectionList.length === 0">
                <el-empty description="No data"></el-empty>
            </div>
            <div v-else>
                <h2>My Collection: {{ collectionList.length }} Articles</h2>
                <div class="item-gourmet" v-for="(gourmet, index) in collectionList" :key="index">
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
        </div>

    </div>
</template>
<script>

export default {
    name: "Save",
    data() {
        return {
            collectionList: [],
        }
    },
    created() {
        this.fetchCollectionData();
    },
    methods: {
        readGourmet(gourmet){
            sessionStorage.setItem('gourmetId',gourmet.id);
            this.$router.push('/gourmetDetail'); // 跳转到美食做法详情页
        },
        handleFilterClear() {
            this.filterText = '';
            this.fetchCollectionData();
        },
        // 获取我收藏的美食做法
        fetchCollectionData() {
            this.$axios.post('interaction/queryCollectionList', {}).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.collectionList = res.data.data;
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
