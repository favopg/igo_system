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
                  "public_flag": 1,
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
                  "public_flag": 1,
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
                  "public_flag": 1,
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
                  "public_flag": 1,
                  "match_at": "2025-01-19"
                }
            ],
            totalPage:0,
            delCheckList:[],
            displayCount:2,
            currentPage:1
        };
    },
    computed: {
        delChecked() {
            console.log('チェックボックス値', this.delCheckList)
        },
        // ページ数制御
        calcTotalPageLink() {
            this.totalPage = Math.ceil(this.records.length / this.displayCount)
            return this.totalPage
        },

        // 対戦一覧表示件数制御
        displayMatches() {
            const start = (this.currentPage - 1) * this.displayCount;
            const end = start + this.displayCount;
            return this.records.slice(start, end);
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
        }
    },

});
matchList.mount('#matchList')








