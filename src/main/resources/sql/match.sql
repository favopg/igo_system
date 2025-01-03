select
    id,
    (
        select
            shops.name
        from
            shops
        where
            shops.id = matches.shop_id
    ) as shops_name,
    (
        select
            concat(rosters.name, rosters.chess_ability)
        from
            rosters
        where
            rosters.id = matches.black_id
    ) as black_name,
    (
        select
            concat(rosters.name, rosters.chess_ability)
        from
            rosters
        where
            rosters.id = matches.white_id
    ) as white_name,
    result,
    result_link,
    match_at
from
    matches
where
    matches.shop_id = 1