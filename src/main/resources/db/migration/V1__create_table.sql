CREATE TABLE users (
	id TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, -- 主キー
    name VARCHAR(20) NOT NULL,                               -- ユーザ名（全角20文字まで）
    password CHAR(65) NOT NULL,                                  -- パスワード（ハッシュ化済み、固定長65文字）
    chess_ability VARCHAR(10) NOT NULL,                          -- 棋力(例：六段
    public_flag SMALLINT NOT NULL CHECK (public_flag IN (0, 1)), -- 公開/非公開フラグ
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,     -- レコード作成日時
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL      -- レコード更新日時
);

CREATE TABLE matches (
    id TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, -- 主キー
    black_id TINYINT UNSIGNED NOT NULL,             -- 黒番の名簿ID
    white_id TINYINT UNSIGNED NOT NULL,             -- 白番の名簿ID
    result VARCHAR(255) NOT NULL,                   -- 対戦結果（例：6目半勝ち）
    result_link VARCHAR(255),                       -- 棋譜のURL
    match_at DATE ,                                 -- 対戦日
    comment VARCHAR(255),                           -- コメント
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- レコード作成日
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL -- レコード更新日
);