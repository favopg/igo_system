CREATE TABLE users (
	id TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
    username VARCHAR(20) NOT NULL,       -- ユーザ名（全角20文字まで）
    password_hash CHAR(65) NOT NULL,    -- パスワード（ハッシュ化済み、固定長65文字）
    public_flag SMALLINT NOT NULL CHECK (public_flag IN (0, 1)), -- 公開/非公開フラグ
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,     -- レコード作成日時
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL      -- レコード更新日時
);
