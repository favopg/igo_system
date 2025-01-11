select
    id,
    (
        select
            concat(users.name, users.chess_ability)
        from
            users
        where
            users.id = matches.black_id
    ) as black_name,
    (
        select
            concat(users.name, users.chess_ability)
        from
            users
        where
            users.id = matches.white_id
    ) as white_name,
    result,
    result_link,
    comment,
    match_at
from
    matches