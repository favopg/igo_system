INSERT INTO users(
    name,
    password,
    public_flag,
    chess_ability,
    created_at,
    updated_at
)
VALUES(
    ?,
    ?,
    ?,
    ?,
    now(),
    now()
)