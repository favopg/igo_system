select
    (
        select
            id
        from
            rosters r1
        where
            r1.name = ?
    ) as black_player_id,
    (
        select
            id
        from
            rosters r2
        where
            r2.name = ?
    ) as white_player_id
from
    rosters
where
    name in(?, ?)