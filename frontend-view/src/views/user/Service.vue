<template>
    <!-- ── Dashboard Page ── -->
    <div class="service-container">

        <!-- ── User profile card ── -->
        <div class="profile-card">
            <img :src="info.userAvatar" class="profile-avatar" />
            <div class="profile-info">
                <div class="profile-name">{{ info.userName }}</div>
                <div class="stats-label">My Activity</div>
                <div class="profile-stats">
                    <div class="stat-pill">
                        <span class="stat-num">{{ info.viewCount }}</span>
                        <span class="stat-desc">articles read</span>
                    </div>
                    <div class="stat-pill">
                        <span class="stat-num">{{ info.upvoteCount }}</span>
                        <span class="stat-desc">articles liked</span>
                    </div>
                    <div class="stat-pill">
                        <span class="stat-num">{{ info.saveCount }}</span>
                        <span class="stat-desc">articles saved</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- ── Custom tabs ── -->
        <div class="tab-bar">
            <span class="tab-item" :class="{ active: activeName === 'first' }" @click="activeName = 'first'">Content
                Sharing</span>
            <span class="tab-item" :class="{ active: activeName === 'second' }"
                @click="activeName = 'second'">Statistics</span>
        </div>
        <div class="tab-divider"></div>

        <!-- ── Tab: Content Sharing ── -->
        <div v-show="activeName === 'first'" class="tab-content">
            <MyContentNet />
        </div>

        <!-- ── Tab: Statistics ── -->
        <div v-show="activeName === 'second'" class="tab-content">
            <Statistics />
        </div>

        <!-- ── Share dialog ── -->
        <el-dialog :visible.sync="dialogShareOperaion" :show-close="false" width="38%" custom-class="food-dialog">
            <div class="dialog-body">
                <!-- Success state -->
                <div v-if="url !== ''" class="share-success">
                    <i class="el-icon-circle-check success-icon"></i>
                    <h3 class="dialog-title">Sharing Link Ready</h3>
                    <div class="share-url">{{ url }}</div>
                </div>

                <!-- Form state -->
                <div v-else>
                    <h3 class="dialog-title">Share Gourmet</h3>

                    <div class="dialog-section">
                        <label class="dialog-label">Valid Days</label>
                        <div class="radio-group">
                            <span v-for="opt in validDayOptions" :key="opt" class="radio-btn"
                                :class="{ active: contentNet.validDay === opt }" @click="contentNet.validDay = opt">{{
                                opt }}</span>
                        </div>
                    </div>

                    <div class="dialog-section">
                        <label class="dialog-label">Password Authentication</label>
                        <div class="toggle-wrap">
                            <el-switch v-model="contentNet.passwordAuth" active-color="#c8392b"
                                inactive-color="#d6c9b8" />
                            <span class="toggle-label">{{ contentNet.passwordAuth ? 'Enabled' : 'Disabled' }}</span>
                        </div>
                    </div>

                    <div class="dialog-section" v-if="contentNet.passwordAuth">
                        <label class="dialog-label">Set Password</label>
                        <input class="dialog-input" type="password" v-model="contentNet.accessPassword"
                            placeholder="Enter password" />
                    </div>
                </div>
            </div>

            <span slot="footer" class="dialog-footer">
                <span class="cancel-btn" @click="cannel()">Close</span>
                <span v-if="url === ''" class="confirm-btn" @click="shareOperation()">Generate Link</span>
            </span>
        </el-dialog>

    </div>
</template>

<script>
import MyContentNet from "@/views/user/MyContentNet.vue";
import Statistics from "@/views/user/Statistics.vue";

export default {
    components: { MyContentNet, Statistics },
    name: "Service",
    data() {
        return {
            cookbookList: [],
            contentNet: {},
            gourmet: {},
            info: {},
            activeName: 'first',
            gourmetList: [],
            dialogShareOperaion: false,
            url: '',
            validDayOptions: ['3 Days', '7 Days', '30 Days', 'Always'],
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
            this.$axios.post('/cookbook/queryUser', { current: 1, size: 100 }).then(res => {
                if (res.data.code === 200) this.cookbookList = res.data.data;
            }).catch(error => { console.log('Error', error); });
        },
        cannel() { this.dialogShareOperaion = false; this.url = ''; this.contentNet = {}; },
        shareOperation() {
            const dayList = [this.contentNet.validDay];
            const validDayList = dayList.map(text => { const match = text.match(/\d+/g); return match ? match : []; });
            if (this.contentNet.passwordAuth) this.contentNet.accessPassword = this.$md5(this.contentNet.accessPassword);
            const saveEntity = {
                gourmetId: this.gourmet.id,
                validDay: validDayList[0].length === 0 ? -1 : validDayList[0][0],
                passwordAuth: this.contentNet.passwordAuth,
                accessPassword: this.contentNet.accessPassword
            };
            this.$axios.post('/contentNet/save', saveEntity).then(res => {
                if (res.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Share', message: 'Link generated successfully', type: 'success' });
                    this.url = res.data.msg;
                }
            }).catch(error => { console.log('Error', error); });
        },
        share(gourmet) { this.gourmet = gourmet; this.dialogShareOperaion = true; },
        edit(gourmet) { sessionStorage.setItem('gourmetId', gourmet.id); this.$router.push('/editGourmet'); },
        async del(gourmet) {
            const confirmed = await this.$swalConfirm({ title: 'Delete Confirmation', text: 'Operation unrecoverable, continue?', icon: 'warning' });
            if (confirmed) {
                try {
                    const response = await this.$axios.post('/gourmet/batchDelete', [gourmet.id]);
                    if (response.data.code === 200) {
                        this.$notify({ duration: 1000, title: 'Delete', message: 'Deleted successfully', type: 'success' });
                        this.fetchMyGourmet();
                    }
                } catch (error) { console.error('Error:', error); }
            }
        },
        readGourmet(gourmet) { sessionStorage.setItem('gourmetId', gourmet.id); this.$router.push('/gourmetDetail'); },
        handleClick(tab, event) { console.log(tab, event); },
        fetchMyGourmet() {
            this.$axios.get('/gourmet/queryUser').then(res => {
                if (res.data.code === 200) this.gourmetList = res.data.data;
            }).catch(error => { console.log('Error', error); });
        },
        fetchCenter() {
            this.$axios.get('/user/center').then(res => {
                if (res.data.code === 200) this.info = res.data.data;
            }).catch(error => { console.log('Error', error); });
        }
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* {
    box-sizing: border-box;
}

.service-container {
    max-width: 1100px;
    margin: 0 auto;
    padding: 28px 24px 48px;
    font-family: 'DM Sans', sans-serif;
}

/* ─── Profile card ───────────────────────────────────── */
.profile-card {
    display: flex;
    align-items: center;
    gap: 20px;
    padding: 20px 24px;
    background-color: rgba(255, 255, 255, 0.6);
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    margin-bottom: 28px;
}

.profile-avatar {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    object-fit: cover;
    flex-shrink: 0;
    border: 2px solid #e8ddd0;
}

.profile-name {
    font-family: 'Klee One', cursive;
    font-size: 22px;
    font-weight: 600;
    color: #2a2018;
    margin-bottom: 8px;
}

.profile-stats {
    display: flex;
    gap: 20px;
    font-family: 'DM Sans', sans-serif;
    font-size: 14px;
    color: #8a7d6e;

    span {
        display: flex;
        align-items: center;
        gap: 5px;
    }

    i {
        font-size: 15px;
    }
}

.stat-pill {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 6px 16px;
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    background-color: rgba(255, 255, 255, 0.55);
    min-width: 72px;
}

.stat-num {
    font-family: 'Klee One', cursive;
    font-size: 18px;
    font-weight: 600;
    color: #c8392b;
    line-height: 1.2;
}

.stat-desc {
    font-family: 'DM Sans', sans-serif;
    font-size: 11px;
    color: #8a7d6e;
    white-space: nowrap;
}

.stats-label {
    font-family: 'Klee One', cursive;
    font-size: 11px;
    font-weight: 600;
    color: #b0a898;
    letter-spacing: 0.8px;
    text-transform: uppercase;
    margin-bottom: 6px;
    margin-top: 10px;
}

/* ─── Custom tabs ────────────────────────────────────── */
.tab-bar {
    display: flex;
    gap: 4px;
}

.tab-item {
    display: inline-flex;
    align-items: center;
    padding: 8px 20px;
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #8a7d6e;
    cursor: pointer;
    border: 1.5px solid transparent;
    border-bottom: none;
    border-radius: 4px 4px 0 0;
    transition: color 0.15s, background-color 0.15s;

    &:hover {
        color: #c8392b;
        background-color: rgba(255, 255, 255, 0.5);
    }

    &.active {
        color: #c8392b;
        background-color: rgba(255, 255, 255, 0.75);
        border-color: #e8ddd0;
        position: relative;
        bottom: -1.5px;
        z-index: 1;
    }
}

.tab-divider {
    height: 1.5px;
    background-color: #e8ddd0;
    margin-bottom: 24px;
}

.tab-content {}

.empty-wrap {
    margin-top: 40px;
}

/* ─── Gourmet list ───────────────────────────────────── */
.item-gourmet {
    display: flex;
    gap: 16px;
    padding: 16px 10px;
    border-bottom: 1px solid #e8ddd0;
    transition: background-color 0.15s;

    &:hover {
        background-color: rgba(255, 255, 255, 0.55);
    }

    &:last-child {
        border-bottom: none;
    }
}

.left img {
    width: 240px;
    height: 165px;
    object-fit: cover;
    border-radius: 4px;
    display: block;
    flex-shrink: 0;
}

.right {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.g-info {
    display: flex;
    align-items: center;
    gap: 7px;
}

.g-avatar {
    width: 22px;
    height: 22px;
    border-radius: 50%;
    object-fit: cover;
}

.g-username {
    font-family: 'Klee One', cursive;
    font-size: 13px;
    color: #8a7d6e;
}

.g-title {
    font-family: 'Klee One', cursive;
    font-size: 20px;
    font-weight: 600;
    color: #2a2018;
    line-height: 1.3;
    cursor: pointer;
    transition: color 0.15s;

    &:hover {
        color: #c8392b;
        text-decoration: underline;
        text-underline-offset: 3px;
    }
}

.g-desc {
    font-size: 14px;
    color: #5a5045;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    line-height: 1.5;
}

.g-meta {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    font-size: 13px;
    color: #9a8d7e;
    align-items: center;
}

.g-actions {
    display: flex;
    gap: 8px;
    margin-top: 4px;
}

.action-btn {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    padding: 4px 12px;
    border-radius: 4px;
    font-family: 'Klee One', cursive;
    font-size: 12px;
    font-weight: 600;
    cursor: pointer;
    border: 1.5px solid transparent;
    transition: background-color 0.15s, border-color 0.15s;

    &.share {
        color: #2e86ab;
        border-color: rgba(46, 134, 171, 0.25);

        &:hover {
            background-color: rgba(46, 134, 171, 0.08);
        }
    }

    &.edit {
        color: #c8392b;
        border-color: rgba(200, 57, 43, 0.25);

        &:hover {
            background-color: rgba(200, 57, 43, 0.08);
        }
    }

    &.delete {
        color: #8a7d6e;
        border-color: #e8ddd0;

        &:hover {
            background-color: rgba(90, 80, 69, 0.08);
            color: #5a5045;
        }
    }
}

/* ─── Cookbook grid ──────────────────────────────────── */
.cookbook-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 18px;
}

.cookbook-card {
    cursor: pointer;
    border-radius: 4px;
    border: 1.5px solid #e8ddd0;
    background-color: rgba(255, 255, 255, 0.55);
    overflow: hidden;
    transition: border-color 0.15s, transform 0.15s, box-shadow 0.15s;

    &:hover {
        border-color: #c8392b;
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(200, 57, 43, 0.1);
    }
}

.card-cover {
    width: 100%;
    aspect-ratio: 4/3;
    overflow: hidden;

    img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        display: block;
        transition: transform 0.3s;
    }
}

.cookbook-card:hover .card-cover img {
    transform: scale(1.04);
}

.card-body {
    padding: 10px 12px 12px;
}

.card-title {
    font-family: 'Klee One', cursive;
    font-size: 16px;
    font-weight: 600;
    color: #2a2018;
    line-height: 1.3;
    margin-bottom: 4px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.cookbook-card:hover .card-title {
    color: #c8392b;
}

.card-time {
    font-size: 12px;
    color: #9a8d7e;
}

/* ─── Dialog ─────────────────────────────────────────── */
::v-deep .food-dialog {
    border-radius: 4px;
    border: 1.5px solid #e8ddd0;

    .el-dialog__header {
        display: none;
    }

    .el-dialog__body {
        padding: 0;
    }

    .el-dialog__footer {
        border-top: 1.5px solid #e8ddd0;
        padding: 14px 20px;
    }
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

.dialog-section {
    margin-bottom: 18px;
}

.dialog-label {
    display: block;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #5a5045;
    margin-bottom: 8px;
}

.radio-group {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
}

.radio-btn {
    display: inline-block;
    padding: 5px 14px;
    border: 1.5px solid #d6c9b8;
    border-radius: 4px;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #5a5045;
    cursor: pointer;
    transition: border-color 0.15s, color 0.15s, background-color 0.15s;

    &:hover {
        border-color: #c8392b;
        color: #c8392b;
    }

    &.active {
        background-color: #c8392b;
        border-color: #c8392b;
        color: #fdf8f2;
    }
}

.toggle-wrap {
    display: flex;
    align-items: center;
    gap: 10px;
}

.toggle-label {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #5a5045;
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

    &::placeholder {
        color: #c0b09e;
    }

    &:focus {
        outline: none;
        border-color: #c8392b;
    }
}

/* Share success state */
.share-success {
    text-align: center;
    padding: 8px 0 4px;
}

.success-icon {
    font-size: 48px;
    color: #27ae60;
    margin-bottom: 10px;
    display: block;
}

.share-url {
    margin-top: 12px;
    padding: 10px 14px;
    background-color: #f5f0e8;
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    font-family: 'DM Sans', sans-serif;
    font-size: 13px;
    color: #3a3028;
    word-break: break-all;
    text-align: left;
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

    &:hover {
        background-color: #f5f0e8;
    }
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

    &:hover {
        background-color: #b03226;
    }
}
</style>