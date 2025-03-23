package jp.favoriteigo;

import jp.favoriteigo.common.SessionInfo;
import jp.favoriteigo.form.MatchForm;
import jp.favoriteigo.service.MatchService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatchServiceTest {

    @Test
    void registerSuccessTest() {
    	MatchService service = new MatchService();
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

        JSONObject response = service.register(input, sessionInfo);
        assertEquals("success", response.optString("status"));
    }

    @Test
    void registerErrorTest() {
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
}
