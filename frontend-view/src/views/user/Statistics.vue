<template>
  <div> 
     <div class="item-type">
        <span :style="{
            backgroundColor: item.id===typeItem.id? 'rgb(235,235,235)':'',
            color: typeItem.id===typeItem.id?'rgb(0,0,0)':''
        }"
         @click="typeSelected(typeItem)" v-for="(typeItem,index) in interactionTypes" :key="index">{{typeItem.name}}</span>
     </div>
    <LineChart @on-selected="onSelected" tag="Streaming Data" height="500px" :values="values" :date="dates" />
  </div>
</template>
<script>
import LineChart from "@/components/LineChart.vue"
export default {
    components:{LineChart},
    name: "Statistics",
    data() {
        return {
            interactionQueryDto:{day:365,type: null},
            values: [],
            dates: [],
            interactionTypes:[],
            item:{}
        }
    },
    created() {
        this.fetchStatisticDay();
        this.fetchInteractionTypes();
    },
    methods: {
        typeSelected(typeItem){
            this.item=typeItem;
            this.interactionQueryDto.type = typeItem.id;
            this.fetchStatisticDay();
        },
        fetchInteractionTypes(){
            this.$axios.get(`/interaction/types`).then(res => {
                const { data } = res;
                if (data.code === 200) {
                   this.interactionTypes = data.data;
                //拿到类型后，默认选中第一个
                if (this.interactionTypes.length > 0) {
                    this.item = this.interactionTypes[0];
                    this.interactionQueryDto.type = this.interactionTypes[0].id;
                }
                this.fetchStatisticDay(); //有了type再查
                }
            }).catch(error => {
                console.log("Error", error);
            });
        },
        onSelected(day){
            this.interactionQueryDto.day=day;
            this.fetchStatisticDay();
        },     
        fetchStatisticDay(){
            this.$axios.post(`/interaction/daysQuery`,this.interactionQueryDto).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    const datas = data.data;
                    this.values = datas.map(entity=>entity.count);
                    this.dates = datas.map(entity=>entity.name)
                }
            }).catch(error => {
                console.log("Error", error);
            });
        }
    }
};
</script>
<style scoped lang="scss">
.item-type{
    margin-block: 20px;
    display:flex;
    justify-content: left;
    gap: 10px;
    span{
        cursor:pointer;
        font-size:16px;
    }
}
</style>
