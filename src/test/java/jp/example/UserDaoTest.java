package jp.example;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.tx.LocalTransaction;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDaoTest {


    @Test
    void domaSelectTest() throws SQLException {

        Config config = new AppConfig();
        UserDao dao = new UserDaoImpl(config);

        TransactionManager transaction = config.getTransactionManager();
        transaction.required(() -> {
            // トランザクション内で SQL を実行する処理
            // SQL クエリがログに出力されます
            Map<String, Object> result = dao.selectFindByName("イッシー");
            assertEquals("イッシー", result.get("name").toString());
        });
    }

}
