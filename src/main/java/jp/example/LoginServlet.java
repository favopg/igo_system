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

@WebServlet(urlPatterns = {"/sample/login", "/sample/login/register"})
public class LoginServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html; charset=UTF-8");
		res.setCharacterEncoding("UTF-8");

		String endpoint = req.getServletPath();
		
		switch(endpoint) {
			case "/sample/login":
				login(req, res);
			break;
			
			default :
				register(req, res);
			break;
		}
		
	}

	/**
	 * ログイン情報登録
	 * @param req リクエスト
	 * @param res レスポンス
	 */
	private void register(HttpServletRequest req, HttpServletResponse res) {
		try {
			// JSONデータ取得
			UserForm userForm = ServletUtil.convertFormFromJson(req, UserForm.class);
			// 登録
			UserService userService = new UserService();
			userService.registerUser(userForm);

			JSONObject response = new JSONObject();
			response.put("status", "success");
			response.put("message", "ログイン画面に返却します");

			// JSONデータを返却
			PrintWriter out = res.getWriter();
			out.print(response.toString());
			out.flush();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			execError(res);
		}
	}

	private void login(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=UTF-8");
		res.setCharacterEncoding("UTF-8");

		// JSONデータを読み取る
        StringBuilder jsonData = new StringBuilder();
		PrintWriter out = res.getWriter();

        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        }
        
        ObjectMapper mapper = new ObjectMapper();
        UserForm uf = mapper.readValue(jsonData.toString(), UserForm.class);
        UserService service = new UserService();
        JSONObject response = service.selectUser(uf);
        
        if (response.optString("status").equals("error")) {
    		out.print(response.toString());
    		out.flush();
    		return;
        }
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setUserId(response.optInt("id"));
		sessionInfo.setName(response.optString("name"));

		HttpSession session = req.getSession();
		session.setMaxInactiveInterval(600);
		session.setAttribute("sessionInfo", sessionInfo);

		// JSONデータを返却
		out.print(response.toString());
		out.flush();
			
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
			logger.error("ログインAPIでエラーが発生しました");
		} catch (Exception e) {
			throw new RuntimeException("ここでエラー起きたらもうおしまいだよ");
		}
	}

}
