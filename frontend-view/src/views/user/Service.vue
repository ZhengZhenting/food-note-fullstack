<template>
    <!-- Dashboard Page -->
    <div>
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
                                        <span style="color: rgb(0,200,95)" @click="share(gourmet)"> Share </span>
                                        <span style="color: rgb(0,95,200)" @click="edit(gourmet)"> Edit </span>
                                        <span style="color: rgb(250,95,0)" @click="del(gourmet)"> Delete </span>
                                    </div>
                                </div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="Cookbook" name="second">
                            <div v-if="cookbookList.length === 0">
                                <el-empty description="No data"></el-empty>
                            </div>
                            <div v-else>
                                <el-row>
                                    <el-col class="cookbook-item" style="margin-bottom: 10px" :span="6"
                                        v-for="(cookbook, index) in cookbookList" :key="index">
                                        <div style="padding: 10px">
                                            <div>
                                                <img style="width: 100%; height: 150px; border-radius: 5px;"
                                                    :src="cookbook.cover" alt="">
                                            </div>
                                            <div @click="readCookbookDetail(cookbook)"
                                                style="cursor: pointer; font-size: 20px; font-weight: 900;">
                                                {{ cookbook.title }}
                                            </div>
                                            <div style="font-size: 16px;">{{ cookbook.createTime }}</div>
                                        </div>
                                    </el-col>
                                </el-row>
                            </div>
                        </el-tab-pane>
                         <el-tab-pane label="Content" name="third">
                            <MyContentNet/>
                         </el-tab-pane>
                    </el-tabs>
                </div>
            </el-col>
            <!-- 操作面板 -->
            <el-dialog :visible.sync="dialogShareOperaion" width="25%">
                <div style="padding:16px 20px; text-align:left;">
                    <div v-if="url !== ''">
                        <el-result icon="success" title="success">
                            <template slot="extra">
                                <span>{{ url }}</span>
                            </template>
                        </el-result>
                    </div>
                    <div v-else>
                        <div>
                            <p>Valid Days:</p>
                            <el-radio-group size="mini" v-model="contentNet.validDay">
                                <el-radio-button label="3 Days"></el-radio-button>
                                <el-radio-button label="7 Days"></el-radio-button>
                                <el-radio-button label="30 Days"></el-radio-button>
                                <el-radio-button label="Always"></el-radio-button>
                            </el-radio-group>
                        </div>
                        <div>
                            <p>Password Authtification:</p>
                            <el-switch v-model="contentNet.passwordAuth" active-color="#13ce66"
                                inactive-color="#ff4949"></el-switch>
                        </div>
                        <div v-if="contentNet.passwordAuth">
                            <p>Set Password:</p>
                            <input class="dialog-input" type="password" v-model="contentNet.accessPassword"
                                placeholder="password" />
                        </div>
                    </div>

                </div>
                <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                    <div>
                        <span class="channel-button" @click="cannel()">
                            cancle
                        </span>
                    </div>
                    <div v-if="url === ''">
                        <span class="edit-button" @click="shareOperation()">
                            confirm
                        </span>
                    </div>
                </span>
            </el-dialog>
        </el-row>
    </div>
</template>
<script>
import MyContentNet from "@/views/user/MyContentNet.vue";

export default {
    components: {MyContentNet},
    name: "Service",
    data() {
        return {
            cookbookList: [],
            contentNet: {},
            gourmet: {},
            info: {},
            activeName: 'first',
            gourmetList: {},
            dialogShareOperaion: false,
            resultContentNet: {},
            url: '',//return sharing link
        }
    },
    created() {
        this.fetchCenter();
        this.fetchMyGourmet();
        this.fetchMyCookbook();
    },
    methods: {
        readCookbookDetail(cookbook) {
            sessionStorage.setItem('cookbookInfo', JSON.stringify(cookbook));
            this.$router.push('/cookbookDetail');
        },
        fetchMyCookbook() {
            this.$axios.post("/cookbook/queryUser", {
                current: 1,
                size: 100
            }).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.cookbookList = data.data;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        cannel() {
            this.dialogShareOperaion = false;
            this.url = '';
            this.contentNet = {};
        },
        shareOperation() {
            const dayList = [this.contentNet.validDay];
            const validDayList = dayList.map(text => {
                const match = text.match(/\d+/g);
                return match ? match : [];
            });
            if (this.contentNet.passwordAuth) {
                this.contentNet.accessPassword = this.$md5(this.contentNet.accessPassword);
            }
            const saveEntity = {
                gourmetId: this.gourmet.id,
                validDay: validDayList[0].length === 0 ? -1 : validDayList[0][0],
                passwordAuth: this.contentNet.passwordAuth,
                accessPassword: this.contentNet.accessPassword
            }
            this.$axios.post("/contentNet/save", saveEntity).then(res => {
                const { data } = res;
                if (data.code === 200) {

                    this.$notify({
                        duration: 1000,
                        title: 'Share Operation',
                        message: 'success',
                        type: 'success'
                    });
                    this.url = data.msg;
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        // 分享gourmet
        share(gourmet) {
            this.gourmet = gourmet;
            this.dialogShareOperaion = true;
        },
        // 修改gourmet
        edit(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
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
.cookbook-item:hover {
    background-color: rgb(248, 248, 248);
}

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
