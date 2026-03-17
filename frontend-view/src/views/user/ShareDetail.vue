<template>
    <div style="width:100%; height:100%; display:flex; justify-content: center; align-items: center; ">
        <div v-if="authStatus" style="width:250px; height:200px; border: 1px solid rgb(245,245,245)">
            <p>Authentication Info</p>
            <div>
                <input class="dialog-input" type="password" v-model="contentNetQueryDto.accessPassword"
                    placeholder="password" />
            </div>
            <div style="margin-block: 20px">
                <span class="edit-button" @click="shareOperation()">
                    confirm
                </span>
            </div>
        </div>
        <div v-else style="width: 800px; margin: 0 auto; ">
            {{ gourmetInfo }}
        </div>
    </div>
</template>
<script>
export default {
    name: "ShareDetail",
    data() {
        return {
            accessCode: null,
            authStatus: false,
            contentNetQueryDto: {},
            gourmetInfo: {}
        }
    },
    created() {
        this.accessCode = this.$route.query.accessCode;
        this.loadAuthStauts(this.accessCode);
    },
    methods: {
        shareOperation() {
            this.contentNetQueryDto.accessCode = this.accessCode;
            this.contentNetQueryDto.accessPassword = this.$md5(this.contentNetQueryDto.accessPassword);
            this.$axios.post("/contentNet/findContent", this.contentNetQueryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    const gourmetList = data.data;
                    if (Array.isArray(gourmetList) && gourmetList.length > 0) {
                        sessionStorage.setItem('gourmetId', gourmetList[0].id);
                        this.$router.push('/gourmetDetail');
                    } else {
                        this.$message.error(data.data); 
                    }
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        //通过访问码查询帖子认证状态
        loadAuthStauts(accessCode) {
            this.$axios.post("/contentNet/authStatus", { accessCode }).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.authStatus = data.data;
                    // 不需要密码验证，直接加载内容并跳转
                    if (!this.authStatus) {
                        this.contentNetQueryDto.accessCode = accessCode;
                        this.$axios.post("/contentNet/findContent", this.contentNetQueryDto).then(res => {
                            const { data } = res;
                            if (data.code === 200 && Array.isArray(data.data) && data.data.length > 0) {
                                sessionStorage.setItem('gourmetId', data.data[0].id);
                                this.$router.push('/gourmetDetail');
                            }
                        });
                    }
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
    }

};
</script>

<style scoped lang="scss"></style>
