package jp.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    static Config config = null;
    static UserDao dao = null;
    static TransactionManager transaction = null;
    static UserEntity testData = null;

    @BeforeAll
    static void init() {
        config = new AppConfig();
        dao = new UserDaoImpl(config);
        transaction = config.getTransactionManager();
        // テストデータ1件分作成
        testData = new UserEntity();
        testData.setName("イッシー");
        String hashedPassword = BCrypt.hashpw("test",BCrypt.gensalt());
        testData.setPassword(hashedPassword);
        testData.setChessAbility("六段");
        testData.setChessAbilityKbn("0");
    }

    @Test
    void domaSelectTest() {
        assertThrows(RuntimeException.class, () -> {
            transaction.required(() -> {
                // 1件登録できることを確認
                dao.insert(testData);
                // 検索検証
                Map<String, Object> result = dao.selectFindByName("イッシー");
                assertEquals("イッシー", result.get("name").toString());
                boolean isPasswordMatch = BCrypt.checkpw(result.get("password").toString(), "test");
                assertTrue(isPasswordMatch);
                assertEquals("六段", result.get("chess_ability").toString());
                assertEquals("0", result.get("chess_ability_kbn").toString());
                // 検索検証後にロールバックする
                throw new RuntimeException("ロールバック");
            });
        });
    }

    @Test
    void userInsertTest() {
        assertThrows(RuntimeException.class, () -> {
            transaction.required(() -> {
                // 1件登録できることを確認
                assertEquals(1, dao.insert(testData));
                // 登録後にロールバックする
                throw new RuntimeException("ロールバック");
            });
        });
    }
}
