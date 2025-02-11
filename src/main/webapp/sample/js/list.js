// ここからモーダル部品
const grade = Vue.createApp({
    data() {
        return {
            gradeInfo: {
                blackVictory: '10',
                whiteVictory: '20'
            },
        };
    },

});
grade.mount('#grade')


// ここから対戦一覧部品
const matchList = Vue.createApp({
    data() {
        return {
            records: [
                {
                    "black_name": "イッシー",
                    "result": "白中押勝ち",
                    "result_link": "https://gokifu.net/t2.php?s=4481733974860716",
                    "white_name": "木部夏生",
                    "created_user_id": 14,
                    "comment": "楽しかった！",
                    "id": 1,
                    "public_flag": false,
                    "match_at": "2024-11-01"
                },
                {
                    "black_name": "ロボット",
                    "result": "黒勝ち",
                    "result_link": "https://gokifu.net/t2.php?s=2391735432155926",
                    "white_name": "イッシー",
                    "created_user_id": 14,
                    "comment": "良いコメント",
                    "id": 2,
                    "public_flag": true,
                    "match_at": "2025-01-19"
                },
                {
                    "black_name": "ロボット",
                    "result": "黒勝ち",
                    "result_link": "https://gokifu.net/t2.php?s=2391735432155926",
                    "white_name": "イッシー",
                    "created_user_id": 14,
                    "comment": "良いコメント",
                    "id": 3,
                    "public_flag": true,
                    "match_at": "2025-01-19"
                },
                {
                    "black_name": "ロボット",
                    "result": "黒勝ち",
                    "result_link": "https://gokifu.net/t2.php?s=2391735432155926",
                    "white_name": "イッシー",
                    "created_user_id": 14,
                    "comment": "良いコメント",
                    "id": 4,
                    "public_flag": false,
                    "match_at": "2025-01-19"
                }
            ],
            totalPage:0,
            delCheckList:[],
            displayCount:10,
            currentPage:1,
            selectedKeyWord:"1",
            inputKeyword:"",
            loginId:14,
            errorMessage: ""
        };
    },
    computed: {
        delChecked() {
            console.log('チェックボックス値', this.delCheckList)
        },
        // ページ数制御
        calcTotalPageLink() {
            this.totalPage = Math.ceil(this.searchFilter.length / this.displayCount)
            return this.totalPage
        },

        // 対戦一覧表示件数制御
        displayMatches() {
            const start = (this.currentPage - 1) * this.displayCount;
            const end = start + Number(this.displayCount)
            return this.searchFilter.slice(start, end);
        },

        // キーワード検索を行った後に選択検索を行う
        searchFilter() {
            if (this.selectedKeyWord === '1') {
                // ログインIDと一致するデータのみ表示
                return this.keywordFilter.filter((record) => record.created_user_id === this.loginId)
            }
            // 公開されているデータのみ表示
            return this.keywordFilter.filter((record) => record.public_flag === true)
        },

        // レコード(黒番、白番、対戦日)に対してキーワード検索を行う
        keywordFilter() {
            if (!this.inputKeyword) {
                return this.records
            }
            const query = this.inputKeyword.toLowerCase();
            return this.records.filter((record) =>
                record.black_name.toLowerCase().includes(query) ||
                record.white_name.toLowerCase().includes(query) ||
                record.match_at.toLowerCase().includes(query)
            )
        },

    },
    methods: {
        // ページ遷移 TODO 算出プロパティは無理・・・？
        pageTransition(page) {
            this.currentPage = page
        },
        // 前へ
        prevPage() {
            if(this.currentPage > 1) {
                this.currentPage--
            }
            return this.currentPage
        },

        // 次へ
        nextPage() {
            if(this.currentPage < this.totalPage) {
                this.currentPage++
            }
            return this.currentPage
        },

        // 対戦一覧初期表示
        displayInitMatches() {
            // APIコール
            callApi('igo_system', 'GET', null)
                .then(response => {
                    if (!response.ok) {
                        this.errorMessage = 'APIコールエラーが発生しました'
                    }
                    console.log("API返却値", response)
                    return response.json()
                })
                .then(data => {
                    const responseJson = JSON.stringify(data, null, 2);
                    const parsedData = JSON.parse(responseJson);
                    this.records = parsedData.records;
                })
                .catch(error => {
                    console.log("エラーです")
                    this.errorMessage = 'システムエラーが発生しました'
                })
        },
        // 削除ボタン押下
        openDeleteModal(modalId) {
            //showModal(modalId)
            const modal = new bootstrap.Modal(document.getElementById("deleteModal"));
            modal.show();
        },

        // 削除APIを呼ぶ
        deleteApi() {
            // APIコール
            callApi('igo_system', 'DELETE', this.delCheckList)
                .then(response => {
                    if (!response.ok) {
                        this.errorMessage = 'APIコールエラーが発生しました'
                    }
                    console.log("API返却値", response)
                    return response.json()
                })
                .then(data => {
                    this.displayInitMatches()
                    this.delCheckList = []
                })
                .catch(error => {
                    this.errorMessage = 'システムエラーが発生しました'
                });
        }

    },
    mounted() {
        this.displayInitMatches()

    },

});
matchList.mount('#igoApp')








