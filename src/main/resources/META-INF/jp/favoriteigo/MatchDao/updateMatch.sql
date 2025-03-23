UPDATE matches
SET
    black_name = /* entity.blackName */'黒番',
    white_name = /* entity.whiteName */'白番',
    result = /* entity.result */'結果',
    result_link = /* entity.resultLink */'棋譜URL',
    match_at = /* entity.matchAt */'対戦日',
    comment = /* entity.comment */'一言コメント',
    public_flag = /* entity.publicFlag */'公開/非公開'
WHERE
    id = /* entity.id */0 AND
    created_user_id = /* entity.createdUserId */0
