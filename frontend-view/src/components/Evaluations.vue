<template>
    <div class="evaluations-wrap">

        <!-- ── Section header ── -->
        <h3 class="comment-header">
            <i class="el-icon-chat-line-round"></i>
            Comments <span class="comment-count">{{ evaluationsCount }}</span>
        </h3>

        <!-- ── New comment input ── -->
        <div class="comment-input-row">
            <el-avatar :src="userData.userAvatar" :size="36" class="input-avatar" />
            <div class="input-box" :class="{ focused: isFocused }">
                <textarea
                    class="comment-textarea"
                    v-model="content"
                    placeholder="Add a comment…"
                    @focus="onFocus"
                    @blur="onBlur"
                />
                <div class="input-footer">
                    <span class="char-count">{{ content.length }} / 300</span>
                    <span class="submit-btn" :class="{ active: isFocused }" @click="commentClick">Comment</span>
                </div>
            </div>
        </div>

        <!-- ── Comment list ── -->
        <div class="comment-list">
            <div class="comment-item" v-for="(comment, index) in commentList" :key="index">

                <!-- Parent comment -->
                <div class="comment-main">
                    <el-avatar :src="comment.userAvatar" :size="34" class="c-avatar" />
                    <div class="c-body">
                        <div class="c-meta-top">
                            <span class="c-username">{{ comment.userName }}</span>
                            <span v-if="comment.userId == userId" class="my-tag">me</span>
                        </div>
                        <div class="c-content">{{ comment.content }}</div>
                        <div class="c-actions">
                            <span class="c-time">{{ comment.time }}</span>
                            <span class="c-action" @click="toggleReplyInput(comment)">
                                <i class="el-icon-chat-dot-round"></i>
                                Reply<span v-if="comment.childTotal">({{ comment.childTotal }})</span>
                            </span>
                            <span class="c-action like" :class="{ liked: comment.upvoteFlag }" @click="upvote(comment)">
                                <i class="el-icon-thumb"></i>
                                {{ comment.upvoteCount || '' }}
                            </span>
                            <el-popconfirm
                                v-if="comment.userId == userId"
                                confirm-button-text="Delete"
                                cancel-button-text="Cancel"
                                icon="el-icon-info"
                                icon-color="#c8392b"
                                title="Delete this comment?"
                                @confirm="deleteComment(comment)"
                            >
                                <span slot="reference" class="c-action danger">
                                    <i class="el-icon-delete"></i> Delete
                                </span>
                            </el-popconfirm>
                        </div>

                        <!-- Reply input for parent -->
                        <div v-if="comment.showReplyInput" class="reply-input-wrap">
                            <textarea
                                class="reply-textarea"
                                v-model="replyContent"
                                :placeholder="replyText"
                            />
                            <div class="input-footer">
                                <span class="char-count">{{ replyContent.length }} / 300</span>
                                <span class="submit-btn active" @click="submitReply(comment)">Reply</span>
                            </div>
                        </div>

                        <!-- Child comments -->
                        <div class="child-list" v-if="comment.commentChildVOS && comment.commentChildVOS.length">
                            <div class="child-item" v-for="(child, ci) in comment.commentChildVOS" :key="ci">
                                <el-avatar :src="child.userAvatar" :size="26" class="c-avatar small" />
                                <div class="c-body">
                                    <div class="c-meta-top">
                                        <span class="c-username">{{ child.userName }}</span>
                                        <span v-if="child.userId == userId" class="my-tag">me</span>
                                        <template v-if="child.replierName">
                                            <span class="reply-arrow">→</span>
                                            <el-avatar :src="child.replierAvatar" :size="18" style="vertical-align:middle;margin-right:4px;" />
                                            <span class="c-username">{{ child.replierName }}</span>
                                            <span v-if="child.replierId == userId" class="my-tag">me</span>
                                        </template>
                                    </div>
                                    <div class="c-content">{{ child.content }}</div>
                                    <div class="c-actions">
                                        <span class="c-time">{{ child.time }}</span>
                                        <span class="c-action" @click="toggleReplyInput1(child)">
                                            <i class="el-icon-chat-dot-round"></i> Reply
                                        </span>
                                        <span class="c-action like" :class="{ liked: child.upvoteFlag }" @click="upvote(child)">
                                            <i class="el-icon-thumb"></i>
                                            {{ child.upvoteCount || '' }}
                                        </span>
                                        <el-popconfirm
                                            v-if="child.userId == userId"
                                            confirm-button-text="Delete"
                                            cancel-button-text="Cancel"
                                            icon="el-icon-info"
                                            icon-color="#c8392b"
                                            title="Delete this comment?"
                                            @confirm="deleteComment(child)"
                                        >
                                            <span slot="reference" class="c-action danger">
                                                <i class="el-icon-delete"></i> Delete
                                            </span>
                                        </el-popconfirm>
                                    </div>

                                    <!-- Reply input for child -->
                                    <div v-if="child.replyInputStatus" class="reply-input-wrap">
                                        <textarea
                                            class="reply-textarea"
                                            v-model="replyChildContent"
                                            :placeholder="replyText"
                                        />
                                        <div class="input-footer">
                                            <span class="char-count">{{ replyChildContent.length }} / 300</span>
                                            <span class="submit-btn active" @click="submitReply1(child)">Reply</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

            </div>
        </div>

        <!-- ── Report dialog ── -->
        <el-dialog
            :show-close="false"
            :visible.sync="dialogVisibleReport"
            width="36%"
            custom-class="food-dialog"
        >
            <div class="dialog-body">
                <h3 class="dialog-title">Report Comment</h3>
                <div v-for="(item, index) in reports" :key="index" class="report-group">
                    <div class="dialog-label">{{ item.name }}</div>
                    <div class="report-options">
                        <span
                            v-for="(itemChild, ic) in item.list"
                            :key="ic"
                            class="report-option"
                            :class="{ selected: itemChild.isSelected }"
                            @click="reportItemClick(itemChild)"
                        >{{ itemChild.name }}</span>
                    </div>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <span class="cancel-btn" @click="dialogVisibleReport = false">Cancel</span>
                <span class="confirm-btn" @click="operationReport">Submit Report</span>
            </span>
        </el-dialog>

    </div>
</template>

<script>
import { timeAgo } from '@/utils/data'
export default {
    props: {
        contentId:   { type: Number, default: 0 },
        contentType: { type: String, default: '' }
    },
    data() {
        return {
            userData: {},
            content: '',
            commentList: [],
            replyContent: '',
            replyChildContent: '',
            isFocused: false,
            bgColor: 'rgb(245,245,245)',
            replyText: '',
            userId: '',
            dialogVisibleReport: false,
            reports: [],
            evaluationsCount: 0,
            comment: {},
        };
    },
    watch: {
        contentId(newVal, oldVal) { if (newVal !== oldVal) this.loadCommentList(); },
        content() { if (this.content === '') this.isFocused = false; },
    },
    created() {
        this.getUserInfo();
        this.loadCommentList();
    },
    methods: {
        getUserInfo() {
            const userInfo = sessionStorage.getItem('userInfo');
            this.userData = JSON.parse(userInfo);
            this.userId = this.userData.id;
        },
        upvote(comment) {
            let upvoteList = comment.upvoteList ? comment.upvoteList.split(',') : [];
            if (upvoteList.length) {
                if (comment.upvoteFlag) {
                    const idx = upvoteList.indexOf(this.userData.id.toString());
                    if (idx !== -1) upvoteList.splice(idx, 1);
                } else {
                    if (!upvoteList.includes(this.userData.userId.toString())) upvoteList.push(this.userData.userId.toString());
                }
            }
            const ev = { id: comment.id, upvoteList: upvoteList.length ? upvoteList.join(',') : this.userData.id };
            this.$axios.put('evaluations/update', ev).then(res => {
                if (res.data.code == 200) {
                    comment.upvoteList = upvoteList.join(',');
                    comment.upvoteFlag = !comment.upvoteFlag;
                    comment.upvoteCount += 1;
                }
            }).catch(err => { console.error('Error ->', err); });
        },
        operationReport() {
            let reportItem = [];
            this.reports.forEach(entity => {
                const sel = entity.list.filter(c => c.isSelected);
                if (sel.length) reportItem = sel;
            });
            if (!reportItem.length) { this.$message('Please select a report reason'); return; }
            this.$axios.get(`evaluations-reports/report/${this.comment.id}/${reportItem[0].name}`).then(res => {
                this.dialogVisibleReport = false;
                this.$swal.fire({ title: 'Report', text: res.data.code == 200 ? 'Report submitted' : res.data.msg, icon: res.data.code == 200 ? 'success' : 'error', showConfirmButton: false, timer: 1100 });
            }).catch(err => { console.error('Error ->', err); });
        },
        reportItemClick(itemChild) {
            this.reports.forEach(e => e.list.forEach(c => { c.isSelected = false; }));
            itemChild.isSelected = true;
        },
        reportList() {
            this.$axios.get('evaluations/reportList').then(res => {
                if (res.data.code == 200) {
                    this.reports = res.data.data.map(entity => ({
                        name: entity.name,
                        list: entity.list.map(item => ({ name: item, isSelected: false }))
                    }));
                }
            }).catch(err => { console.error('Error ->', err); });
        },
        reportComment(comment) { this.reportList(); this.dialogVisibleReport = true; this.comment = comment; },
        deleteComment(comment) {
            this.$axios.delete(`evaluations/delete/${comment.id}`).then(res => {
                if (res.data.code == 200) { this.$message.success(res.data.msg); this.loadCommentList(); }
            }).catch(err => { console.error('Error ->', err); });
        },
        onFocus() { this.isFocused = true; },
        onBlur() { if (this.content === '') this.isFocused = false; else this.isFocused = true; },
        commentClick() {
            if (!this.content) { this.$swal.fire({ title: 'Notice', text: 'Comment cannot be empty', icon: 'warning', showConfirmButton: false, timer: 800 }); return; }
            this.$axios.post('evaluations/insert', { contentType: this.contentType, content: this.content, contentId: this.contentId }).then(res => {
                if (res.data.code == 200) {
                    this.content = '';
                    this.$swal.fire({ title: 'Comment', text: 'Comment posted', icon: 'success', showConfirmButton: false, timer: 1100 });
                    setTimeout(() => this.loadCommentList(), 1100);
                } else {
                    this.$swal.fire({ title: 'Error', text: res.data.msg, icon: 'error', showConfirmButton: false, timer: 1100 });
                }
            }).catch(err => { console.error('Error ->', err); });
        },
        toggleReplyInput(comment) {
            this.replyText = `Reply to ${comment.userName}…`;
            comment.showReplyInput = comment.showReplyInput == null ? true : !comment.showReplyInput;
        },
        toggleReplyInput1(comment) {
            comment.replyInputStatus = comment.replyInputStatus == null ? true : !comment.replyInputStatus;
        },
        submitReply(comment) {
            if (!this.replyContent) { this.$message('Reply cannot be empty'); return; }
            this.$axios.post('evaluations/insert', { contentType: this.contentType, content: this.replyContent, contentId: this.contentId, parentId: comment.id }).then(res => {
                if (res.data.code == 200) {
                    this.replyContent = ''; comment.showReplyInput = false;
                    this.$swal.fire({ title: 'Reply', text: 'Reply posted', icon: 'success', showConfirmButton: false, timer: 1300 });
                    setTimeout(() => this.loadCommentList(), 1300);
                } else { this.$swal.fire({ title: 'Error', text: res.data.msg, icon: 'error', showConfirmButton: false, timer: 1100 }); }
            }).catch(err => { console.error('Error ->', err); });
        },
        submitReply1(comment) {
            if (!this.replyChildContent) { this.$message('Reply cannot be empty'); return; }
            this.$axios.post('evaluations/insert', { replierId: comment.userId, contentType: this.contentType, content: this.replyChildContent, contentId: this.contentId, parentId: comment.parentId }).then(res => {
                if (res.data.code == 200) {
                    this.replyChildContent = ''; comment.replyInputStatus = false;
                    this.$swal.fire({ title: 'Reply', text: 'Reply posted', icon: 'success', showConfirmButton: false, timer: 1300 });
                    setTimeout(() => this.loadCommentList(), 1300);
                } else { this.$swal.fire({ title: 'Error', text: res.data.msg, icon: 'error', showConfirmButton: false, timer: 1100 }); }
            }).catch(err => { console.error('Error ->', err); });
        },
        loadCommentList() {
            this.$axios.get(`evaluations/list/${this.contentId}/${this.contentType}`).then(res => {
                if (res.data.code == 200) {
                    this.commentList = res.data.data.data;
                    this.evaluationsCount = res.data.data.evaluationsCount;
                    this.commentList.forEach(e => {
                        e.time = timeAgo(e.createTime);
                        e.commentChildVOS.forEach(c => { c.time = timeAgo(c.createTime); });
                    });
                }
            }).catch(err => { console.error('Error ->', err); });
        },
    }
};
</script>

<style lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

/* ─── Section header ─────────────────────────────────── */
.comment-header {
    font-family: 'Klee One', cursive;
    font-size: 18px;
    font-weight: 600;
    color: #2a2018;
    margin: 0 0 20px 0;
    display: flex;
    align-items: center;
    gap: 8px;

    i { color: #c8392b; font-size: 18px; }
}

.comment-count {
    font-size: 14px;
    color: #8a7d6e;
    font-weight: 400;
}

/* ─── New comment row ────────────────────────────────── */
.comment-input-row {
    display: flex;
    gap: 12px;
    margin-bottom: 28px;
    align-items: flex-start;
}

.input-avatar { flex-shrink: 0; margin-top: 2px; }

.input-box {
    flex: 1;
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    background-color: #fdfaf5;
    padding: 10px 12px 8px;
    transition: border-color 0.2s;

    &.focused { border-color: #c8392b; background-color: #ffffff; }
}

.comment-textarea,
.reply-textarea {
    width: 100%;
    min-height: 56px;
    font-family: 'DM Sans', sans-serif;
    font-size: 14px;
    color: #2a2018;
    background: transparent;
    border: none;
    outline: none;
    resize: none;
    padding: 0;
    display: block;

    &::placeholder { color: #c0b09e; }
}

.input-footer {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 10px;
    margin-top: 6px;
}

.char-count {
    font-family: 'DM Sans', sans-serif;
    font-size: 11px;
    color: #b0a898;
}

.submit-btn {
    display: inline-block;
    padding: 5px 16px;
    font-family: 'Klee One', cursive;
    font-size: 12px;
    font-weight: 600;
    border-radius: 20px;
    cursor: pointer;
    background-color: #d6c9b8;
    color: #fdf8f2;
    transition: background-color 0.15s;
    user-select: none;

    &.active {
        background-color: #c8392b;
        &:hover { background-color: #b03226; }
    }
}

/* ─── Comment list ───────────────────────────────────── */
.comment-list { display: flex; flex-direction: column; gap: 0; }

.comment-item {
    padding: 16px 0;
    border-bottom: 1px solid #f0ebe0;

    &:last-child { border-bottom: none; }
}

.comment-main { display: flex; gap: 12px; }

.c-avatar { flex-shrink: 0; margin-top: 2px; }

.c-body { flex: 1; min-width: 0; }

.c-meta-top {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-bottom: 5px;
    flex-wrap: wrap;
}

.c-username {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #3a3028;
}

.my-tag {
    font-family: 'DM Sans', sans-serif;
    font-size: 11px;
    padding: 1px 8px;
    border-radius: 20px;
    background-color: rgba(200, 57, 43, 0.08);
    color: #c8392b;
    border: 1px solid rgba(200, 57, 43, 0.2);
}

.reply-arrow {
    font-size: 12px;
    color: #b0a898;
    margin: 0 2px;
}

.c-content {
    font-family: 'DM Sans', sans-serif;
    font-size: 15px;
    color: #2a2018;
    line-height: 1.6;
    margin-bottom: 8px;
    word-break: break-word;
}

.c-actions {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 14px;
}

.c-time {
    font-family: 'DM Sans', sans-serif;
    font-size: 12px;
    color: #b0a898;
}

.c-action {
    font-family: 'DM Sans', sans-serif;
    font-size: 12px;
    color: #9a8d7e;
    cursor: pointer;
    display: inline-flex;
    align-items: center;
    gap: 4px;
    user-select: none;
    transition: color 0.15s;

    &:hover { color: #5a5045; }

    &.like { &:hover { color: #c8392b; } }
    &.like.liked { color: #c8392b; }

    &.danger { &:hover { color: #c8392b; } }
}

/* ─── Reply input box ────────────────────────────────── */
.reply-input-wrap {
    margin-top: 12px;
    border: 1.5px solid #c8392b;
    border-radius: 4px;
    background-color: #ffffff;
    padding: 10px 12px 8px;
}

/* ─── Child comments ─────────────────────────────────── */
.child-list {
    margin-top: 12px;
    padding: 12px 14px;
    background-color: rgba(255,255,255,0.55);
    border: 1.5px solid #f0ebe0;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.child-item {
    display: flex;
    gap: 10px;
    align-items: flex-start;

    .c-avatar.small { margin-top: 2px; }
}

/* ─── Report dialog ──────────────────────────────────── */
.food-dialog {
    border-radius: 4px !important;
    border: 1.5px solid #e8ddd0 !important;

    .el-dialog__header { display: none !important; }
    .el-dialog__body { padding: 0 !important; }
    .el-dialog__footer { border-top: 1.5px solid #e8ddd0 !important; padding: 14px 20px !important; }
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
    margin: 0 0 18px 0;
}

.report-group { margin-bottom: 16px; }

.dialog-label {
    font-family: 'Klee One', cursive;
    font-size: 13px;
    font-weight: 600;
    color: #5a5045;
    margin-bottom: 8px;
    display: block;
}

.report-options { display: flex; flex-wrap: wrap; gap: 8px; }

.report-option {
    display: inline-block;
    padding: 5px 14px;
    background-color: rgba(255,255,255,0.7);
    border: 1.5px solid #d6c9b8;
    border-radius: 4px;
    font-family: 'DM Sans', sans-serif;
    font-size: 13px;
    color: #5a5045;
    cursor: pointer;
    transition: border-color 0.15s, color 0.15s;
    user-select: none;

    &:hover { border-color: #c8392b; color: #c8392b; }
    &.selected { background-color: #c8392b; border-color: #c8392b; color: #fdf8f2; }
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