<template>
    <div class="admin-container">

        <!-- ── Sidebar ── -->
        <div class="menu-side" :class="{ 'menu-side-narrow': flag }">
            <!-- Brand -->
            <div class="brand-wrap">
                <img src="/logo.png" class="brand-logo" />
                <span v-if="!flag" class="brand-name">Food Note</span>
            </div>

            <!-- Navigation menu -->
            <div class="menu-wrap">
                <AdminMenu :flag="flag" :routes="adminRoutes" :bag="bagMenu" @select="handleRouteSelect" />
            </div>
        </div>

        <!-- ── Main area ── -->
        <div class="main">
            <div class="header-section">
                <LevelHeader
                    @eventListener="eventListener"
                    @selectOperation="selectOperation"
                    :tag="tag"
                    :userInfo="userInfo"
                />
            </div>
            <div class="content-section">
                <router-view></router-view>
            </div>
        </div>

        <!-- ── Profile dialog ── -->
        <el-dialog
            :show-close="false"
            :visible.sync="dialogOperaion"
            width="28%"
            custom-class="food-dialog"
        >
            <div class="dialog-body">
                <h3 class="dialog-title">Edit Profile</h3>

                <div class="dialog-section">
                    <label class="dialog-label">Profile Picture</label>
                    <el-upload
                        class="avatar-uploader"
                        action="http://localhost:21090/api/food-share-sys/v1.0/file/upload"
                        :show-file-list="false"
                        :on-success="handleAvatarSuccess"
                    >
                        <img v-if="userInfo.url" :src="userInfo.url" class="avatar-preview" />
                        <div v-else class="avatar-placeholder">
                            <i class="el-icon-plus"></i>
                            <span>Upload</span>
                        </div>
                    </el-upload>
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">User Name <span class="required">*</span></label>
                    <input class="dialog-input" v-model="userInfo.name" placeholder="Enter username" />
                </div>

                <div class="dialog-section">
                    <label class="dialog-label">Email <span class="required">*</span></label>
                    <input class="dialog-input" v-model="userInfo.email" placeholder="Enter email" />
                </div>
            </div>

            <span slot="footer" class="dialog-footer">
                <span class="cancel-btn" @click="dialogOperaion = false">Cancel</span>
                <span class="confirm-btn" @click="updateUserInfo()">Save Changes</span>
            </span>
        </el-dialog>

    </div>
</template>

<script>
import request from "@/utils/request.js";
import router from "@/router/index";
import { clearToken } from "@/utils/storage"
import AdminMenu from '@/components/VerticalMenu.vue';
import Logo from '@/components/Logo.vue';
import LevelHeader from '@/components/LevelHeader.vue';
export default {
    name: "Admin",
    components: { Logo, LevelHeader, AdminMenu },
    data() {
        return {
            adminRoutes: [],
            activeIndex: '',
            userInfo: { id: null, url: '', name: '', role: null, email: '' },
            flag: false,
            tag: 'Visualization',
            bag: '#ffffff',
            colorLogo: '#333333',
            bagMenu: '#fdfaf5',
            dialogOperaion: false
        };
    },
    created() {
        const menus = router.options.routes.filter(r => r.path === '/admin')[0];
        this.adminRoutes = menus.children;
        this.tokenCheckLoad();
        this.menuOperationHistory();
    },
    methods: {
        async updateUserInfo() {
            try {
                const dto = { userAvatar: this.userInfo.url, userName: this.userInfo.name, userEmail: this.userInfo.email };
                const { data } = await this.$axios.put('/user/update', dto);
                if (data.code === 200) {
                    this.dialogOperaion = false;
                    this.tokenCheckLoad();
                    this.$swal.fire({ title: 'Edit Profile', text: data.msg, icon: 'success', showConfirmButton: false, timer: 1000 });
                }
            } catch (e) {
                this.dialogOperaion = false;
                this.$swal.fire({ title: 'Error', text: e, icon: 'error', showConfirmButton: false, timer: 2000 });
            }
        },
        handleAvatarSuccess(res) {
            if (res.code !== 200) { this.$message.error('Upload failed'); return; }
            this.$message.success('Upload successful');
            this.userInfo.url = res.data;
        },
        eventListener(event) {
            if (event === 'center')   this.dialogOperaion = true;
            if (event === 'loginOut') this.loginOut();
        },
        async loginOut() {
            const confirmed = await this.$swalConfirm({ title: 'Log out?', text: 'You will need to log in again', icon: 'warning' });
            if (confirmed) {
                this.$swal.fire({ title: 'Logged out', text: 'Redirecting…', icon: 'success', showConfirmButton: false, timer: 1000 });
                setTimeout(() => { clearToken(); this.$router.push('/login'); }, 1000);
            }
        },
        menuOperationHistory() { this.flag = sessionStorage.getItem('flag') === 'true'; },
        selectOperation(flag)  { this.flag = flag; },
        handleRouteSelect(index) {
            const match = this.adminRoutes.filter(e => e.path === index);
            if (match.length) this.tag = match[0].name;
            if (this.$router.currentRoute.fullPath !== index) this.$router.push(index);
        },
        async tokenCheckLoad() {
            try {
                const res = await request.get('user/auth');
                if (res.data.code === 400) { this.$message.error(res.data.msg); this.$router.push('/login'); return; }
                const { id, userAvatar: url, userName: name, userRole: role, userEmail: email } = res.data.data;
                this.userInfo = { id, url, name, role, email };
                const rolePath = role === 1 ? '/admin' : '/user';
                const targetMenu = router.options.routes.find(r => r.path === rolePath);
                if (targetMenu) this.routers = targetMenu.children;
            } catch (error) {
                console.error('Error:', error);
                this.$message.error('Error, please try later!');
            }
        },
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

/* ─── Overall shell ──────────────────────────────────── */
.admin-container {
    display: flex;
    height: 100vh;
    width: 100%;
    background-color: #f5f0e8;
    background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.04'/%3E%3C/svg%3E");
    font-family: 'DM Sans', sans-serif;
}

/* ─── Sidebar ────────────────────────────────────────── */
.menu-side {
    width: 240px;
    min-width: 240px;
    height: 100vh;
    background-color:#fdfaf5;
    border-right: 1.5px solid #e8ddd0;
    display: flex;
    flex-direction: column;
    transition: width 0.3s ease, min-width 0.3s ease;
    overflow: hidden;
    flex-shrink: 0;
}

.menu-side-narrow {
    width: 64px;
    min-width: 64px;
}

/* Brand section */
.brand-wrap {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 18px 16px 10px;
    flex-shrink: 0;
}

.brand-logo {
    width: 30px;
    height: 30px;
    flex-shrink: 0;
    object-fit: contain;
}

.brand-name {
    font-family: 'Caveat', cursive;
    font-weight: 700;
    font-size: 22px;
    color: #c8392b;
    white-space: nowrap;
    transform: rotate(-1deg);
    display: inline-block;
    line-height: 1;
}

.menu-wrap {
    flex: 1;
    overflow-y: auto;
    overflow-x: hidden;
    padding: 4px 0;
}

/* ─── Main area ──────────────────────────────────────── */
.main {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    min-width: 0;
}

.header-section {
    flex-shrink: 0;
}

.content-section {
    flex: 1;
    overflow-y: auto;
    padding: 0;
}

/* ─── Profile dialog ─────────────────────────────────── */
::v-deep .food-dialog {
    border-radius: 4px;
    border: 1.5px solid #e8ddd0;

    .el-dialog__header { display: none; }
    .el-dialog__body   { padding: 0; }
    .el-dialog__footer { border-top: 1.5px solid #e8ddd0; padding: 14px 20px; }
}

.dialog-body {
    padding: 24px 24px 8px;
    background-color: #fdfaf5;
}

.dialog-title {
    font-family: 'Klee One', cursive;
    font-size: 20px;
    font-weight: 600;
    color: #c8392b;
    margin: 0 0 20px 0;
}

.dialog-section { margin-bottom: 18px; }

.dialog-label {
    display: block;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #5a5045;
    margin-bottom: 7px;
}

.required { color: #c8392b; margin-left: 2px; }

.avatar-uploader {
    ::v-deep .el-upload {
        border: 1.5px dashed #d6c9b8;
        border-radius: 50%;
        cursor: pointer;
        overflow: hidden;
        transition: border-color 0.15s;
        display: block;
        &:hover { border-color: #c8392b; }
    }
}

.avatar-preview {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    object-fit: cover;
    display: block;
}

.avatar-placeholder {
    width: 80px;
    height: 80px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 4px;
    color: #b0a898;
    font-family: 'Klee One', cursive;
    font-size: 11px;

    i { font-size: 22px; }
}

.dialog-input {
    width: 100%;
    height: 40px;
    padding: 0 12px;
    font-family: 'DM Sans', sans-serif;
    font-size: 14px;
    color: #2a2018;
    background-color: #ffffff;
    border: 1.5px solid #d6c9b8;
    border-radius: 4px;
    transition: border-color 0.2s;

    &::placeholder { color: #c0b09e; }
    &:focus { outline: none; border-color: #c8392b; }
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
}

.cancel-btn {
    display: inline-block;
    padding: 7px 18px;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #8a7d6e;
    border: 1.5px solid #d6c9b8;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.15s;
    &:hover { background-color: #f5f0e8; }
}

.confirm-btn {
    display: inline-block;
    padding: 7px 18px;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #fdf8f2;
    background-color: #c8392b;
    border: 1.5px solid #c8392b;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.15s;
    &:hover { background-color: #b03226; }
}
</style>