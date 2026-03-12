<template>
    <el-row>
        <el-col :span="18"
            style="padding:10px 20px; background-color: white; border-right: 2px solid rgb(235, 235, 235);">
            <div style="position: sticky; top: 0px; background-color: white; z-index: 10; padding: 10px 0;">
                <div class="title"> {{ gourmet.title }}</div>
                <div class="info">
                    <img style="width: 20px; height: 20px; border-radius: 50%;" :src="gourmet.userAvatar">
                    <span>{{ gourmet.userName }}</span>
                    <span> {{ gourmet.createTime }}</span>
                    <span> Views({{ gourmet.viewCount }})</span>
                    <span> Likes({{ gourmet.upvoteCount }})</span>
                    <span> Saves({{ gourmet.saveCount }})</span>
                    <span> Rating({{ gourmet.rating }})</span>
                </div>
            </div>
            <div style="border-top: 2px solid rgb(235, 235, 235);">
                <div v-html="gourmet.content"></div>
            </div>
            <div style="margin: 40px 0; display: flex; justify-content: center; align-items: center;">
                <span class="upvote-operation">
                    <el-tooltip class="item" effect="dark" :content="upvoteStatus ? 'cancle' : 'upvote'"
                        placement="bottom">
                        <span @click="operationUpvote()">
                            <i style="font-size: 30px" class="el-icon-medal-1"></i>
                            <span>
                                {{ gourmet.upvoteCount }} likes
                            </span>
                        </span>
                    </el-tooltip>
                </span>
                <span class="upvote-operation">
                    <el-tooltip class="item" effect="dark" :content="saveStatus ? 'cancle' : 'upvote'"
                        placement="bottom">
                        <span @click="operationSave()">
                            <i style="font-size: 30px" class="el-icon-trophy"></i>
                            <span>
                                {{ gourmet.saveCount }} saves
                            </span>
                        </span>
                    </el-tooltip>
                </span>
                <span class="upvote-operation">
                    <span>
                        <i style="font-size: 30px" class="el-icon-star-off"></i>
                        <span>
                            {{ gourmet.rating }} points
                        </span>
                    </span>
                </span>
            </div>
            <div>
                <div style="text-align: center; display: flex; align-items: center; justify-content: center; gap: 8px;">
                    
                    <div v-if="!ratingStatus">
                        <h4 style="margin: 0;">Your Rating: </h4>
                        <el-rate @change="ratingEvent" v-model="ratingScore"
                            :texts="['oops', 'disappointed', 'normal', 'good', 'great']" show-text>
                        </el-rate>
                    </div>
                    <div v-else>
        <h4 style="margin: 0;">You have already rated this article </h4>
    </div>
                </div>
            </div>
            <div>
                <Evaluations v-if="gourmet.id" :contentId="gourmet.id" contentType="GOURMET" />
            </div>
        </el-col>

        <el-col :span="6" style="padding:10px 20px; background-color: rgb(245, 245, 245);">
            <h4 style="margin-top: 10px;">
                Recommendations
                <div class="item-gourmet" v-for="(gourmet, index) in recommendGourmet" :key="index">
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
                            <span> Views({{ gourmet.viewCount }})</span>
                            <span> Likes({{ gourmet.upvoteCount }})</span>
                            <span> Saves({{ gourmet.saveCount }})</span>
                            <span> Rating({{ gourmet.rating }})</span>
                        </div>
                    </div>
                </div>
            </h4>
        </el-col>
    </el-row>
</template>
<script>

import Evaluations from "@/components/Evaluations"
export default {
    components: {
        Evaluations
    },
    name: "GourmetDetail",
    data() {
        return {
            gourmetId: null,
            gourmet: {},
            ratingScore: 0,
            recommendGourmet: [],
            upvoteStatus: false, // deault: not upvoted
            saveStatus: false, // default: not saved
            ratingStatus: false
        }
    },
    created() {
        this.loadGourmetId();
        this.fetchTopGourmet();
    },
    methods: {
        //用户评分时触发，给与评分
        ratingEvent() {
            this.$axios.post(`/interaction/ratingOperation/${this.gourmetId}/${this.ratingScore}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet = data.data[0];
                    this.dealRating(this.gourmet);
                    this.$notify({
                        duration: 1000,
                        title:'rating',
                        message:'rated successfully',
                        type:'success'
                    });
                    this.ratingStatus=true;
                }else{
                    this.$message(data.msg);
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        // 查询评分状态（是否已经评过分）
        fetchRatingOperation(contentId) {
            this.$axios.get(`/interaction/ratingStatus/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.ratingStatus = data.data > 0;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        // 获取点赞状态
        fetchUpvoteOperation(contentId) {
            this.$axios.get(`/interaction/upvoteStatus/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.upvoteStatus = data.data > 0;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        // 获取收藏状态
        fetchSaveOperation(contentId) {
            this.$axios.get(`/interaction/saveStatus/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.saveStatus = data.data > 0;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        // 收藏操作
        operationSave() {
            this.$axios.post(`/interaction/saveOperation/${this.gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    if (data.data > 0) {
                        this.gourmet.saveCount += 1;
                    } else {
                        this.gourmet.saveCount -= 1;
                    }
                    this.saveStatus = data.data > 0;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        // 点赞操作
        operationUpvote() {
            this.$axios.post(`/interaction/upvoteOperation/${this.gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    if (data.data > 0) {
                        this.gourmet.upvoteCount += 1;
                    } else {
                        this.gourmet.upvoteCount -= 1;
                    }
                    this.upvoteStatus = data.data > 0;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        readGourmet(gourmet) {
            this.fetchGourmetById(gourmet.id);
        },
        loadGourmetId() {
            this.gourmetId = sessionStorage.getItem('gourmetId');
            // 根据ID获取美食做法详情
            this.fetchGourmetById(this.gourmetId);
            // 浏览操作
            this.viewOeration(this.gourmetId);
            // 获取点赞状态
            this.fetchUpvoteOperation(this.gourmetId);
            // 获取收藏状态
            this.fetchSaveOperation(this.gourmetId);
            // 获取评分状态
            this.fetchRatingOperation(this.gourmetId);
        },
        // 根据ID获取美食做法详情
        fetchGourmetById(gourmetId) {
            this.$axios.get(`/gourmet/${gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet = data.data[0];
                    this.dealRating(this.gourmet);
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        dealRating(gourmet) {
            this.gourmet.rating = gourmet.rating === null ? 0 : gourmet.rating;
        },
        //浏览操作
        viewOeration(contentId) {
            this.$axios.post(`/interaction/viewOperation/${contentId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    console.log("View operation recorded successfully");
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        // TODO：后续根据算法推荐相关美食做法
        fetchTopGourmet() {
            this.$axios.post(`/gourmet/queryList`, { size: 3, current: 1 }).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.recommendGourmet = data.data;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        }
    }
};
</script>
<style scoped lang="scss">
.upvote-operation {
    font-weight: 800;
    cursor: pointer;
    display: inline-block;
    padding: 5px 10px;
    border-radius: 5px;
}

.upvote-operation:hover {
    background-color: #f1f1f1;
}

.title {
    font-size: 30px;
    font-weight: bold;

}

.info {
    margin-top: 5px;
    font-size: 15px;
    color: #333232;
    display: flex;
    justify-content: left;
    gap: 10px;
}


.item-gourmet {
    display: flex;
    justify-content: left;
    gap: 10px;
    border-radius: 5px;
    cursor: pointer;

    .left {
        img {
            width: 100px;
            border-radius: 5px;
            height: 80px;
            margin-bottom: 10px;
        }

    }

    .right {
        text-align: left;

        .title:hover {
            text-decoration: underline;
        }

        .title {
            font-size: 16px;
            font-weight: bold;
            margin: 5px;
            gap: 10px;
            width: 140px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;

        }

        .detail {
            margin-top: 10px;
            font-size: 12px;
            color: #333232;
            display: flex;
            justify-content: left;
            gap: 10px;
        }

        .info {
            display: flex;
            justify-content: left;
            align-items: center;
            gap: 5px;
            font-size: 12px;
        }
    }
}
</style>
