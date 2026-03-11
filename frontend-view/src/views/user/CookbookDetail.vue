<template>
    <div style="background-color: rgb(255,255,255); border-radius: 2px; padding: 20px;">
        <div style="font-size: 30px; font-weight: bold;">{{ cookbook.title }}</div>
        <div style="margin-top: 5px; font-size: 15px; border-bottom: 2px solid rgb(225,225,225); padding-block:10px">
            <span style="margin-right: 10px;">
                Category: {{ cookbook.categoryName }}
            </span>
            <span>
                Created at: {{ cookbook.createTime }}
            </span>
        </div>
        <div v-html="cookbook.content"> </div>
        <div>
            <Evaluations v-if="cookbook.id" :contentId="cookbook.id" contentType="COOKBOOK" />
        </div>
    </div>
</template>
<script>
import Evaluations from "@/components/Evaluations"
export default {
    components: {
        Evaluations
    },
    name: "Cookbook Detail",
    data() {
        return {
            cookbook: {}
        }
    },
    created() {
        this.loadCookbookDetail();
    },
    methods: {
        loadCookbookDetail() {
            const jsonCookbook = sessionStorage.getItem('cookbookInfo');
            this.cookbook = JSON.parse(jsonCookbook);
        },
    }
};
</script>
<style scoped lang="scss"></style>
