// ここからはメニュー部分
const naviSidever = Vue.createApp({
  data() {
    return {};
  },
});

naviSidever.component("navi_sideber", {
	template: `
		<div class="sidebar-content">
    		<h3 class="text-white mb-4">メニュー</h3>
            	<ul class="nav flex-column mb-auto">
            		<li class="nav-item mb-2">
                    	<a href="#" class="nav-link text-white">
                        	<i class="bi bi-list-ul me-2"></i>対戦一覧
                    	</a>
                	</li>
                	<li class="nav-item mb-2">
                    	<a href="#" class="nav-link text-white">
                        	<i class="bi bi-plus-circle me-2"></i>対戦登録
                    	</a>
                	</li>
                	<li class="nav-item mb-2">
                    	<a href="#" class="nav-link text-white">
                        	<i class="bi bi-file-earmark-arrow-up me-2"></i>CSV取込
                    	</a>
                	</li>
            	</ul>
    	</div>
    	<button class="btn btn-danger w-100">
            <i class="bi bi-box-arrow-right me-2"></i>ログアウト
        </button>
	`,
});
naviSidever.mount('#sidebar')


// ここからはコンテンツ部分
const app = Vue.createApp({
  data() {
    return {
      message: "ここに対戦一覧のコンテンツが入ります。",
    };
  },
  methods: {
    change() {
      this.message = "切り替えます";
    },
  },
});

// コンポーネントの登録
app.component("greeting", {
  template: `
        <div>
            <h1>{{ message }}</h1>
            <button @click="changeMessage">メッセージを変更</button>
        </div>
    `,
  data() {
    return {
      message: "こんにちは、Vue 3 コンポーネント!",
    };
  },
  methods: {
    changeMessage() {
      this.message = "メッセージが変更されました！";
    },
  },
});
app.mount("#content");
