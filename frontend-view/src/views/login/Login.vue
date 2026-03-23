<template>
    <div class="login-container">

        <div class="logo-icon">
            <el-image style="width: 52px; height: 52px" src="/logo.png" fit="fill" />
        </div>

        <h1 class="site-title">Food Note</h1>

        <div class="login-form">
            <div class="field">
                <label>Username</label>
                <input v-model="act" class="act" placeholder="Enter your username" />
            </div>
            <div class="field">
                <label>Password</label>
                <input v-model="pwd" class="pwd" type="password" placeholder="Enter your password" />
            </div>

            <span class="login-btn" @click="login">Log In</span>

            <div class="tip">
                <p>Don't have an account? <span class="no-act" @click="toDoRegister">Register Now</span></p>
            </div>
        </div>

    </div>
</template>

<script>
const DELAY_TIME = 1300;
import request from "@/utils/request.js";
import { setToken } from "@/utils/storage.js";
import md5 from 'js-md5';
import Logo from '@/components/Logo.vue';
export default {
    name: "Login",
    components: { Logo },
    data() {
        return {
            act: '',
            pwd: '',
            colorLogo: 'rgb(38,38,38)',
        }
    },
    methods: {
        toDoRegister() {
            this.$router.push('/register');
        },
        async login() {
            if (!this.act || !this.pwd) {
                this.$swal.fire({
                    title: '填写校验',
                    text: '账号或密码不能为空',
                    icon: 'error',
                    showConfirmButton: false,
                    timer: DELAY_TIME,
                });
                return;
            }
            const hashedPwd = md5(md5(this.pwd));
            const paramDTO = { userAccount: this.act, userPwd: hashedPwd };
            try {
                const { data } = await request.post(`user/login`, paramDTO);
                if (data.code !== 200) {
                    this.$swal.fire({
                        title: '登录失败',
                        text: data.msg,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: DELAY_TIME,
                    });
                    return;
                }
                setToken(data.data.token);
                setTimeout(() => {
                    const { role } = data.data;
                    sessionStorage.setItem('role', role);
                    this.navigateToRole(role);
                }, DELAY_TIME);
            } catch (error) {
                console.error('Error:', error);
                this.$message.error('Error, please try again！');
            }
        },
        navigateToRole(role) {
            switch (role) {
                case 1:
                    this.$router.push('/admin');
                    break;
                case 2:
                    this.$router.push('/gourmet');
                    break;
                default:
                    console.warn('Unknown role:', role);
                    break;
            }
        },
    }
};
</script>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&display=swap');

* {
    user-select: none;
    box-sizing: border-box;
}

.login-container {
    width: 100%;
    min-height: 100vh;
    background-color: #f5f0e8;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px 20px 40px;
    font-family: 'Klee One', cursive;
    background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.045'/%3E%3C/svg%3E");
}

/* ─── Logo ───────────────────────────────────────────── */
.logo-icon {
    margin-bottom: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 72px;
    height: 72px;
    border-radius: 50%;
    background-color: rgba(200, 57, 43, 0.08);
}

/* ─── Title — 1.35× bigger, generous gap below ──────── */
.site-title {
    font-family: 'Caveat', cursive;
    font-weight: 700;
    font-size: 88px;     /* was 64px → ×1.375 */
    color: #c8392b;
    margin: 0 0 64px 0;  /* was 44px → larger gap to form */
    text-align: center;
    line-height: 1;
    letter-spacing: 1px;
    transform: rotate(-1.2deg);
}

/* ─── Form ───────────────────────────────────────────── */
.login-form {
    width: 100%;
    max-width: 320px;
    display: flex;
    flex-direction: column;
    align-items: stretch;
}

.field {
    display: flex;
    flex-direction: column;
    margin-bottom: 18px;

    label {
        font-family: 'Klee One', cursive;
        font-size: 16px;
        font-weight: 600;
        color: #3a3028;
        margin-bottom: 7px;
        padding-left: 2px;
    }
}

/* ─── Inputs — warm off-white, no blue ──────────────── */
.act,
.pwd {
    height: 46px;
    width: 100%;
    font-size: 14px;
    font-family: 'Klee One', cursive;
    font-weight: 600;
    padding: 0 16px;
    background-color: #fdf9f4;   /* warm off-white */
    border: 1.5px solid #d6c9b8;
    border-radius: 23px;
    color: #2a2018;
    transition: border-color 0.2s, background-color 0.2s, box-shadow 0.2s;

    &::placeholder {
        color: #c0b09e;
        font-family: 'Klee One', cursive;
        font-weight: 600;
    }

    &:focus {
        outline: none;
        border-color: #c8392b;
        background-color: #ffffff;
        box-shadow: 0 0 0 3px rgba(200, 57, 43, 0.08);
    }
}

/* ─── Button ─────────────────────────────────────────── */
.login-btn {
    display: block;
    text-align: center;
    margin-top: 10px;
    height: 46px;
    line-height: 46px;
    width: 100%;
    background-color: #c8392b;
    color: #fdf8f2;
    font-family: 'Klee One', cursive;
    font-size: 16px;
    font-weight: 600;
    border-radius: 23px;
    cursor: pointer;
    transition: background-color 0.2s, transform 0.1s, box-shadow 0.2s;
    letter-spacing: 0.5px;

    &:hover {
        background-color: #b03226;
        box-shadow: 0 4px 16px rgba(200, 57, 43, 0.25);
    }

    &:active {
        transform: translateY(1px);
    }
}

/* ─── Tip ────────────────────────────────────────────── */
.tip {
    margin-top: 22px;
    text-align: center;

    p {
        font-family: 'Klee One', cursive;
        font-size: 14px;
        font-weight: 600;
        color: #8a7d6e;
        margin: 0;
    }

    .no-act {
        color: #c8392b;
        cursor: pointer;
        text-decoration: underline;
        text-underline-offset: 3px;

        &:hover { color: #a52f24; }
    }
}

@media (max-width: 400px) {
    .site-title { font-size: 66px; }
    .login-form { max-width: 290px; }
}
</style>