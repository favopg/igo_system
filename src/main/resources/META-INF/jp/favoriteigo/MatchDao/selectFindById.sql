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
    id = /* id */99