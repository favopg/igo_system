package jp.favoriteigo;

import jp.favoriteigo.common.SessionInfo;
import jp.favoriteigo.dao.MatchDao;
import jp.favoriteigo.entity.MatchEntity;
import jp.favoriteigo.form.MatchForm;
import jp.favoriteigo.service.MatchService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchDao dao;

    @InjectMocks
    private MatchService service = new MatchService();

    /**
     * 登録処理正常確認
     * registerメソッド実行時に登録OKの場合に正常レスポンスが返却できること
     */
    @Test
    void registerSuccessTest() {
        MatchForm input = new MatchForm();
        input.setBlackName("ロボット");
        input.setWhiteName("イッシー");
        input.setResult("黒勝ち");
        input.setResultLink("https://gokifu.net/t2.php?s=2391735432155926");
        input.setMatchAt(new Date(System.currentTimeMillis()));
        input.setPublicFlag(true);
        input.setComment("良いコメント");
        input.setCreatedUserId(14);

        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setName("ロボット");
        sessionInfo.setUserId(14);

        // Daoの登録処理が呼ばれた時は正常値を返す
        doReturn(1).when(dao).insertMatch(any(MatchEntity.class));

        JSONObject response = service.register(input, sessionInfo);
        assertEquals("success", response.optString("status"));
    }

    @Test
    void registerCheckErrorTest() {
        MatchService service = new MatchService();
        MatchForm input = new MatchForm();
        input.setBlackName("aa");
        input.setWhiteName("bb");
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setName("イッシー");
        sessionInfo.setUserId(1);

        JSONObject response = service.register(input, sessionInfo);
        assertEquals("error", response.optString("status"));
        assertEquals("黒番、白番のいずれかが登録されていないユーザです。ご確認ください", response.optString("message"));
        
    }

    /**
     * daoの登録処理で1件以外の場合はエラーレスポンスが返却できること
     */
    @Test
    void registerErrorTest() {
        // 入力チェックに引っかからないデータを設定
        MatchForm input = new MatchForm();
        input.setBlackName("イッシー");
        input.setWhiteName("ロボット");
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setName("ロボット");
        sessionInfo.setUserId(14);

        // Daoの登録処理は0を返す
        doReturn(0).when(dao).insertMatch(any(MatchEntity.class));

        JSONObject response = service.register(input, sessionInfo);
        assertEquals("error", response.optString("status"));
        assertEquals("対戦成績登録処理で失敗しました", response.optString("message"));

    }

}
