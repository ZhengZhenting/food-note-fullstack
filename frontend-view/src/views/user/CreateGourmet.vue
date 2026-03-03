<template>
    <div class="container">
        <div class="top">
            <el-tooltip class="item" effect="dark" content="Back" placement="bottom">
                <span class="last-page" @click="goBack">
                    <i class="el-icon-back"></i>
                </span>
                <el-button>bottom</el-button>
            </el-tooltip>
            <span class="text">Publish Gourmet</span>
        </div>
        <div class="bottom">
            <div style="background-color: rgb(255,255,255);">
                <div style="padding-block:10px;">
                    <input type="text" class="input-title" placeholder="Title" v-model="gourmet.title">
                </div>
                <div style="border-bottom: 2px solid rgb(234,243,243);">
                    <Editor :receiveContent="gourmet.content" @on-receive="onReceive" />
                </div>
                <div style="display: flex; justify-content: left; align-items: center; gap: 20px; line-height: 100px;">
                    <span>
                        * Category:
                    </span>
                    <el-select style="width: 200px;" size="small" v-model="gourmet.categoryId"
                        placeholder="category" clearable>
                        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </div>
                <div style="display: flex; justify-content: left; align-items: center; gap: 20px; line-height: 100px;">
                    <p>*Cover:</p>
                    <el-upload class="cover-uploader"
                        action="http://localhost:21090/api/food-share-sys/v1.0/file/upload" :show-file-list="false"
                        :on-success="handleCoverSuccess">
                        <img v-if="cover" :src="cover" style="width: 180px; height: 130px;">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </div>
                <div style="display: flex; justify-content: left; align-items: center; gap: 20px; line-height: 100px;">
                    <p>*Public:</p>
                    <el-switch v-model="gourmet.isPublish" active-color="#13ce66" inactive-color="#3c3f41">
                    </el-switch>
                </div>
                <div style="margin-block: 30px;">
                    <el-button @click="postGourmet" plain>PUBLISH</el-button>
                </div>
            </div>
        </div>

    </div>
</template>
<script>
import Editor from "@/components/Editor"
export default {
    components: { Editor },
    name: "CreateGourmet",
    data() {
        return {
            gourmet: {},
            categories: [],
            cover: null,

        }
    },
    created() {
        this.fetchFreshCategories();
    },
    methods: {
        // 发布美食
        postGourmet() {
            this.gourmet.cover = this.cover;
            this.$axios.post('/gourmet/save', this.gourmet).then(response => {
                if (response.data.code === 200) {
                    this.$notify({
                        duration: 1000,
                        title: 'Gourmet Published',
                        type: 'success'
                    });
                    this.goBack();
                }
            }).catch(error => {
                console.log('Error:', error);
            });
        },
        // 封面上传回调函数
        handleCoverSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: 'Cover Upload',
                message: res.code === 200 ? 'Upload Success' : 'Upload Failed',
                type: res.code === 200 ? 'success' : 'error'
            });
            // 上传成功则更新封面
            if (res.code === 200) {
                this.cover = res.data;
            }
        },
        fetchFreshCategories() {
            this.$axios.post('/category/query', {}).then(response => {
                if (response.data.code === 200) {
                    this.categories = response.data.data;
                    this.categories.unshift({ id: null, name: 'All' })
                }
            }).catch(error => {
                console.log('Error:', error);
            });
        },
        // 返回上一页
        goBack() {
            this.$router.go(-1);
        },
        onReceive(content) {
            // 接收 Editor 组件传递的内容
            this.gourmet.content = content;
        }
    }
};
</script>
<style scoped lang="scss">
.container {
    background-color: rgb(248, 248, 248);
    min-height: 100vh;

    .bottom {
        margin-top: 6px;
    }

    .top {
        line-height: 64px;
        padding-inline: 20px;
        background-color: rgb(255, 255, 255);

        .text {
            font-size: 18px;
            font-weight: 500;
            margin-left: 10px;
        }

        .last-page:hover {
            background-color: rgb(232, 232, 232);
        }
    }

    .last-page {
        backgroun-color: rgb(245, 245, 245);
        padding: 2px 3px;
        border-radius: 12px;
        border: 1px solid rgb(231, 231, 231);
        cursor: pointer;
    }
}
</style>
