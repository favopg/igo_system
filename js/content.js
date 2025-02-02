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
    props: ['title', 'detail', 'danger_info', 'primary_info'],
    template: `
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="confirmModalLabel">{{ title}}</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    {{ detail }}
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">{{ danger_info }}</button>
                    <button type="button" class="btn btn-primary">{{ primary_info}}</button>
                </div>
            </div>
        </div>
    `,
  });
modal.mount('#confirmModal')
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
        register() {
            this.error_info.black_name = "黒番はログインIDか対戦相手のいずれかを入力してください"
            console.log("黒番の値" + this.match_info.black_name)
            console.log("白番の値" + this.match_info.white_name)
            console.log("公開非公開" + this.match_info.public_flag)

        }
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