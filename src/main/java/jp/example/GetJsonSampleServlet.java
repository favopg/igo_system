package jp.example;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/sample/igo_system")
public class GetJsonSampleServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        System.out.println("リクエストパス" + path);
                        
        SampleDao dao = new SampleDao();
        JSONObject dbResponse = dao.select();

        // JSONデータを返却
        PrintWriter out = res.getWriter();
        out.print(dbResponse.toString());
        out.flush();
						
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// リクエストのエンコーディングを設定
        req.setCharacterEncoding("UTF-8");
	}
}
