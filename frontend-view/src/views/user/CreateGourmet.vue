<template>
    <div class="create-container">

        <!-- ── Toolbar ── -->
        <div class="toolbar">
            <span class="add-btn" @click="add()">
                <i class="el-icon-plus"></i> Add Gourmet
            </span>
        </div>

        <!-- ── Table ── -->
        <div class="table-wrap">
            <el-table :data="gourmetList" style="width: 100%;" :header-cell-style="headerStyle" :cell-style="cellStyle">

                <el-table-column prop="title" label="Title" min-width="300" />
                <el-table-column label="Stats" min-width="160">
                    <template slot-scope="scope">
                        <span class="stat-item"><i class="el-icon-view"></i> {{ scope.row.viewCount }}</span>
                        <span class="stat-item"><i class="el-icon-thumb"></i> {{ scope.row.upvoteCount }}</span>
                        <span class="stat-item"><i class="el-icon-collection"></i> {{ scope.row.saveCount }}</span>
                        <span class="stat-item"><i class="el-icon-star-off"></i> {{ scope.row.rating }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="Public" min-width="80">
                    <template slot-scope="scope">
                        <span :class="scope.row.isPublish ? 'badge-private' : 'badge-public'">
                            {{ scope.row.isPublish ? 'Private' : 'Public' }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="Created" min-width="150" />
                <el-table-column label="Actions" width="250">
                    <template slot-scope="scope">
                        <span class="text-btn share" @click="share(scope.row)"><i class="el-icon-share"></i>
                            Share</span>
                        <span class="text-btn edit" @click="edit(scope.row)">
                            <i class="el-icon-edit"></i> Edit
                        </span>
                        <span class="text-btn delete" @click="del(scope.row)"><i class="el-icon-delete"></i>
                            Delete</span>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- ── Add / Edit Dialog ── -->
        <el-dialog :show-close="false" :visible.sync="dialogGourmetOperation" width="56%" custom-class="food-dialog">
            <div class="dialog-body">
                <h3 class="dialog-title">Add Gourmet</h3>

                <!-- Title -->
                <div class="dialog-section">
                    <label class="dialog-label">Title <span class="required">*</span></label>
                    <input type="text" class="dialog-input" placeholder="Recipe title…" v-model="gourmet.title" />
                </div>

                <!-- Editor -->
                <div class="dialog-section editor-section">
                    <label class="dialog-label">Content</label>
                    <div class="editor-wrap">
                        <Editor v-if="dialogGourmetOperation" :receiveContent="gourmet.content"
                            @on-receive="onReceive" />
                    </div>
                </div>

                <!-- Category -->
                <div class="dialog-section dialog-row">
                    <label class="dialog-label">Category <span class="required">*</span></label>
                    <el-select style="width: 200px;" size="small" v-model="gourmet.categoryId"
                        placeholder="Select category" clearable>
                        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </div>

                <!-- Cover -->
                <div class="dialog-section dialog-row">
                    <label class="dialog-label">Cover Image <span class="required">*</span></label>
                    <el-upload class="cover-uploader"
                        action="http://localhost:21090/api/food-share-sys/v1.0/file/upload" :show-file-list="false"
                        :on-success="handleCoverSuccess">
                        <img v-if="cover" :src="cover" class="cover-preview" />
                        <div v-else class="cover-placeholder">
                            <i class="el-icon-plus"></i>
                            <span>Upload Cover</span>
                        </div>
                    </el-upload>
                </div>

                <!-- Visibility -->
                <div class="dialog-section dialog-row">
                    <label class="dialog-label">Visibility <span class="required">*</span></label>
                    <div class="toggle-wrap">
                        <el-switch v-model="gourmet.isPublish" active-color="#c8392b" inactive-color="#d6c9b8" />
                        <span class="toggle-label">{{ gourmet.isPublish ? 'Public' : 'Private' }}</span>
                    </div>
                </div>
            </div>

            <span slot="footer" class="dialog-footer">
                <span class="cancel-btn" @click="cannel()">Cancel</span>
                <span class="confirm-btn" @click="postGourmet()">
                    <i class="el-icon-s-promotion"></i> Publish
                </span>
            </span>
        </el-dialog>

        <!-- ── Share Dialog ── -->
        <el-dialog :visible.sync="dialogShareOperaion" :show-close="false" width="38%" custom-class="food-dialog">
            <div class="dialog-body">
                <div v-if="url !== ''" class="share-success">
                    <i class="el-icon-circle-check success-icon"></i>
                    <h3 class="dialog-title">Sharing Link Ready</h3>
                    <div class="share-url">{{ url }}</div>
                </div>
                <div v-else>
                    <h3 class="dialog-title">Share Gourmet</h3>
                    <div class="dialog-section">
                        <label class="dialog-label">Valid Days</label>
                        <div class="radio-group">
                            <span v-for="opt in validDayOptions" :key="opt" class="radio-btn"
                                :class="{ active: contentNet.validDay === opt }" @click="contentNet.validDay = opt">{{
                                    opt
                                }}</span>
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
                <span class="cancel-btn" @click="cannelShare()">Close</span>
                <span v-if="url === ''" class="confirm-btn" @click="shareOperation()">Generate Link</span>
            </span>
        </el-dialog>

    </div>
</template>

<script>
import Editor from "@/components/Editor"
export default {
    components: { Editor },
    name: "CreateGourmet",
    data() {
        return {
            gourmetList: [],
            dialogGourmetOperation: false,
            gourmet: {},
            categories: [],
            cover: null,
            dialogShareOperaion: false,
            url: '',
            contentNet: {},
            validDayOptions: ['3 Days', '7 Days', '30 Days', 'Always'],
            shareTarget: {},
        }
    },
    created() {
        this.fetchMyGourmet();
        this.fetchFreshCategories();
    },
    methods: {
        /* ── Table style ── */
        headerStyle() {
            return {
                fontFamily: "'Klee One', cursive",
                fontSize: '13px',
                fontWeight: '600',
                color: '#5a5045',
                backgroundColor: '#f5f0e8',
                borderBottom: '1.5px solid #e8ddd0',
            };
        },
        cellStyle() {
            return {
                fontFamily: "'DM Sans', sans-serif",
                fontSize: '13px',
                color: '#3a3028',
                backgroundColor: 'transparent',
                borderBottom: '1px solid #f0ebe0',
            };
        },

        /* ── Fetch ── */
        fetchMyGourmet() {
            this.$axios.get('/gourmet/queryUser').then(res => {
                if (res.data.code === 200) this.gourmetList = res.data.data;
            }).catch(error => { console.log('Error', error); });
        },
        fetchFreshCategories() {
            this.$axios.post('/category/query', {}).then(response => {
                if (response.data.code === 200) {
                    this.categories = response.data.data;
                }
            }).catch(error => { console.log('Error:', error); });
        },

        /* ── Add / Edit dialog ── */
        add() {
            this.gourmet = {};
            this.cover = null;
            this.isOperation = false;
            this.dialogGourmetOperation = true;
        },
        edit(gourmet) {
            sessionStorage.setItem('gourmetId', gourmet.id);
            this.$router.push('/editGourmet');
        },
        cannel() {
            this.gourmet = {};
            this.cover = null;
            this.isOperation = false;
            this.dialogGourmetOperation = false;
        },

        /* ── Create ── */
        postGourmet() {
            this.gourmet.cover = this.cover;
            this.$axios.post('/gourmet/save', this.gourmet).then(response => {
                if (response.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Gourmet Published', type: 'success' });
                    this.cannel();
                    this.fetchMyGourmet();
                }
            }).catch(error => { console.log('Error:', error); });
        },

        /* ── Update ── */
        updateGourmet() {
            this.gourmet.cover = this.cover;
            this.$axios.put('/gourmet/update', this.gourmet).then(response => {
                if (response.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Edit', message: 'Updated successfully', type: 'success' });
                    this.cannel();
                    this.fetchMyGourmet();
                }
            }).catch(error => { console.log('Error:', error); });
        },

        /* ── Delete ── */
        async del(gourmet) {
            const confirmed = await this.$swalConfirm({
                title: 'Delete Confirmation',
                text: 'Operation unrecoverable, continue?',
                icon: 'warning',
            });
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

        /* ── Share ── */
        share(gourmet) {
            this.shareTarget = gourmet;
            this.url = '';
            this.contentNet = {};
            this.dialogShareOperaion = true;
        },
        cannelShare() {
            this.dialogShareOperaion = false;
            this.url = '';
            this.contentNet = {};
        },
        shareOperation() {
            const dayList = [this.contentNet.validDay];
            const validDayList = dayList.map(text => { const match = text.match(/\d+/g); return match ? match : []; });
            if (this.contentNet.passwordAuth) this.contentNet.accessPassword = this.$md5(this.contentNet.accessPassword);
            const saveEntity = {
                gourmetId: this.shareTarget.id,
                validDay: validDayList[0].length === 0 ? -1 : validDayList[0][0],
                passwordAuth: this.contentNet.passwordAuth,
                accessPassword: this.contentNet.accessPassword,
            };
            this.$axios.post('/contentNet/save', saveEntity).then(res => {
                if (res.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Share', message: 'Link generated successfully', type: 'success' });
                    this.url = res.data.msg;
                }
            }).catch(error => { console.log('Error', error); });
        },

        /* ── Cover upload ── */
        handleCoverSuccess(res) {
            this.$notify({
                duration: 1500,
                title: 'Cover Upload',
                message: res.code === 200 ? 'Upload Success' : 'Upload Failed',
                type: res.code === 200 ? 'success' : 'error',
            });
            if (res.code === 200) this.cover = res.data;
        },

        /* ── Editor ── */
        onReceive(content) { this.gourmet.content = content; },
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* {
    box-sizing: border-box;
}

.create-container {
    max-width: 1100px;
    margin: 0 auto;
    padding: 28px 24px 60px;
    font-family: 'DM Sans', sans-serif;
}

/* ─── Toolbar ────────────────────────────────────────── */
.toolbar {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
}

.add-btn {
    display: inline-flex;
    align-items: center;
    gap: 5px;
    padding: 7px 16px;
    background-color: #c8392b;
    color: #fdf8f2;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.15s;
    white-space: nowrap;

    &:hover {
        background-color: #b03226;
    }
}

/* ─── Table ──────────────────────────────────────────── */
.table-wrap {
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    overflow: hidden;
    background-color: rgba(255, 255, 255, 0.55);

    ::v-deep .el-table {
        background-color: transparent;

        tr:hover>td {
            background-color: rgba(200, 57, 43, 0.03) !important;
        }

        .el-table__row {
            background-color: transparent;
        }
    }
}

.stat-item {
    display: inline-flex;
    align-items: center;
    gap: 3px;
    margin-right: 8px;
    color: #9a8d7e;
    font-size: 12px;
}

.badge-public {
    display: inline-block;
    padding: 2px 8px;
    border-radius: 20px;
    font-size: 11px;
    font-family: 'Klee One', cursive;
    font-weight: 600;
    background-color: rgba(200, 57, 43, 0.08);
    color: #c8392b;
    border: 1px solid rgba(200, 57, 43, 0.2);
}

.badge-private {
    display: inline-block;
    padding: 2px 8px;
    border-radius: 20px;
    font-size: 11px;
    font-family: 'Klee One', cursive;
    font-weight: 600;
    background-color: rgba(90, 80, 69, 0.06);
    color: #8a7d6e;
    border: 1px solid #d6c9b8;
}

.text-btn {
    font-family: 'Klee One', cursive;
    font-size: 12px;
    font-weight: 600;
    cursor: pointer;
    padding: 3px 7px;
    border-radius: 3px;
    margin-right: 3px;
    transition: background-color 0.15s;

    &.share {
        color: #5a7d9a;

        &:hover {
            background-color: rgba(90, 125, 154, 0.08);
        }
    }

    &.edit {
        color: #c8392b;

        &:hover {
            background-color: rgba(200, 57, 43, 0.08);
        }
    }

    &.delete {
        color: #8a7d6e;

        &:hover {
            background-color: rgba(90, 80, 69, 0.08);
            color: #5a5045;
        }
    }
}

/* ─── Dialog (shared) ────────────────────────────────── */
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
    max-height: 70vh;
    overflow-y: auto;
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

    ::v-deep .el-input__inner {
        border-color: #d6c9b8;
        border-radius: 4px;
        font-family: 'DM Sans', sans-serif;
        font-size: 14px;
        color: #3a3028;
        background-color: #ffffff;

        &::placeholder {
            color: #c0b09e;
        }

        &:focus {
            border-color: #c8392b;
        }
    }
}

.dialog-row {
    display: flex;
    align-items: center;
    gap: 20px;
}

.dialog-label {
    display: block;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #5a5045;
    margin-bottom: 7px;
    white-space: nowrap;
    min-width: 110px;
}

.dialog-row .dialog-label {
    margin-bottom: 0;
}

.required {
    color: #c8392b;
    margin-left: 2px;
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
    box-sizing: border-box;
    transition: border-color 0.2s;

    &::placeholder {
        color: #c0b09e;
    }

    &:focus {
        outline: none;
        border-color: #c8392b;
    }
}

/* ─── Editor inside dialog ───────────────────────────── */
.editor-section {
    display: flex;
    flex-direction: column;
}

.editor-section .dialog-label {
    margin-bottom: 7px;
}

.editor-wrap {
    width: 100%;
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    overflow: hidden;

    &:focus-within {
        border-color: #c8392b;
    }
}

/* ─── Cover uploader inside dialog ──────────────────── */
.cover-uploader {
    ::v-deep .el-upload {
        border: 1.5px dashed #d6c9b8;
        border-radius: 4px;
        cursor: pointer;
        overflow: hidden;
        transition: border-color 0.15s;
        display: block;

        &:hover {
            border-color: #c8392b;
        }
    }
}

.cover-preview {
    width: 180px;
    height: 130px;
    object-fit: cover;
    display: block;
}

.cover-placeholder {
    width: 180px;
    height: 130px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 8px;
    color: #b0a898;
    font-family: 'Klee One', cursive;
    font-size: 13px;

    i {
        font-size: 26px;
    }
}

/* ─── Toggle ─────────────────────────────────────────── */
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

/* ─── Radio group (share dialog) ────────────────────── */
.radio-group {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
}

.radio-btn {
    padding: 5px 14px;
    border: 1.5px solid #d6c9b8;
    border-radius: 20px;
    font-family: 'Klee One', cursive;
    font-size: 13px;
    color: #5a5045;
    cursor: pointer;
    transition: all 0.15s;

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

/* ─── Share success ──────────────────────────────────── */
.share-success {
    text-align: center;
    padding: 16px 0 8px;
}

.success-icon {
    font-size: 40px;
    color: #c8392b;
    display: block;
    margin-bottom: 10px;
}

.share-url {
    margin-top: 12px;
    padding: 10px 14px;
    background-color: #f5f0e8;
    border: 1px solid #e8ddd0;
    border-radius: 4px;
    font-family: 'DM Sans', sans-serif;
    font-size: 13px;
    color: #3a3028;
    word-break: break-all;
    text-align: left;
}

/* ─── Dialog footer ──────────────────────────────────── */
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
    display: inline-flex;
    align-items: center;
    gap: 5px;
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