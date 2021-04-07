let amazon={
	template:`
	<div>
	<el-row :gutter="20">
		<el-col :span="6">
			内容：<el-input v-model="search" placeholder="搜索内容"></el-input>
		</el-col>
		<el-col :span="6">
			页数：<el-input v-model="page" placeholder="搜索页数" type="number"></el-input>
		</el-col>
		<el-col :span="6">
			线程：<el-input v-model="thread" placeholder="线程数" type="number"></el-input>
		</el-col>
		<el-col :span="6">
			<el-button type="primary" style=" margin-top:20px" @click="run()">运行</el-button>
		</el-col>
	</el-row>
	
	<el-table v-loading="loading"
      :data="tableData"
      style="width: 100%">
      <el-table-column
        prop="title"
        label="标题"
        width="180">
      </el-table-column>
      <el-table-column
        prop="price"
        label="价格"
        width="180">
      </el-table-column>
	  <el-table-column
        prop="description"
        label="描述">
		<template slot-scope="scope">
			<el-tooltip class="item" effect="dark" :content="scope.row.description" placement="top">
	      		<el-button>显示</el-button>
		    </el-tooltip>
		</template>
      </el-table-column>
	 <el-table-column
        prop="inventory"
        label="货存">
      </el-table-column>
	  <el-table-column
        prop="score"
        label="星级">
      </el-table-column>
	  <el-table-column
        prop="ratings"
        label="评分等级">
      </el-table-column>
	 <el-table-column
        prop="images"
        label="小图">
		<template slot-scope="scope">
			<el-image v-for="img in scope.row.images" :key="img" :src="img" lazy></el-image>   
      	</template>
      </el-table-column>
	  <el-table-column
        prop="image"
        label="图片">
		<template slot-scope="scope">
			<el-image
		      style="width: 100px; height: 100px"
		      :src="scope.row.image"></el-image>     
      	</template>
      </el-table-column>
	  <el-table-column
        prop="url"
        label="详情">
		<template slot-scope="scope">
			<el-link :href="scope.row.url" target="_blank" type="primary">点击跳转</el-link>        
      	</template>
      </el-table-column>
    </el-table>
	<div>
	`,
	data(){
		return {
			search:'',
			page:1,
			thread:4,
			tableData:[],
			loading:false
		}
	},
	methods:{
		run(){
			this.loading=true;
			axios.post(`/amazon/list/${this.search}/${this.page}/${this.thread}`).then(resp=>{
				this.tableData=resp.data;
				this.loading=false;
			})
		}
	}
}