package jp.example;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;

import java.util.List;
import java.util.Map;

/**
 * MatchDao
 * 対戦テーブルを操作するDAOクラス
 *
 * @author イッシー
 * @version 1.0
 * @see MatchService
 * @since 1.0
 */
@Dao
public interface MatchDao {

    @Select
    public List<Map<String, Object>> selectFindByUserId(int userId);

    @Insert
    public int insertMatch(MatchEntity entity);
}
