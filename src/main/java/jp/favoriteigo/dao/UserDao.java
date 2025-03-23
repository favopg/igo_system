package jp.favoriteigo.dao;

import jp.favoriteigo.entity.UserEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;

import java.util.Map;

@Dao
public interface UserDao {

    @Select
    Map<String, Object> selectFindByName(String name);

    @Insert
    int insert(UserEntity userEntity);
}
