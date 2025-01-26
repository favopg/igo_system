package jp.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/sample/igo_system")
public class MatchServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MatchServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {

        try {
            res.setContentType("text/html; charset=UTF-8");
            res.setCharacterEncoding("UTF-8");

            String path = req.getServletPath();
            logger.debug("リクエストパス{}", path);

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

            // JSONデータを返却
            PrintWriter out = res.getWriter();
            out.print(response.toString());
            out.flush();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            try {
                execError(res);
            } catch (IOException ex) {
                logger.error("ここでエラー起きたらもうおしまいだよ", ex);
                throw new RuntimeException(ex);
            }
        }
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
            JSONObject response = new JSONObject();
            if (req.getSession() == null) {
                throw new RuntimeException("セッション情報取得でエラー発生");
            }

            // バリデーションチェック
            JSONObject validateResponse = ValidateUtil.validate(form);
            if (validateResponse.optString(ApiResponse.STATUS.getCode()).equals(ApiResponse.NG.getCode())) {
                PrintWriter out = res.getWriter();
                out.print(validateResponse.toString());
                out.flush();
                logger.debug("対戦結果登録APIのバリデーションチェックエラー{}", validateResponse.toString());
                return;
            }

            // 対戦登録処理
            SessionInfo sessionInfo = (SessionInfo) session.getAttribute("sessionInfo");
            MatchService service = new MatchService();
            response = service.register(form, sessionInfo);

            PrintWriter out = res.getWriter();
            out.print(response.toString());
            out.flush();
        } catch (Exception e) {
            try {
                execError(res);
            } catch (IOException ex) {
                logger.error("ここでエラー起きたらもうおしまいだよ", ex);
                throw new RuntimeException(ex);
            }
        }
    }

    private void execError(HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();

        JSONObject response = new JSONObject();
        response.put(ApiResponse.STATUS.getCode(), ApiResponse.NG.getCode());
        response.put("message", "システムエラーが発生しました");
        out.print(response.toString());
        out.flush();
        logger.error("APIエラーレスポンス{}",response.toString());
        logger.error("対戦一覧取得APIでエラーが発生しました");
    }

}
