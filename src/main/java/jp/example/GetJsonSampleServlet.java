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
public class GetJsonSampleServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        System.out.println("リクエストパス" + path);
        int userId = 0;

        HttpSession session = req.getSession(false);
        if(req.getSession() != null) {
           userId = (int)session.getAttribute("userId");
        }

        System.out.println("セッションの値" + userId);
        // 対戦データ全取得
        SampleService service = new SampleService();
        JSONObject response = service.getMatchList(userId);

        // JSONデータを返却
        PrintWriter out = res.getWriter();
        out.print(response.toString());
        out.flush();
						
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// register.htmlから登録ボタン押下時に呼ばれる
		
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
        FormData form = mapper.readValue(jsonData.toString(), FormData.class);
        
        System.out.println("json変換後データ" + form.getBlackPlayer());
        
        SampleService service = new SampleService();
        JSONObject response = service.register(form);
        
        System.out.println(response.toString(2));
                        
        PrintWriter out = res.getWriter();
        out.print(response.toString());
        out.flush();
		
	}
}
