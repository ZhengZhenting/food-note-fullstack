<template>
    <div class="self-container">

        <div class="page-header">
            <h2 class="page-title">My Profile</h2>
        </div>

        <div class="form-body">

            <!-- Avatar -->
            <div class="form-section form-row">
                <label class="form-label">Avatar <span class="required">*</span></label>
                <el-upload
                    class="avatar-uploader"
                    action="http://localhost:21090/api/food-share-sys/v1.0/file/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                >
                    <img v-if="userAvatar" :src="userAvatar" class="avatar-preview" />
                    <div v-else class="avatar-placeholder">
                        <i class="el-icon-plus"></i>
                        <span>Upload</span>
                    </div>
                </el-upload>
            </div>

            <!-- Username -->
            <div class="form-section">
                <label class="form-label">Username <span class="required">*</span></label>
                <input class="form-input" v-model="userInfo.userName" placeholder="Enter your username" />
            </div>

            <!-- Email -->
            <div class="form-section">
                <label class="form-label">Email <span class="required">*</span></label>
                <input class="form-input" v-model="userInfo.userEmail" placeholder="Enter your email" />
            </div>

            <!-- Account status -->
            <div class="form-section">
                <label class="form-label">Account Status</label>
                <div class="status-row">
                    <div class="status-item">
                        <span class="status-name">
                            Login Access
                            <el-tooltip effect="dark" content="Banned accounts cannot log in or use system features" placement="right">
                                <i class="el-icon-info status-info"></i>
                            </el-tooltip>
                        </span>
                        <span v-if="!userInfo.isLogin" class="status-badge ok">
                            <i class="el-icon-circle-check"></i> Normal
                        </span>
                        <span v-else class="status-badge banned">
                            <i class="el-icon-warning-outline"></i> Banned
                        </span>
                    </div>

                    <div class="status-item">
                        <span class="status-name">
                            Comment Access
                            <el-tooltip effect="dark" content="Muted users cannot interact with content" placement="right">
                                <i class="el-icon-info status-info"></i>
                            </el-tooltip>
                        </span>
                        <span v-if="!userInfo.isWord" class="status-badge ok">
                            <i class="el-icon-circle-check"></i> Normal
                        </span>
                        <span v-else class="status-badge banned">
                            <i class="el-icon-warning-outline"></i> Muted
                        </span>
                    </div>
                </div>
            </div>

            <!-- Submit -->
            <div class="form-section">
                <span class="submit-btn" @click="postInfo">
                    <i class="el-icon-check"></i> Save Changes
                </span>
            </div>

        </div>
    </div>
</template>

<script>
export default {
    name: "Self",
    data() {
        return {
            userInfo: {},
            userAvatar: '',
        }
    },
    created() {
        this.auth();
    },
    methods: {
        async postInfo() {
            const { userName, userEmail } = this.userInfo;
            const userUpdateDTO = { userAvatar: this.userAvatar, userName, userEmail };
            const { data } = await this.$axios.put('/user/update', userUpdateDTO);
            this.$notify({
                duration: 1000,
                title: 'Edit Profile',
                message: data.code === 200 ? 'Profile updated successfully' : 'Update failed',
                type: data.code === 200 ? 'success' : 'error'
            });
            if (data.code === 200) this.auth();
        },
        handleAvatarSuccess(res) {
            this.$notify({
                duration: 1500,
                title: 'Avatar Upload',
                message: res.code === 200 ? 'Upload successful' : 'Upload failed',
                type: res.code === 200 ? 'success' : 'error'
            });
            if (res.code === 200) this.userAvatar = res.data;
        },
        async auth() {
            const { data } = await this.$axios.get('/user/auth');
            if (data.code !== 200) {
                this.$router.push('/');
            } else {
                this.userInfo = data.data;
                this.userAvatar = data.data.userAvatar;
            }
        },
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.self-container {
    max-width: 480px;
    margin: 0 auto;
    padding: 28px 24px 60px;
    font-family: 'DM Sans', sans-serif;
}

/* ─── Header ─────────────────────────────────────────── */
.page-header {
    margin-bottom: 28px;
}

.page-title {
    font-family: 'Caveat', cursive;
    font-weight: 700;
    font-size: 42px;
    color: #c8392b;
    margin: 0;
    line-height: 1;
    transform: rotate(-0.8deg);
    display: inline-block;
}

/* ─── Form ───────────────────────────────────────────── */
.form-body {
    display: flex;
    flex-direction: column;
}

.form-section {
    padding: 18px 0;
    border-bottom: 1px solid #e8ddd0;

    &:last-child { border-bottom: none; }
}

.form-row {
    display: flex;
    align-items: center;
    gap: 20px;
}

.form-label {
    display: block;
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #5a5045;
    margin-bottom: 8px;
    white-space: nowrap;
}

/* In form-row the label needs no bottom margin */
.form-row .form-label { margin-bottom: 0; }

.required {
    color: #c8392b;
    margin-left: 2px;
}

/* ─── Avatar uploader ────────────────────────────────── */
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
    width: 88px;
    height: 88px;
    border-radius: 50%;
    object-fit: cover;
    display: block;
}

.avatar-placeholder {
    width: 88px;
    height: 88px;
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

/* ─── Text inputs ────────────────────────────────────── */
.form-input {
    width: 100%;
    height: 44px;
    padding: 0 14px;
    font-family: 'DM Sans', sans-serif;
    font-size: 14px;
    color: #2a2018;
    background-color: #fdfaf5;
    border: 1.5px solid #d6c9b8;
    border-radius: 4px;
    transition: border-color 0.2s, background-color 0.2s;

    &::placeholder { color: #c0b09e; }
    &:focus { outline: none; border-color: #c8392b; background-color: #ffffff; }
}

/* ─── Status section ─────────────────────────────────── */
.status-row {
    display: flex;
    flex-direction: column;
    gap: 12px;
    margin-top: 4px;
}

.status-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 14px;
    background-color: rgba(255,255,255,0.55);
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
}

.status-name {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #3a3028;
    display: flex;
    align-items: center;
    gap: 6px;
}

.status-info {
    font-size: 14px;
    color: #b0a898;
    cursor: help;
}

.status-badge {
    display: inline-flex;
    align-items: center;
    gap: 5px;
    padding: 3px 12px;
    border-radius: 20px;
    font-family: 'Klee One', cursive;
    font-size: 12px;
    font-weight: 600;

    &.ok {
        background-color: rgba(39, 174, 96, 0.08);
        border: 1px solid rgba(39, 174, 96, 0.25);
        color: #27ae60;
    }

    &.banned {
        background-color: rgba(200, 57, 43, 0.08);
        border: 1px solid rgba(200, 57, 43, 0.25);
        color: #c8392b;
        text-decoration: underline;
        text-decoration-style: dashed;
        text-underline-offset: 2px;
    }
}

/* ─── Submit button ──────────────────────────────────── */
.submit-btn {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 10px 28px;
    background-color: #c8392b;
    color: #fdf8f2;
    font-family: 'Klee One', cursive;
    font-size: 15px;
    font-weight: 600;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.15s, transform 0.1s;

    &:hover { background-color: #b03226; }
    &:active { transform: translateY(1px); }
}
</style>