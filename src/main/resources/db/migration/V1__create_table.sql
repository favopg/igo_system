/** ユーザテーブル */
CREATE TABLE IF NOT EXISTS users (
	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,                  -- 主キー
    name VARCHAR(20) NOT NULL,                                   -- ユーザ名（全角20文字まで）
    password CHAR(65) NOT NULL,                                  -- パスワード（ハッシュ化済み、固定長65文字）
    chess_ability VARCHAR(20) NOT NULL,                          -- 棋力(例：六段)
    chess_ability_kbn CHAR(1) NOT NULL,                          -- 棋力区分(0:アマ 1:プロ)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,     -- レコード作成日時
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL      -- レコード更新日時
);

/** 対戦テーブル */
CREATE TABLE IF NOT EXISTS matches (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,                  -- 主キー
    black_name VARCHAR(20) NOT NULL,                             -- 黒番の名前
    white_name VARCHAR(20) NOT NULL,                             -- 白番の名前
    result VARCHAR(15) NOT NULL,                                  -- 対戦結果（例：6目半勝ち）
    result_link VARCHAR(255),                                    -- 棋譜のURL
    match_at DATE ,                                              -- 対戦日(YYYY-MM-DD)
    comment VARCHAR(30),                                         -- 一言コメント
    public_flag SMALLINT NOT NULL CHECK (public_flag IN (0, 1)), -- 公開/非公開フラグ
    created_user_id INT UNSIGNED NOT NULL,                       -- レコード登録者ID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,     -- レコード作成日
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL, -- レコード更新日
    FOREIGN KEY (created_user_id) REFERENCES users(id) ON DELETE CASCADE
);