package jp.favoriteigo;

import static java.util.Objects.*;

import java.io.BufferedReader;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		
		// TODO GETの場合はいったんjspを返すようにソースを残しておく
		req.setAttribute("message", requireNonNullElse(req.getParameter("name"), "World"));
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		 // リクエストのエンコーディングを設定
        req.setCharacterEncoding("UTF-8");
        
        System.out.println("プロパティ情報" + PropertyUtil.getProperty("db.url"));

        // リクエストボディを読み取る
        StringBuilder jsonBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
        }

        // JSON文字列をパース
        JSONObject jsonObject = new JSONObject(jsonBody.toString());
        String name = jsonObject.getString("name");
        System.out.println("リクエストjson" + name);
               
        // レスポンス設定
		JSONObject json = new JSONObject();
		json.put("message", "Hello, Jetty!");

        // JSONを返却
        res.getWriter().write(json.toString());
	}
}
