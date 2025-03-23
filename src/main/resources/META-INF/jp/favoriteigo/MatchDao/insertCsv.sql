INSERT INTO matches(
    id,
    black_name,
    white_name,
    result,
    result_link,
    match_at,
    comment,
    public_flag,
    created_user_id
)
VALUES(
    /* matchEntityLists.id */0,
    /* matchEntityLists.blackName */'黒番',
    /* matchEntityLists.whiteName */'白番',
    /* matchEntityLists.result */'結果',
    /* matchEntityLists.resultLink */'棋譜URL',
    /* matchEntityLists.matchAt */'2021-01-01',
    /* matchEntityLists.comment */'コメント',
    /* matchEntityLists.publicFlag */true,
    /* matchEntityLists.createdUserId */0
)
ON DUPLICATE KEY UPDATE
    black_name = VALUES(black_name),
    white_name = VALUES(white_name),
    result = VALUES(result),
    result_link = VALUES(result_link),
    match_at = VALUES(match_at),
    comment = VALUES(comment),
    public_flag = VALUES(public_flag),
    created_user_id = VALUES(created_user_id)