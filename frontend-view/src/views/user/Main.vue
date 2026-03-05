<template>
    <div>
        <div class="top">
            <div class="top-left">
                <Logo sysName="美食天下" />
            </div>
            <div class="top-right">
                <ul>
                    <li @click="route('/gourmet')">
                        <i class="el-icon-food" style="margin-right: 4px;"></i>
                        天下美食
                    </li>
                    <li @click="route('/cookbook')">
                        <i class="el-icon-notebook-2" style="margin-right: 4px;"></i>
                        食谱大全
                    </li>
                    <li @click="route('/Save')">
                        <i class="el-icon-star-off" style="margin-right: 4px;"></i>
                        我的收藏
                    </li>
                    <li>
                        <el-dropdown type="success" size="mini" :hide-on-click="false">
                            <span class="el-dropdown-link">
                                我的饮食<i class="el-icon-arrow-down el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item @click.native="route('/dietHistory')">饮食历史</el-dropdown-item>
                                <el-dropdown-item @click.native="route('/nutriment')">营养摄入</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </li>
                    <li>
                        <el-dropdown type="success" size="mini" :hide-on-click="false">
                            <span class="el-dropdown-link">
                                Create<i class="el-icon-arrow-down el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item @click.native="route('/createGourmet')">add Gourmet</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </li>
                </ul>
                <el-button type="primary" style="margin-right: 15px;" size="mini" @click="route('/service')" round>内容中心</el-button>
                <el-dropdown type="success" size="mini" class="user-dropdown">
                    <span class="el-dropdown-link" style="display: flex; align-items: center;cursor: pointer;">
                        <el-avatar :size="30" :src="userInfo.userAvatar" style="margin-top: 0;"></el-avatar>
                        <span class="user-name" style="margin-left: 5px;font-size: 14px;">{{ userInfo.userName }}</span>
                        <i class="el-icon-arrow-down el-icon--right" style="margin-left: 5px;"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item @click.native="route('/self')">个人资料</el-dropdown-item>
                        <el-dropdown-item @click.native="route('/resetPwd')">修改密码</el-dropdown-item>
                        <el-dropdown-item @click.native="loginOut">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>
        <div class="router-view">
            <div class="item">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>
<script>
import Logo from "@/components/Logo"
export default {
    components: { Logo },
    name: "User",
    data() {
        return {
            key: '',
            defaultPath: '/gourmet',
            userInfo: {}
        }
    },
    created() {
        this.auth();
        // 默认加载景点页
        //this.route(this.defaultPath);
    },
    methods: {
        // 路由跳转
        route(path) {
            if (this.$route.path !== path) {
                this.$router.push(path);
            };
        },
        // 退出登录
        async loginOut() {
            const confirmed = await this.$swalConfirm({
                title: '退出登录',
                text: `退出后需要重新登录哦？`,
                icon: 'warning',
            });
            if (confirmed) {
                sessionStorage.setItem('token', null);
                this.$router.push('/');
            }
        },
        // Token 检验
        async auth() {
            const { data } = await this.$axios.get('/user/auth');
            if (data.code !== 200) { // Token校验异常
                this.$router.push('/');
            } else {
                this.userInfo = data.data;
                // 将用户信息存储在 sessionStorage 中，供其他组件使用
                sessionStorage.setItem('userInfo', JSON.stringify(this.userInfo));
            }
        },
    }
};
</script>
<style scoped lang="scss">
.top {
    height: 65px;
    line-height: 65px;
    padding: 20px 100px;
    box-sizing: border-box;
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid rgb(231, 231, 231);

    .top-right {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 6px;

        ul {
            list-style: none;

            li {
                cursor: pointer;
                float: left;
                margin-right: 6px;
                padding: 5px 10px;

                min-width: 50px;
                max-width: 100px;
                color: rgb(111, 53, 71);
                font-size: 14px;
                box-sizing: border-box;
                font-weight: 500;
            }

            li:hover {
                color: rgb(25, 70, 160);
            }
        }
    }

    .top-left {
        display: flex;
        justify-content: center;
        align-items: center;
    }
}

.router-view {
    padding: 10px 30px;
    box-sizing: border-box;
    background-color: rgb(245, 245, 245);
    min-height: calc(100vh - 66px);

    .item {
        width: 70%;
        margin: 0 auto;
    }
}
</style>
