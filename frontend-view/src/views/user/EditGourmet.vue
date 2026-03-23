<template>
    <div class="create-container">

        <!-- ── Page header ── -->
        <div class="page-header">
            <span class="back-btn" @click="goBack">
                <i class="el-icon-back"></i>
            </span>
            <h2 class="page-title">Modify Gourmet</h2>
        </div>

        <!-- ── Form ── -->
        <div class="form-body">

            <!-- Title -->
            <div class="form-section">
                <input
                    type="text"
                    class="input-title"
                    placeholder="Recipe title…"
                    v-model="gourmet.title"
                />
            </div>

            <!-- Editor -->
            <div class="form-section editor-section">
                <label class="form-label">Content</label>
                <div class="editor-wrap">
                    <Editor :receiveContent="gourmet.content" @on-receive="onReceive" />
                </div>
            </div>

            <!-- Category -->
            <div class="form-section form-row">
                <label class="form-label">Category <span class="required">*</span></label>
                <el-select
                    style="width: 200px;"
                    size="small"
                    v-model="gourmet.categoryId"
                    placeholder="Select category"
                    clearable
                >
                    <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
            </div>

            <!-- Cover -->
            <div class="form-section form-row">
                <label class="form-label">Cover Image <span class="required">*</span></label>
                <el-upload
                    class="cover-uploader"
                    action="http://localhost:21090/api/food-share-sys/v1.0/file/upload"
                    :show-file-list="false"
                    :on-success="handleCoverSuccess"
                >
                    <img v-if="cover" :src="cover" class="cover-preview" />
                    <div v-else class="cover-placeholder">
                        <i class="el-icon-plus"></i>
                        <span>Upload Cover</span>
                    </div>
                </el-upload>
            </div>

            <!-- Public toggle -->
            <div class="form-section form-row">
                <label class="form-label">Visibility <span class="required">*</span></label>
                <div class="toggle-wrap">
                    <el-switch
                        v-model="gourmet.isPublish"
                        active-color="#c8392b"
                        inactive-color="#d6c9b8"
                    />
                    <span class="toggle-label">{{ gourmet.isPublish ? 'Public' : 'Private' }}</span>
                </div>
            </div>

            <!-- Submit -->
            <div class="form-section">
                <span class="publish-btn" @click="updateGourmet">
                    <i class="el-icon-check"></i> Save Changes
                </span>
            </div>

        </div>
    </div>
</template>

<script>
import Editor from "@/components/Editor"
export default {
    components: { Editor },
    name: "ModifyGourmet",
    data() {
        return {
            gourmet: {},
            categories: [],
            cover: null,
        }
    },
    created() {
        this.fetchFreshCategories();
        this.loadGourmetId();
    },
    methods: {
        loadGourmetId() {
            const gourmetId = sessionStorage.getItem('gourmetId');
            this.fetchGourmetById(Number(gourmetId));
        },
        fetchGourmetById(gourmetId) {
            this.$axios.get(`/gourmet/${gourmetId}`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.gourmet = data.data[0];
                    this.cover = this.gourmet.cover;
                }
            }).catch(error => { console.log('Error', error); });
        },
        updateGourmet() {
            this.gourmet.cover = this.cover;
            this.$axios.put('/gourmet/update', this.gourmet).then(response => {
                if (response.data.code === 200) {
                    this.$notify({ duration: 1000, title: 'Gourmet Modified', type: 'success' });
                    this.goBack();
                }
            }).catch(error => { console.log('Error:', error); });
        },
        handleCoverSuccess(res) {
            this.$notify({
                duration: 1500,
                title: 'Cover Upload',
                message: res.code === 200 ? 'Upload Success' : 'Upload Failed',
                type: res.code === 200 ? 'success' : 'error'
            });
            if (res.code === 200) this.cover = res.data;
        },
        fetchFreshCategories() {
            this.$axios.post('/category/query', {}).then(response => {
                if (response.data.code === 200) {
                    this.categories = response.data.data;
                    this.categories.unshift({ id: null, name: 'All' });
                }
            }).catch(error => { console.log('Error:', error); });
        },
        goBack() { this.$router.go(-1); },
        onReceive(content) { this.gourmet.content = content; }
    }
};
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Caveat:wght@600;700&family=Klee+One:wght@600&family=DM+Sans:wght@400;500&display=swap');

* { box-sizing: border-box; }

.create-container {
    max-width: 860px;
    margin: 0 auto;
    padding: 28px 24px 60px;
    font-family: 'DM Sans', sans-serif;
}

.page-header {
    display: flex;
    align-items: center;
    gap: 14px;
    margin-bottom: 28px;
}

.back-btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 34px;
    height: 34px;
    border: 1.5px solid #d6c9b8;
    border-radius: 4px;
    color: #5a5045;
    cursor: pointer;
    font-size: 16px;
    flex-shrink: 0;
    transition: border-color 0.15s, color 0.15s, background-color 0.15s;

    &:hover {
        border-color: #c8392b;
        color: #c8392b;
        background-color: rgba(200,57,43,0.05);
    }
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

.form-body {
    display: flex;
    flex-direction: column;
}

.form-section {
    padding: 20px 0;
    border-bottom: 1px solid #e8ddd0;
    &:last-child { border-bottom: none; }
}

.form-row {
    display: flex;
    align-items: center;
    gap: 20px;
}

.form-label {
    font-family: 'Klee One', cursive;
    font-size: 14px;
    font-weight: 600;
    color: #5a5045;
    white-space: nowrap;
    min-width: 110px;
}

.required {
    color: #c8392b;
    margin-left: 2px;
}

.input-title {
    width: 100%;
    height: 52px;
    font-family: 'Klee One', cursive;
    font-size: 24px;
    font-weight: 600;
    color: #2a2018;
    background: transparent;
    border: none;
    border-bottom: 2px solid #e8ddd0;
    outline: none;
    padding: 0 4px;
    transition: border-color 0.2s;

    &::placeholder { color: #c8bfb0; }
    &:focus { border-bottom-color: #c8392b; }
}

.editor-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    display: flex;
}

.editor-wrap {
    width: 100%;
    border: 1.5px solid #e8ddd0;
    border-radius: 4px;
    overflow: hidden;
    &:focus-within { border-color: #c8392b; }
}

.form-section {
    ::v-deep .el-input__inner {
        border-color: #d6c9b8;
        border-radius: 4px;
        font-family: 'DM Sans', sans-serif;
        font-size: 14px;
        color: #3a3028;
        background-color: rgba(255,255,255,0.7);

        &::placeholder { color: #c0b09e; }
        &:focus { border-color: #c8392b; }
    }
}

.cover-uploader {
    ::v-deep .el-upload {
        border: 1.5px dashed #d6c9b8;
        border-radius: 4px;
        cursor: pointer;
        overflow: hidden;
        transition: border-color 0.15s;
        display: block;
        &:hover { border-color: #c8392b; }
    }
}

.cover-preview {
    width: 200px;
    height: 148px;
    object-fit: cover;
    display: block;
}

.cover-placeholder {
    width: 200px;
    height: 148px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 8px;
    color: #b0a898;
    font-family: 'Klee One', cursive;
    font-size: 13px;

    i { font-size: 26px; }
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

.publish-btn {
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