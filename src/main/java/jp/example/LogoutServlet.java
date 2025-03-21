package jp.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * ログアウト
 *
 * @author イッシー
 * @version 1.0
 * @see LogoutServlet
 * @since 1.0
 */
@WebServlet(urlPatterns = {"/igo_app/igo_system/logout"})
public class LogoutServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getSession().invalidate();
        PrintWriter out = res.getWriter();
        JSONObject response = new JSONObject();
        response.put("status", "success");
        out.print(response.toString());
        out.flush();
    }
}
