INSERT INTO users(
    username,
    password_hash,
    public_flag,
    created_at,
    updated_at
)
VALUES(
    ?,
    ?,
    ?,
    now(),
    now()
)