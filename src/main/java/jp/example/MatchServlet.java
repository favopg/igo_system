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

@WebServlet("/sample/igo_system")
public class MatchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        res.setContentType("text/html; charset=UTF-8");
        res.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();
        System.out.println("リクエストパス" + path);

        HttpSession session = req.getSession(false);
        SessionInfo sessionInfo = new SessionInfo();
        if (req.getSession() != null) {
            sessionInfo = (SessionInfo) session.getAttribute("sessionInfo");
        }

        // 対戦データ全取得
        MatchService service = new MatchService();
        JSONObject response = service.getMatchList(sessionInfo.getUserId());

        // JSONデータを返却
        PrintWriter out = res.getWriter();
        out.print(response.toString());
        out.flush();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // register.htmlから登録ボタン押下時に呼ばれる
        res.setContentType("text/html; charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        // JSONデータを読み取る
        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        }
        System.out.println("受信したJSONデータ: " + jsonData.toString());

        ObjectMapper mapper = new ObjectMapper();
        MatchForm form = mapper.readValue(jsonData.toString(), MatchForm.class);

        System.out.println("変換確認: " + form.toString());

        // セッション情報が取れない場合はAPIえらーとして返却する
        HttpSession session = req.getSession(false);
        JSONObject response = new JSONObject();
        if (req.getSession() == null) {
            response.put(ApiResponse.STATUS.getCode(), ApiResponse.NG.getCode());
            response.put("message", "セッション切れです");

            PrintWriter out = res.getWriter();
            out.print(response.toString());
            out.flush();
            return;
        }

        // バリデーションチェック
        JSONObject validateResponse = ValidateUtil.validate(form);
        if (validateResponse.optString(ApiResponse.STATUS.getCode()).equals(ApiResponse.NG.getCode())) {
            PrintWriter out = res.getWriter();
            out.print(validateResponse.toString());
            out.flush();
            return;
        }

        SessionInfo sessionInfo = (SessionInfo) session.getAttribute("sessionInfo");

        String userName = session.getAttribute("userName").toString();
        MatchService service = new MatchService();
        response = service.register(form, sessionInfo);

        PrintWriter out = res.getWriter();
        out.print(response.toString());
        out.flush();

    }
}
