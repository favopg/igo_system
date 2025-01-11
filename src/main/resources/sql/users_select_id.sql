select
    (
        select
            id
        from
            users u1
        where
            u1.name = ?
    ) as black_player_id,
    (
        select
            id
        from
            users u2
        where
            u2.name = ?
    ) as white_player_id
from
    users
where
    name in(?, ?)