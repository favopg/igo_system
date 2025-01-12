package jp.example;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;

import java.util.Map;

@Dao
public interface UserDao {

    @Select
    //@Sql("SELECT * FROM users WHERE name = /*name*/''")
    Map<String, Object> selectFindByName(String name);
}
