// ここからモーダル部品
const modal = Vue.createApp({
    data() {
        return {
        };
    },
});

// 確認用モーダルと処理中のモーダル
modal.component("modal", {
    props: ['title', 'detail', 'dangerInfo', 'primaryInfo', 'isConfirm', 'modalId', 'screenId'],
    methods: {
        // 画面遷移
        changeScreen(screenId) {
            console.log('画面遷移',screenId)
            window.location.href = screenId;
        },
        handleClick(info) {
        }
    },
    template: `
        <!-- 処理中の場合はバックグラウンドでモーダルを消せないように設定  -->
        <div class="modal fade" :id="modalId" tabindex="-1" :data-bs-backdrop="isConfirm ? null : 'static'" :data-bs-keyboard="isConfirm ? null : false" aria-labelledby="confirmModalLabel" aria-hidden="true">
            <div :class="isConfirm? 'modal-dialog' : 'modal-dialog modal-dialog-centered'">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" :id="(modalId + 'Label')" >{{ title}}</h1>
                        <button type="button" v-if="isConfirm" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <!-- モーダルbody -->
                    <div class="modal-body">
                        <div v-html="detail"></div>
                        <!-- 処理中用のスピナー -->
                        <div v-if="!isConfirm" class="d-flex justify-content-center">
                            <div class="spinner-border" role="status">
                                <span class="visually-hidden">Loading...</span>
                            </div>
                        </div>
                    </div>
                    <!-- モーダルfooter -->
                    <div class="modal-footer">
                        <!-- 確認モーダル用のOK,NOボタン -->
                        <button type="button" v-if="isConfirm" class="btn btn-secondary" data-bs-dismiss="modal">{{ dangerInfo }}</button>
                        <button type="button" v-if="isConfirm" class="btn btn-primary" @click="changeScreen(screenId)">{{ primaryInfo}}</button>
                        <!-- 処理中用のプログレスバー -->
                        <div v-if="!isConfirm" class="progress w-100">
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
            matchInfo: {
                blackName:"",
                whiteName:"",
                result:"",
                resultLink:"",
                matchAt:"",
                comment:"",
                publicFlag:false,
                id:0
            },
            errorInfo: {
                blackName:"",
                whiteName:"",
                result:"",
                resultLink:"",
                matchAt:"",
                comment:"",
                publicFlag:false,
                errorMessage:""
            },
        }
    },
    methods: {
        // 登録処理
        register(modalId) {
            this.validateForm()
            if (this.errorInfo.isValidated) {
                // バリデーションエラー時は後続処理は実施しない
                return;
            }

            // 登録処理中モーダルを表示する
            const modalInfo = showModal(modalId)

            // APIコール
            callApi('igo_system', 'POST', this.matchInfo)
                .then(response => {
                    if (!response.ok) {
                        this.errorInfo.errorMessage = 'APIコールエラーが発生しました'
                    }
                    console.log("API返却値", response)
                    return response.json()
                })
                .then(data => {
                    // モーダルを閉じる
                    closeModal(modalInfo.modalElement, modalInfo.modal)
                })
                .then(()=> {
                    // 閉じた後に次のモーダルを開く
                    isHideModal(modalInfo.modalElement, modalInfo.modal)
                })
                .catch(error => {
                    closeModal(modalInfo.modalElement, modalInfo.modal)
                    console.error('エラー:', error);
                });
        },

        // 更新処理
        update(modalId) {
            this.validateForm()
            if (this.errorInfo.isValidated) {
                // バリデーションエラー時は後続処理は実施しない
                return;
            }

            // 登録処理中モーダルを表示する
            const modalInfo = showModal(modalId)

            // APIコール
            callApi('igo_system', 'PUT', this.matchInfo)
                .then(response => {
                    if (!response.ok) {
                        this.errorInfo.errorMessage = 'APIコールエラーが発生しました'
                    }
                    console.log("API返却値", response)
                    return response.json()
                })
                .then(data => {
                    // モーダルを閉じる
                    closeModal(modalInfo.modalElement, modalInfo.modal)
                })
                .then(()=> {
                    // 閉じた後に次のモーダルを開く
                    isHideModal(modalInfo.modalElement, modalInfo.modal)
                })
                .catch(error => {
                    closeModal(modalInfo.modalElement, modalInfo.modal)
                    console.error('エラー:', error);
                });
        },

        selectMatch() {
            const urlParams = new URLSearchParams(window.location.search);
            console.log("値チェック" + urlParams.get("id"))
            console.log("値チェック" + urlParams.has("id"))

            if(!urlParams.has("id")) {
                return;
            }
            fetch("igo_system/refer?" + urlParams.toString())
                .then(response => {
                    if (!response.ok) {
                        throw new Error('HTTP error ' + response.status);
                    }
                    return response.json();
                })
                .then(data => {
                    const responseJson = JSON.stringify(data, null, 2);
                    const parsedData = JSON.parse(responseJson);
                    this.matchInfo.blackName = parsedData.black_name
                    this.matchInfo.whiteName = parsedData.white_name
                    this.matchInfo.result = parsedData.result
                    this.matchInfo.resultLink = parsedData.result_link
                    this.matchInfo.id = parsedData.id
                    if (parsedData.public_flag === 0) {
                        this.matchInfo.publicFlag = false
                    } else {
                        this.matchInfo.publicFlag = true
                    }
                    this.matchInfo.comment = parsedData.comment
                    this.matchInfo.matchAt = parsedData.match_at

                })
                .catch(error => {
                    console.error("データ取得に失敗しました")
                })
        },

        // バリデーションチェック
        validateForm() {
            this.errorInfo = {
                blackName: "",
                whiteName: "",
                result: "",
                resultLink: "",
                comment: "",
                matchAt:"",
                publicFlag:false,
                isValidated: false,
                errorMessage:""
            };

            // 黒番必須チェック
            if (this.matchInfo.blackName.trim() === "") {
                this.errorInfo.blackName = "黒番はログインIDか対戦相手を入力してください。";
                this.errorInfo.isValidated = true;
            }

            if (this.errorInfo.blackName.trim() === "") {
                // 黒番：20文字チェック
                if (this.matchInfo.blackName.length > 20) {
                    this.errorInfo.blackName = "黒番は20文字以内で入力してください。";
                    this.errorInfo.isValidated = true;
                }
            }
            // 白番必須チェック
        },
    },
    mounted() {
        this.selectMatch()
    }
});


formInput.component('forminput',{
    props: ["labelFor", "itemName", "type", "placeholder", "errorMessage", "className", "icon", "modelValue"],
    template: `
        <div id="form-input" class="mb-1 col-md-8">
            <label :for="labelFor" class="form-label"><i class="me-1" :class="[className,icon]"></i>{{ itemName }}</label>
            <input :type="type" class="form-control" :id="labelFor" :placeholder="placeholder" 
            @input="$emit('update:modelValue', $event.target.value)">
        </div>
        <div v-if="errorMessage" class="text-danger mb-3 bi-x-circle-fill">
            {{ errorMessage }}
        </div>
    `,
});

// ラジオボタン
formInput.component('formradio',{
    props: ["labelFor", "itemName", "checked", "value", "name"],
    template: `
        <div class="form-check form-check-inline mb-3">
            <input :id="labelFor" class="form-check-input" :name="name" type="radio" :checked ="checked">
            <label :for="labelFor" class="form-check-label">
                {{ itemName }}
            </label>
        </div>
    `,
});

// テキストエリア
formInput.component('formtextarea',{
    props: ["labelFor", "itemName", "placeholder", "errorMessage", "className", "icon", "modelValue", "rows"],
    template: `
        <div class="mb-4 col-md-8">
            <label :for="labelFor" class="form-label"><i class="me-1" :class="[className,icon]"></i>{{ itemName }}</label>
            <textarea class="form-control" :id="labelFor" :rows="rows" :placeholder="placeholder" @input="$emit('update:modelValue', $event.target.value)"></textarea>
        </div>
        <div v-if="errorNassage" class="text-danger mb-3 bi-x-circle-fill">
            {{ errorMessage  }}
        </div>
    `,
});

formInput.mount("#form-input")

// fetchAPIでServletをコールする
function callApi(endpoint, method, request) {
    if (!request) {
        return fetch(endpoint)
    }
    const fetchData = fetch(endpoint,
        {
            method: method,
            headers: {
                "Content-Type": "application/json",
            },
            body:JSON.stringify(request)
        }
    )
    return fetchData
}

async function selectMatch() {
    const urlParams = new URLSearchParams(window.location.search);
    console.log("値チェック" + urlParams.get("id"))
    console.log("値チェック" + urlParams.has("id"))

    if(!urlParams.has("id")) {
        return;
    }
    try {
        const response = await fetch("igo_system/refer?" + urlParams.toString())
        const data = await response.json()
        return data
    } catch (error) {
        console.error('更新情報取得APIでシステムエラー', error);
        return '';
    }
}


// モーダルを閉じる
function closeModal(modalElement, modal) {

    modalElement.addEventListener('shown.bs.modal', function () {
        modal.hide()
    })
}

// モーダルを表示する
function showModal(modalId) {
    /** モーダルDOM */
    const modalElement = document.getElementById(modalId)
    // BootstrapのModalクラスを初期化
    const modal = new bootstrap.Modal(modalElement)
    // モーダルを表示
    modal.show()

    return {
        modalElement: modalElement,
        modal : modal
    }
}

// モーダルが完全に閉じたのを検知してからモーダルを開く
function isHideModal(modalElement, modal) {

    // モーダルの完全に閉じた後に次を処理
    modalElement.addEventListener('hidden.bs.modal', () => {
            showModal('confirmModal')
    })
}
