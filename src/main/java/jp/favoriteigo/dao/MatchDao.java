package jp.favoriteigo.dao;

import jp.favoriteigo.entity.MatchEntity;
import jp.favoriteigo.service.MatchService;
import org.seasar.doma.*;

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
    public Map<String, Object> selectFindById(int id);

    @Select
    public List<Map<String, Object>> selectFindByUserId(int userId);

    @Insert
    public int insertMatch(MatchEntity entity);

    @BatchInsert()
    public int[] insertCsv(List<MatchEntity> matchEntityLists);

    @BatchDelete(sqlFile = true)
    public int[] deleteMatch(List<MatchEntity> matchEntityList);

    @Update(sqlFile = true)
    public int updateMatch(MatchEntity entity);

}
