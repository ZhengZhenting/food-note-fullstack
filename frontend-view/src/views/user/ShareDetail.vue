<template>
    <div class="share-container">

        <!-- ── Password auth form ── -->
        <div v-if="authStatus" class="auth-card">
            <div class="auth-icon">
                <i class="el-icon-lock"></i>
            </div>
            <h2 class="auth-title">Food Note</h2>
            <p class="auth-subtitle">This content is password protected.</p>

            <div class="auth-field">
                <label class="auth-label">Password</label>
                <input
                    class="auth-input"
                    type="password"
                    v-model="contentNetQueryDto.accessPassword"
                    placeholder="Enter password"
                    @keyup.enter="shareOperation()"
                />
            </div>

            <span class="auth-btn" @click="shareOperation()">
                <i class="el-icon-right"></i> Access Content
            </span>
        </div>

        <!-- ── Loading state (no auth needed, redirecting) ── -->
        <div v-else class="loading-wrap">
            <i class="el-icon-loading loading-icon"></i>
            <p class="loading-text">Loading content…</p>
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
            this.$axios.post('/contentNet/findContent', this.contentNetQueryDto).then(res => {
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
            }).catch(error => { console.log('Error', error); });
        },
        loadAuthStauts(accessCode) {
            this.$axios.post('/contentNet/authStatus', { accessCode }).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.authStatus = data.data;
                    if (!this.authStatus) {
                        this.contentNetQueryDto.accessCode = accessCode;
                        this.$axios.post('/contentNet/findContent', this.contentNetQueryDto).then(res => {
                            const { data } = res;
                            if (data.code === 200 && Array.isArray(data.data) && data.data.length > 0) {
                                sessionStorage.setItem('gourmetId', data.data[0].id);
                                this.$router.push('/gourmetDetail');
                            }
                        });
                    }
                }
            }).catch(error => { console.log('Error', error); });
        },
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

/* ─── Full-page background ───────────────────────────── */
.share-container {
    width: 100%;
    min-height: 100vh;
    background-color: #f5f0e8;
    background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.045'/%3E%3C/svg%3E");
    display: flex;
    align-items: center;
    justify-content: center;
    font-family: 'DM Sans', sans-serif;
    padding: 40px 20px;
}

/* ─── Auth card ──────────────────────────────────────── */
.auth-card {
    width: 100%;
    max-width: 340px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.auth-icon {
    width: 64px;
    height: 64px;
    border-radius: 50%;
    background-color: rgba(200, 57, 43, 0.08);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 14px;

    i {
        font-size: 28px;
        color: #c8392b;
    }
}

.auth-title {
    font-family: 'Caveat', cursive;
    font-weight: 700;
    font-size: 64px;
    color: #c8392b;
    margin: 0 0 6px 0;
    text-align: center;
    line-height: 1;
    transform: rotate(-1.2deg);
    display: inline-block;
}

.auth-subtitle {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #8a7d6e;
    margin: 0 0 28px 0;
    text-align: center;
}

.auth-field {
    width: 100%;
    display: flex;
    flex-direction: column;
    margin-bottom: 16px;
}

.auth-label {
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #5a5045;
    margin-bottom: 7px;
    letter-spacing: 0.3px;
}

.auth-input {
    height: 46px;
    width: 100%;
    font-size: 14px;
    font-family: 'Klee One', cursive;
    font-weight: 600;
    padding: 0 16px;
    background-color: #fdf9f4;
    border: 1.5px solid #d6c9b8;
    border-radius: 23px;
    color: #2a2018;
    transition: border-color 0.2s, background-color 0.2s, box-shadow 0.2s;

    &::placeholder { color: #c0b09e; }

    &:focus {
        outline: none;
        border-color: #c8392b;
        background-color: #ffffff;
        box-shadow: 0 0 0 3px rgba(200, 57, 43, 0.08);
    }
}

.auth-btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 6px;
    height: 46px;
    line-height: 46px;
    width: 100%;
    background-color: #c8392b;
    color: #fdf8f2;
    font-family: 'Klee One', cursive;
    font-size: 15px;
    font-weight: 600;
    border-radius: 23px;
    cursor: pointer;
    transition: background-color 0.2s, transform 0.1s;
    letter-spacing: 0.5px;
    margin-top: 4px;

    &:hover { background-color: #b03226; }
    &:active { transform: translateY(1px); }
}

/* ─── Loading state ──────────────────────────────────── */
.loading-wrap {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 14px;
}

.loading-icon {
    font-size: 36px;
    color: #c8392b;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    from { transform: rotate(0deg); }
    to   { transform: rotate(360deg); }
}

.loading-text {
    font-family: 'Klee One', cursive;
    font-size: 15px;
    font-weight: 600;
    color: #8a7d6e;
    margin: 0;
}
</style>