select
    id,
    black_name,
    white_name,
    result,
    result_link,
    match_at,
    comment,
    public_flag,
    created_user_id
from
    matches
where
    created_user_id = /* userId */99
union
select
    id,
    black_name,
    white_name,
    result,
    result_link,
    match_at,
    comment,
    public_flag,
    created_user_id
from
    matches
where
    public_flag = 1 AND
    NOT EXISTS(
        select 1
        from matches
        where created_user_id = /* userId */99
    )