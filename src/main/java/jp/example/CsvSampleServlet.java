package jp.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/sample/uploadCsv")
@MultipartConfig
public class CsvSampleServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Part filePart = request.getPart("file");

		if (filePart == null || filePart.getSize() == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\": \"No file uploaded\"}");
			return;
		}
		
		List<Map<String, String>> jsonData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream(), "UTF-8"))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"CSV file is empty\"}");
                return;
            }

            // ヘッダー行を取得
            String[] headers = headerLine.split(",");

            // データ行を解析
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i].trim(), i < values.length ? values[i].trim() : "");
                }
                jsonData.add(row);
            }
        }
        
        // JSON形式に変換してレスポンスを返す
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(jsonData);
        System.out.println("CSVアップロード成功" + json.toString());
        
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
	}
}
