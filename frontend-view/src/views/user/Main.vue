<template>
    <div class="user-layout">

        <!-- ── Top nav bar ── -->
        <div class="top-bar">
            <div class="top-bar-inner">

                <!-- Brand -->
                <div class="brand" @click="route('/gourmet')">
                    <el-image style="width: 32px; height: 32px; flex-shrink:0;" src="/logo.png" fit="fill" />
                    <span class="brand-name">Food Note</span>
                </div>

                <!-- Nav links -->
                <ul class="nav-links">
                    <li @click="route('/gourmet')">
                        <i class="el-icon-food"></i> Gourmet
                    </li>
                    <li @click="route('/cookbook')">
                        <i class="el-icon-notebook-2"></i> Cookbook
                    </li>
                    <li @click="route('/Save')">
                        <i class="el-icon-star-off"></i> Favorites
                    </li>
                    <li>
                        <el-dropdown :hide-on-click="false">
                            <span class="nav-dropdown-trigger">
                                My Diet <i class="el-icon-arrow-down el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item @click.native="route('/dietHistory')">History</el-dropdown-item>
                                <el-dropdown-item @click.native="route('/nutriment')">Nutriment</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </li>
                    <li>
                        <el-dropdown :hide-on-click="false">
                            <span class="nav-dropdown-trigger">
                                Create <i class="el-icon-arrow-down el-icon--right"></i>
                            </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item @click.native="route('/createGourmet')">Add Gourmet</el-dropdown-item>
                                <el-dropdown-item @click.native="route('/myCookbook')">Add Cookbook</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </li>
                </ul>

                <!-- Right actions -->
                <div class="nav-actions">
                    <span class="dashboard-btn" @click="route('/service')">Dashboard</span>

                    <el-dropdown :hide-on-click="false" class="user-dropdown">
                        <span class="user-trigger">
                            <el-avatar :size="28" :src="userInfo.userAvatar" />
                            <span class="user-name">{{ userInfo.userName }}</span>
                            <i class="el-icon-arrow-down"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item @click.native="route('/self')">My Profile</el-dropdown-item>
                            <el-dropdown-item @click.native="route('/resetPwd')">Change Password</el-dropdown-item>
                            <el-dropdown-item @click.native="loginOut">Log Out</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>

            </div>
        </div>

        <!-- ── Page content ── -->
        <div class="router-view">
            <router-view></router-view>
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
    },
    methods: {
        route(path) {
            if (this.$route.path !== path) {
                this.$router.push(path);
            }
        },
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
        async auth() {
            const { data } = await this.$axios.get('/user/auth');
            if (data.code !== 200) {
                this.$router.push('/');
            } else {
                this.userInfo = data.data;
                sessionStorage.setItem('userInfo', JSON.stringify(this.userInfo));
            }
        },
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

/* ─── Overall shell ──────────────────────────────────── */
.user-layout {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    background-color: #f5f0e8;
    background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.04'/%3E%3C/svg%3E");
    font-family: 'DM Sans', sans-serif;
    /* Reset any browser/global margin that would create gaps around the nav */
    margin: 0;
    padding: 0;
}

/* ─── Top bar — truly full-bleed ────────────────────── */
.top-bar {
    /* Stretch beyond any parent padding by using negative margins */
    width: 100vw;
    position: sticky;
    top: 0;
    left: 0;
    margin-left: calc(-50vw + 50%);
    background-color: #c8392b;
    box-shadow: 0 2px 14px rgba(200, 57, 43, 0.2);
    z-index: 100;
}

.top-bar-inner {
    /* Match the same left offset used in Gourmet content (24px padding) */
    padding: 0 24px;
    height: 58px;
    display: flex;
    align-items: center;
    gap: 32px;
}

/* ─── Brand ──────────────────────────────────────────── */
.brand {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    flex-shrink: 0;
    text-decoration: none;
    /* Push everything after brand to the right */
    margin-right: auto;
}

.brand-name {
    font-family: 'Caveat', cursive;
    font-weight: 700;
    font-size: 30px;
    color: #fdf8f2;
    line-height: 1;
    transform: rotate(-1.2deg);
    display: inline-block;
    letter-spacing: 0.5px;
}

/* ─── Nav links ──────────────────────────────────────── */
.nav-links {
    display: flex;
    align-items: center;
    list-style: none;
    margin: 0;
    padding: 0;
    gap: 2px;
}

.nav-links li {
    cursor: pointer;
    padding: 6px 12px;
    border-radius: 20px;
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: rgba(253, 248, 242, 0.88);
    display: flex;
    align-items: center;
    gap: 5px;
    transition: background-color 0.15s, color 0.15s;
    white-space: nowrap;

    i { font-size: 15px; }

    &:hover {
        background-color: rgba(255, 255, 255, 0.15);
        color: #fdf8f2;
    }
}

.nav-dropdown-trigger {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: rgba(253, 248, 242, 0.88);
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 3px;

    &:hover { color: #fdf8f2; }
}

/* ─── Right actions ──────────────────────────────────── */
.nav-actions {
    display: flex;
    align-items: center;
    gap: 14px;
    flex-shrink: 0;
}

.dashboard-btn {
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #c8392b;
    background-color: #fdf8f2;
    padding: 5px 14px;
    border-radius: 20px;
    cursor: pointer;
    transition: background-color 0.15s, transform 0.1s;
    white-space: nowrap;

    &:hover {
        background-color: #f5ece0;
    }

    &:active {
        transform: translateY(1px);
    }
}

.user-trigger {
    display: flex;
    align-items: center;
    gap: 6px;
    cursor: pointer;
    padding: 4px 10px;
    border-radius: 20px;
    transition: background-color 0.15s;

    &:hover {
        background-color: rgba(255,255,255,0.15);
    }
}

.user-name {
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #fdf8f2;
    max-width: 90px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.user-trigger i {
    color: rgba(253,248,242,0.8);
    font-size: 12px;
}

/* Override Element dropdown in red bar */
::v-deep .el-dropdown-menu {
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    box-shadow: 0 4px 16px rgba(0,0,0,0.08);

    .el-dropdown-menu__item {
        font-family: 'Klee One', cursive;
        font-size: 14px;
        color: #3a3028;

        &:hover {
            background-color: #fdf8f2;
            color: #c8392b;
        }
    }
}

/* ─── Router view area ───────────────────────────────── */
.router-view {
    flex: 1;
    /* No extra wrapper — let each child page control its own padding */
}
</style>