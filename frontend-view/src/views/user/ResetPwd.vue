<template>
    <div class="resetpwd-container">

        <div class="page-header">
            <h2 class="page-title">Change Password</h2>
        </div>

        <div class="form-body">

            <div class="form-section">
                <label class="form-label">Current Password <span class="required">*</span></label>
                <input class="form-input" type="password" v-model="oldPwd" placeholder="Enter current password" />
            </div>

            <div class="form-section">
                <label class="form-label">New Password <span class="required">*</span></label>
                <input class="form-input" type="password" v-model="newPwd" placeholder="Enter new password" />
            </div>

            <div class="form-section">
                <label class="form-label">Confirm Password <span class="required">*</span></label>
                <input class="form-input" type="password" v-model="againPwd" placeholder="Confirm new password" />
            </div>

            <div class="form-section">
                <span class="submit-btn" @click="postInfo">
                    <i class="el-icon-check"></i> Save Changes
                </span>
            </div>

        </div>
    </div>
</template>

<script>
import { clearToken } from "@/utils/storage"
export default {
    name: "ResetPwd",
    data() {
        return {
            oldPwd: '',
            newPwd: '',
            againPwd: ''
        }
    },
    methods: {
        async postInfo() {
            const userUpdatePwdDTO = {
                oldPwd: this.$md5(this.$md5(this.oldPwd)),
                newPwd: this.$md5(this.$md5(this.newPwd)),
                againPwd: this.$md5(this.$md5(this.againPwd))
            };
            const { data } = await this.$axios.put('/user/updatePwd', userUpdatePwdDTO);
            this.$notify({
                duration: 1000,
                title: 'Change Password',
                message: data.code === 200 ? 'Password updated. Please log in again.' : data.msg,
                type: data.code === 200 ? 'success' : 'error'
            });
            if (data.code === 200) {
                clearToken();
                this.$router.push('/');
            }
        }
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.resetpwd-container {
    max-width: 480px;
    margin: 0 auto;
    padding: 28px 24px 60px;
    font-family: 'DM Sans', sans-serif;
}

/* ─── Page header ────────────────────────────────────── */
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

.form-label {
    display: block;
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #5a5045;
    margin-bottom: 8px;
}

.required {
    color: #c8392b;
    margin-left: 2px;
}

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

    &:focus {
        outline: none;
        border-color: #c8392b;
        background-color: #ffffff;
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