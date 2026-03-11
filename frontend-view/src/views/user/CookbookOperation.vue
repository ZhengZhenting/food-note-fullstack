<template>
    <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-tabs type="border-card">
        <el-tab-pane label="My Cookbook">
            <MyCookbook/>
        </el-tab-pane>
        <el-tab-pane label="My Nutriment">
            <MyCookbookNutriment/>
        </el-tab-pane>
        </el-tabs>
    </el-row>
</template>
<script>
import MyCookbook from "@/views/user/MyCookbook"
import MyCookbookNutriment from "@/views/user/MyCookbookNutriment"
export default {
    components: {MyCookbook,MyCookbookNutriment},
    name: "CookbookOperation",
    data() {
        return {

        }
    },
    created() {

    },
    methods: {

    }
};
</script>
<style scoped lang="scss"></style>
