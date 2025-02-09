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
      };
    },
  });

// 確認用モーダルと処理中のモーダル
modal.component("modal", {
    props: ['title', 'detail', 'danger_info', 'primary_info', 'is_confirm'],
    template: `
        <!-- 処理中の場合はバックグラウンドでモーダルを消せないように設定  -->
        <div class="modal fade" id="confirmModal" tabindex="-1" :data-bs-backdrop="is_confirm ? null : 'static'" :data-bs-keyboard="is_confirm ? null : false" aria-labelledby="confirmModalLabel" aria-hidden="true">
            <div :class="is_confirm ? 'modal-dialog' : 'modal-dialog modal-dialog-centered'">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="confirmModalLabel">{{ title}}</h1>
                        <button type="button" v-if="is_confirm" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <!-- モーダルbody -->
                    <div class="modal-body">
                        {{ is_confirm ? detail : '' }}
                        <!-- 処理中用のスピナー -->
                        <div v-if="!is_confirm" class="d-flex justify-content-center">
                            <div class="spinner-border" role="status">
                                <span class="visually-hidden">Loading...</span>
                            </div>
                        </div>
                    </div>
                    <!-- モーダルfooter -->
                    <div class="modal-footer">
                        <!-- 確認モーダル用のOK,NOボタン -->
                        <button type="button" v-if="is_confirm" class="btn btn-secondary" data-bs-dismiss="modal">{{ danger_info }}</button>
                        <button type="button" v-if="is_confirm" class="btn btn-primary">{{ primary_info}}</button>
                        <!-- 処理中用のプログレスバー -->
                        <div v-if="!is_confirm" class="progress w-100">
                            <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width: 100%;" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"></div>
                        </div>
                    </div>                
                </div>
            </div>
        </div>
    `,
  });
modal.mount('#igo_modal')
// ここまでモーダル
  
// ここから入力フォーム
const formInput = Vue.createApp({
    data() {
        return {
            match_info: {
                black_name:"",
                white_name:"",
                result:"",
                result_link:"",
                match_at:"",
                comment:"",
                public_flag:"0"
            },
            error_info: {
                black_name:"",
                white_name:"",
                result:"",
                result_link:"",
                match_at:"",
                comment:"",
                public_flag:"0"
            }
        }
    },
    methods: {
        // 登録処理
        register() {
            this.validateForm()
            /** モーダル呼び出しのサンプル 
            const modalElement = document.getElementById('confirmModal');
			// Bootstrap の Modal クラスを初期化
			const modal = new bootstrap.Modal(modalElement);
			// モーダルを表示
			modal.show();
            */
        },
        // バリデーションチェック
        validateForm() {
            this.error_info = {
                black_name: "",
                white_name: "",
                result: "",
                result_link: "",
                comment: "",
                match_at:"",
                public_flag:"0",
                is_validated: false
            };

            // 黒番必須チェック
            if (this.match_info.black_name.trim() === "") {
				this.error_info.black_name = "黒番はログインIDか対戦相手を入力してください。";
				this.error_info.is_validated = true;
            }

            if (this.error_info.black_name.trim() === "") {
                // 黒番：20文字チェック
                if (this.match_info.black_name.length > 20) {
                    this.error_info.black_name = "黒番は20文字以内で入力してください。";
                    this.error_info.is_validated = true;
                }
            }

            // 白番必須チェック
            

        },        
    },
});

formInput.component('forminput',{
    props: ["label_for", "item_name", "type", "placeholder", "error_message", "class_name", "icon", "modelValue"],
    template: `
        <div id="form-input" class="mb-1 col-md-8">
            <label :for="label_for" class="form-label"><i class="me-1" :class="[class_name,icon]"></i>{{ item_name }}</label>
            <input :type="type" class="form-control" :id="label_for" :placeholder="placeholder" 
            @input="$emit('update:modelValue', $event.target.value)">
        </div>
        <div v-if="error_message" class="text-danger mb-3 bi-x-circle-fill">
            {{ error_message }}
        </div>
    `,  
});

// ラジオボタン
formInput.component('formradio',{
    props: ["label_for", "item_name", "checked", "value", "name"],
    template: `
        <div class="form-check form-check-inline mb-3">
            <input :id="label_for" class="form-check-input" :name="name" type="radio" :checked ="checked" :value="value">
            <label :for="label_for" class="form-check-label">
                {{ item_name }}
            </label>
        </div>
    `,  
});

// テキストエリア
formInput.component('formtextarea',{
    props: ["label_for", "item_name", "placeholder", "error_message", "class_name", "icon", "modelValue", "rows"],
    template: `
        <div class="mb-4">
            <label :for="label_for" class="form-label"><i class="me-1" :class="[class_name,icon]"></i>{{ item_name }}</label>
            <textarea class="form-control" :id="label_for" :rows="rows" :placeholder="placeholder" @input="$emit('update:modelValue', $event.target.value)"></textarea>
        </div>
        <div v-if="error_massage" class="text-danger mb-3 bi-x-circle-fill">
            {{ error_message  }}
        </div>
    `,  
});

formInput.mount("#form-input")