package jp.example;

import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns = {"/sample/igo_system", "/sample/igo_system/refer"})
public class MatchServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MatchServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {

        try {
            res.setContentType("text/html; charset=UTF-8");
            res.setCharacterEncoding("UTF-8");

            String path = req.getServletPath();
            logger.debug("リクエストパス{}", path);

            switch (path) {
                case "/sample/igo_system/refer":
                    refer(req, res);
                    break;

                default:
                    list(req, res);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            execError(res);
        }
    }

    /**
     * 編集用の対戦データを取得します。
     * @param req リクエスト
     * @param res レスポンス
     * @throws Exception 例外発生時にスローします。
     */
    private void refer(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッション情報取得
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new RuntimeException("セッション情報取得でエラー発生");
        }

        SessionInfo sessionInfo = (SessionInfo) session.getAttribute("sessionInfo");
        logger.debug("セッション情報{}", sessionInfo.toString());

        logger.debug("選択された対戦id{}", req.getParameter("id"));

        // 対戦データ全取得
        MatchService service = new MatchService();
        JSONObject response = service.getMatch(Integer.parseInt(req.getParameter("id")));

        logger.debug("対戦一覧結果一覧：{}", response.toString());

        // APIレスポンス返却
        ServletUtil.apiResponse(res, response);

    }

    /**
     * 対戦一覧データ取得します
     * @param req リクエスト
     * @param res　レスポンス
     */
    private void list(HttpServletRequest req, HttpServletResponse res) {

        // セッション情報取得
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new RuntimeException("セッション情報取得でエラー発生");
        }

        SessionInfo sessionInfo = (SessionInfo) session.getAttribute("sessionInfo");
        logger.debug("セッション情報{}", sessionInfo.toString());

        // 対戦データ全取得
        MatchService service = new MatchService();
        JSONObject response = service.getMatchList(sessionInfo.getUserId());

        logger.debug("対戦一覧結果一覧：{}", response.toString());

        // APIレスポンス返却
        ServletUtil.apiResponse(res, response);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        // register.htmlから登録ボタン押下時に呼ばれる
        try {
            res.setContentType("text/html; charset=UTF-8");
            res.setCharacterEncoding("UTF-8");
            // リクエスト情報変換
            MatchForm form = ServletUtil.convertFormFromJson(req, MatchForm.class);
            logger.debug("対戦結果登録APIの変換後データ{}", form.toString());

            // セッション情報が取れない場合はAPIエラーとして返却する
            HttpSession session = req.getSession(false);
            if (req.getSession() == null) {
                throw new RuntimeException("セッション情報取得でエラー発生");
            }

            // バリデーションチェック
            JSONObject validateResponse = ValidateUtil.validate(form);
            if (validateResponse.optString(ApiResponse.STATUS.getCode()).equals(ApiResponse.NG.getCode())) {
                ServletUtil.apiResponse(res, validateResponse);
                logger.debug("対戦結果登録APIのバリデーションチェックエラー{}", validateResponse.toString());
                return;
            }

            SessionInfo sessionInfo = (SessionInfo) session.getAttribute("sessionInfo");
            logger.debug("セッション情報{}",sessionInfo.toString());

            // 対戦登録処理
            MatchService service = new MatchService();
            JSONObject response = service.register(form, sessionInfo);

            ServletUtil.apiResponse(res, response);

        } catch (Exception e) {
            logger.debug("対戦登録処理でエラー発生");
            logger.error(e.getMessage(), e);
            execError(res);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
        try {
            res.setContentType("text/html; charset=UTF-8");
            res.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            int[] ids = objectMapper.readValue(req.getInputStream(),int[].class);
            logger.debug("削除選択された対戦件数{}", ids.length);

            MatchService service = new MatchService();
            JSONObject response = service.delete(ids);

            ServletUtil.apiResponse(res, response);

        } catch (Exception e) {
            throw new RuntimeException("削除APIでエラーが発生しました",e);
        }
    }

    private void execError(HttpServletResponse res) {
        try {
            PrintWriter out = res.getWriter();
            JSONObject response = new JSONObject();
            response.put(ApiResponse.STATUS.getCode(), ApiResponse.NG.getCode());
            response.put("message", "システムエラーが発生しました");

            out.print(response.toString());
            out.flush();
            logger.error("APIエラーレスポンス{}", response.toString());
            logger.error("対戦一覧取得APIでエラーが発生しました");
        } catch (Exception e) {
            throw new RuntimeException("ここでエラー起きたらもうおしまいだよ");
        }
    }

}
