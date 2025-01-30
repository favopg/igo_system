package jp.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MatchDaoTest
 * 対戦DAOテストクラス
 *
 * @author イッシー
 * @version 1.0
 * @since 1.0
 */
public class MatchDaoTest {

    static Config config = null;
    static MatchDao dao = null;
    static TransactionManager transaction = null;
    static UserEntity testData = null;

    @BeforeAll
    static void init() {
        config = new AppConfig();
        dao = new MatchDaoImpl(config);
        transaction = config.getTransactionManager();
    }

    @Test
    void selectFindById() {
        transaction.required(() -> {
            // 削除実行
           Map<String, Object> res = dao.selectFindById(1);
           assertEquals(1L, res.get("id"));
        });

    }

    @Test
    void deleteTest() {
        ArrayList<MatchEntity> matches = new ArrayList<>();
        MatchEntity entity = new MatchEntity();
        entity.setId(1);
        matches.add(entity);

        entity = new MatchEntity();
        entity.setId(2);
        matches.add(entity);

        assertThrows(RuntimeException.class, () -> {
            transaction.required(() -> {
                // 削除実行
                dao.deleteMatch(matches);
                // 検索検証
                List<Map<String, Object>> result = dao.selectFindByUserId(1);

                List<Map<String, Object>> deleteList = result.stream()
                        .filter(row -> Integer.parseInt(row.get("id").toString()) == 1 &&
                                Integer.parseInt(row.get("id").toString()) == 2)
                        .toList();
                assertTrue(deleteList.isEmpty());
                // 検索検証後にロールバックする
                throw new RuntimeException("ロールバック");
            });
        });
    }
}
