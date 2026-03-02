<template>
    <div class="container">
        <div class="top">
            <el-tooltip class="item" effect="dark" content="Back" placement="bottom">
                <span class="last-page" @click="goBack">
                <i class="el-icon-back"></i>
                </span>
                <el-button>bottom</el-button>
            </el-tooltip>
            
        </div>
        <div class="bottom">

        </div>
        
    </div>
</template>
<script>
export default {
    name: "CreateGourmet",
    data() {
        return {

        }
    },
    created() {

    },
    methods: {
        // 返回上一页
        goBack() {
            this.$router.go(-1);
        },
    }
};
</script>
<style scoped lang="scss">
.container{
    background-color:rgb(248,248,248);
    min-height: 100vh;
    .top{line-height: 64px;
        padding-inline: 20px;
        background-color: rgb(255,255,255);
    .last-page:hover{
            background-color: rgb(232,232,232);}}
        .last-page{
            backgroun-color: rgb(245,245,245);
            padding: 2px 3px;
            border-radius: 12px;
            border: 1px solid rgb(231,231,231);
            cursor: pointer;
        }
}
</style>
