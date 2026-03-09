<template>
    <!-- Dashboard Page -->
    <div class="text">
        <el-row style="background-color: rgb(255,255,255); padding: 20px">
            <el-col :span="24">
                <div class="info">
                    <div>
                        <img :src="info.userAvatar" alt="">
                    </div>
                    <div>
                        <div class="title">{{ info.userName }}</div>
                        <div class="point-info">
                            <span>Views: {{ info.viewCount }}</span>
                            <span>Likes: {{ info.upvoteCount }}</span>
                            <span>Saves: {{ info.saveCount }}</span>
                        </div>
                    </div>
                </div>
                <div style="margin-inline: 20px;">
                    <el-tabs v-model="activeName" @tab-click="handleClick">
                        <el-tab-pane label="Gourmet" name="first">
                            <div class="item-gourmet" v-for="(gourmet, index) in gourmetList" :key="index">
                                <div class="left">
                                    <img :src="gourmet.cover">
                                </div>
                                <div class="right">
                                    <div class="info">
                                        <img style="width: 25px; height: 25px; border-radius: 50%;"
                                            :src="gourmet.userAvatar">
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
                                        <span style="color: rgb(0,95,200)" @click="edit(gourmet)"> Modify </span>
                                        <span style="color: rgb(250,95,0)" @click="del(gourmet)"> Delete </span>
                                    </div>
                                </div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="Cookbook" name="second">Cookbook</el-tab-pane>
                    </el-tabs>
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
export default {
    name: "Service",
    data() {
        return {
            info: {},
            activeName: 'first',
            gourmetList: {}
        }
    },
    created() {
        this.fetchCenter();
        this.fetchMyGourmet();
    },
    methods: {
        // 修改gourmet
        edit(gourmet) {
            sessionStorage.setItem('gourmetId',gourmet.id);
            this.$router.push('/editGourmet');
        },
        // 删除gourmet
        async del(gourmet) {
            const confirmed = await this.$swalConfirm({
                title: 'delete confirmation',
                text: `operation unrecoverable, continue？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = [gourmet.id];
                    const response = await this.$axios.post(`/gourmet/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$notify({
                            duration: 1000,
                            title: 'Delete Operation',
                            message: 'success',
                            type: 'success'
                        });
                        this.fetchMyGourmet();
                        return;
                    }
                } catch (error) {
                    this.$notify.error({
                        duration: 1000,
                        title: 'Error',
                        message: error
                    });
                    console.error(`Error：`, error);
                }
            }
        },
        readGourmet(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
            this.$router.push('/gourmetDetail'); // 跳转到美食做法详情页
        },
        handleClick(tab, event) {
            console.log(tab, event);
        },
        fetchMyGourmet() {
            this.$axios.get("/gourmet/queryUser").then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmetList = data.data;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        fetchCenter() {
            this.$axios.get("/user/center").then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.info = data.data;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        }
    }
};
</script>

<style scoped lang="scss">
.info {
    display: flex;
    justify-content: left;
    padding: 20px;
    gap: 20px;

    img {
        width: 100px;
        height: 100px;
        border-radius: 50%;
    }

    .title {
        font-size: 24px;
        font-weight: bold;
        text-align: left;

    }

    .point-info {
        margin-top: 10px;
        font-size: 16px;
        color: #666;
        justify-content: left;

        span {
            margin-right: 20px;
        }
    }
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
