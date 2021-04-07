const router = new VueRouter({
	routes: [

		{
			path: '/',
			component: amazon,
			meta: {
				title: "亚马逊",
				content: ""
			}
		}
		
	]
	});
router.beforeEach((to, from, next) => {
    /* 路由发生变化修改页面meta */
    if (to.meta.content) {
        let head = document.getElementsByTagName('head');
        let meta = document.createElement('meta');
        meta.content = to.meta.content;
        head[0].appendChild(meta)
    }
    /* 路由发生变化修改页面title */
    if (to.meta.title) {
        document.title = to.meta.title;
    }
    next()
});