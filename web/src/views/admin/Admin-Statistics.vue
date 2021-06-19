<template>
  <div>
    <a-row>
      <a-col :span="24">
        <a-card>
          <a-row>
            <a-col :span="8">
              <a-statistic title="Total view" :value="statistic.viewCount">
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="Total Vote" :value="statistic.voteCount">
                <template #suffix>
                  <like-outlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="Vote rate" :value="statistic.voteCount / statistic.viewCount * 100"
                           :precision="2"
                           suffix="%"
                           :value-style="{ color: '#cf1322' }">
                <template #suffix>
                  <like-outlined />
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <br>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic title="Today View" :value="statistic.todayViewCount" style="margin-right: 50px">
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic title="Today vote" :value="statistic.todayVoteCount">
                <template #suffix>
                  <like-outlined />
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic
                title="Expected to read today"
                :value="statistic.todayViewIncrease"
                :value-style="{ color: '#0000ff' }"
              >
                <template #suffix>
                  <UserOutlined />
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic
                title="Expected reading growth today"
                :value="statistic.todayViewIncreaseRateAbs"
                :precision="2"
                suffix="%"
                class="demo-class"
                :value-style="statistic.todayViewIncreaseRate < 0 ? { color: '#3f8600' } : { color: '#cf1322' }"
              >
                <template #prefix>
                  <arrow-down-outlined v-if="statistic.todayViewIncreaseRate < 0"/>
                  <arrow-up-outlined v-if="statistic.todayViewIncreaseRate >= 0"/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <br>
    <a-row>
      <a-col :span="24" id="main-col">
        <div id="main" style="width: 100%;height:300px;"></div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, onMounted } from 'vue'
  import axios from 'axios';

  declare let echarts: any;

  export default defineComponent({
    name: 'AdminStatistics',
    setup () {
      const statistic = ref();
      statistic.value = {};
      const getStatistic = () => {
        axios.get('/statistic').then((response) => {
          const data = response.data;
          if (data.success) {
            const statisticResp = data.content;
            if(statisticResp.length == 2){
              statistic.value.viewCount = statisticResp[1].viewCount;
              statistic.value.voteCount = statisticResp[1].voteCount;
              statistic.value.todayViewCount = statisticResp[1].viewIncrease;
              statistic.value.todayVoteCount = statisticResp[1].voteIncrease;

              // Calculate the current time point in minutes, as a percentage of a day
              const now = new Date();
              const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
              statistic.value.todayViewIncrease = parseInt(String(statisticResp[1].viewIncrease / nowRate));
              // todayViewIncreaseRate
              const den = statisticResp[0].viewIncrease == 0? 1: statisticResp[0].viewIncrease;
              statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease - statisticResp[0].viewIncrease) / den;
              statistic.value.todayViewIncreaseRateAbs = Math.abs(statistic.value.todayViewIncreaseRate);
            }else{
              statistic.value.viewCount = statisticResp[0].viewCount;
              statistic.value.voteCount = statisticResp[0].voteCount;
              statistic.value.todayViewCount = statisticResp[0].viewIncrease;
              statistic.value.todayVoteCount = statisticResp[0].voteIncrease;

              // Calculate the current time point in minutes, as a percentage of a day
              const now = new Date();
              const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
              statistic.value.todayViewIncrease = parseInt(String(statisticResp[0].viewIncrease / nowRate));
              // todayViewIncreaseRate
              statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease) / 1;
              statistic.value.todayViewIncreaseRateAbs = Math.abs(statistic.value.todayViewIncreaseRate);
            }
          }
        });
      };

      const init30DayEcharts = (list: any) => {
        // 发布生产后出现问题：切到别的页面，再切回首页，报表显示不出来
        // 解决方法：把原来的id=main的区域清空，重新初始化
        const mainDom = document.getElementById('main-col');
        if (mainDom) {
          mainDom.innerHTML = '<div id="main" style="width: 100%;height:300px;"></div>';
        }
        // 基于准备好的dom，初始化echarts实例
        const myChart = echarts.init(document.getElementById('main'));

        const xAxis = [];
        const seriesView = [];
        const seriesVote = [];
        for (let i = 0; i < list.length; i++) {
          const record = list[i];
          xAxis.push(record.date);
          seriesView.push(record.viewIncrease);
          seriesVote.push(record.voteIncrease);
        }

        // 指定图表的配置项和数据
        const option = {
          title: {
            text: '30天趋势图'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['总阅读量', '总点赞量']
          },
          grid: {
            left: '1%',
            right: '3%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xAxis
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '总阅读量',
              type: 'line',
              // stack: '总量', 不堆叠
              data: seriesView,
              smooth: true
            },
            {
              name: '总点赞量',
              type: 'line',
              // stack: '总量', 不堆叠
              data: seriesVote,
              smooth: true
            }
          ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
      };

      const get30DayStatistic = () => {
        axios.get('/statistic/month').then((response) => {
          const data = response.data;
          if (data.success) {
            const statisticList = data.content;

            init30DayEcharts(statisticList)
          }
        });
      };

      // const testEcharts = () => {
      //   // 基于准备好的dom，初始化echarts实例
      //   const myChart = echarts.init(document.getElementById('main'));
      //
      //   // 指定图表的配置项和数据
      //   const option = {
      //     title: {
      //       text: 'ECharts 入门示例'
      //     },
      //     tooltip: {},
      //     legend: {
      //       data:['销量']
      //     },
      //     xAxis: {
      //       data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
      //     },
      //     yAxis: {},
      //     series: [{
      //       name: '销量',
      //       type: 'bar',
      //       data: [5, 20, 36, 10, 10, 20]
      //     }]
      //   };
      //
      //   // 使用刚指定的配置项和数据显示图表。
      //   myChart.setOption(option);
      // };

      onMounted(() => {
        getStatistic();
        // testEcharts();
        get30DayStatistic();
      });

      return {
        statistic
      }
    }
  });
</script>
<style scoped>
  .tip {
    padding: 10px 5px;
    margin-bottom: 20px;
    border: 1px solid transparent;
    background: linear-gradient(white,white) padding-box,repeating-linear-gradient(-45deg, black 0, black 25%, white 0, white 50%) 0/.6em .6em;
    animation:ants 12s linear infinite;
  }
  .tip b{
    color: red;
  }
</style>
