--#REPEAT entity : entities
INSERT INTO matches(
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
          /* black_name */
          'イッシー',
          /* white_name */
          'ロボット',
          /* result */
          '黒勝ち',
          /* result_link */
          'http:',
          /* match_at */
          '2025-01-01',
          /* comment */
          'コメント',
          /* public_flag */
          false,
          /* created_user_id */
          0
      )
    ON DUPLICATE KEY UPDATE
                         black_name =
                     VALUES(
                         black_name
                         ),
                         white_name =
                     VALUES(
                         white_name
                         ),
                         result =
                     VALUES(
                         result
                         ),
                         result_link =
                     VALUES(
                         result_link
                         ),
                         match_at =
                     VALUES(
                         match_at
                         ),
                         comment =
                     VALUES(
                         comment
                         ),
                         pubic_flag =
                     VALUES(
                         public_flag
                         ),
                         created_user_id =
                     VALUES(
                         created_user_id
                         )
;
--#END