<template>
    <el-row>
        <el-col :span="18"
            style="padding:10px 20px; background-color: white; border-right: 2px solid rgb(235, 235, 235);">
            <div style="position: sticky; top: 0px; background-color: white; z-index: 10; padding: 10px 0;">
                <div class="title"> {{ gourmet.title }}</div>
                <div class="info">
                    <span> {{ gourmet.createTime }}</span>
                    <span> Views({{ gourmet.viewCount }})</span>
                    <span> Likes({{ gourmet.upvoteCount }})</span>
                    <span> Saves({{ gourmet.saveCount }})</span>
                    <span> Rating({{ gourmet.rating }})</span>
                </div>
            </div>
            <div style="border-top: 2px solid rgb(235, 235, 235);">
                <div v-html="gourmet.content" ></div>
            </div>
            <div>
                <Evaluations :contentId="gourmet.id" contentType="GOURMET"/>
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
            recommendGourmet: []
        }
    },
    created() {
        this.loadGourmetId();
        this.fetchTopGourmet();
    },
    methods: {
        readGourmet(gourmet) {
            this.fetchGourmetById(gourmet.id);
        },
        loadGourmetId() {
            this.gourmetId = sessionStorage.getItem('gourmetId');
            this.fetchGourmetById(this.gourmetId);
        },
        // 根据ID获取美食做法详情
        fetchGourmetById(gourmetId) {
            this.$axios.get(`/gourmet/${gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet = data.data[0];
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
