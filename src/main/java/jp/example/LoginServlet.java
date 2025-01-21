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

@WebServlet("/sample/login")
public class LoginServlet extends HttpServlet {

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
				//registUser(req, res);
				System.out.println("デフォルトは何もしない");
			break;
		}
		
	}
	/**
	private void registUser(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		// JSONデータを読み取る
        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        }
        
        ObjectMapper mapper = new ObjectMapper();
        UserForm uf = mapper.readValue(jsonData.toString(), UserForm.class);

        
		

		JSONObject response = new JSONObject();
		response.put("status", "success");
		response.put("message", "ログイン画面に返却します");

		// JSONデータを返却
		PrintWriter out = res.getWriter();
		out.print(response.toString());
		out.flush();
			
	}
	*/

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
}
