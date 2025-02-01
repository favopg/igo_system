// ここからはメニュー部分
const naviSidever = Vue.createApp({
  data() {
    return {
    };
  },
});

naviSidever.component("navi_sideber", {
    props: ['activekbn'],
    template: `
		<div class="sidebar-content">
    		<h3 class="text-white mb-4">メニュー</h3>
              <ul class="nav flex-column mb-auto nav-pills">
            		<li class="nav-item mb-2">
                    	<a href="#" class="nav-link text-white" :class="activekbn === '1' ? 'active':'' ">
                        	<i class="bi bi-list-ul me-2"></i>対戦一覧
                    	</a>
                	</li>
                	<li class="nav-item mb-2">
                    	<a href="#" class="nav-link text-white" :class="activekbn === '2' ? 'active':'' ">
                        	<i class="bi bi-plus-circle me-2"></i>対戦登録
                    	</a>
                	</li>
                	<li class="nav-item mb-2">
                    	<a href="#" class="nav-link text-white" :class="activekbn === '3' ? 'active':'' ">
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

// ここからモーダル部品
const modal = Vue.createApp({
    data() {
      return {
        // モーダルのデフォルト情報
        modalinfo: {
            title:"削除",
            detail:"選択した行を削除します。よろしいですか？",
            dangerinfo:"いいえ",
            primaryinfo:"はい"
        }
      };
    },
  });
  
  modal.component("modal", {
      props: ['modalinfo'],
      template: `
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="confirmModalLabel">{{modalinfo.title}}</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    {{modalinfo.detail}}
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">{{ modalinfo.dangerinfo }}</button>
                    <button type="button" class="btn btn-primary">{{ modalinfo.primaryinfo}}</button>
                </div>
            </div>
        </div>
      `,
  });
  modal.mount('#confirmModal')
  // ここまでモーダル
  


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
