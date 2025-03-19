package jp.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/sample/uploadCsv")
@MultipartConfig
public class CsvUploadServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(CsvUploadServlet.class);

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse res)
			throws ServletException, IOException {
		
		Part filePart = request.getPart("file");
        JSONObject response = new JSONObject();

		if (filePart == null || filePart.getSize() == 0) {
            response.put(ApiResponse.STATUS.getCode(), ApiResponse.NG.getCode());
            response.put("message", "CSVファイルアップロードに失敗しました");
            ServletUtil.apiResponse(res, response);
            return;
		}
		
		List<Map<String, Object>> jsonData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(filePart.getInputStream(), StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                response.put(ApiResponse.STATUS.getCode(), ApiResponse.NG.getCode());
                response.put("message", "CSVファイルが空です");
                ServletUtil.apiResponse(res, response);
                return;
            }

            // ヘッダー行を取得
            String[] headers = headerLine.split(",");
            // DATE型用
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // データ行を解析
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, Object> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    // 対戦日の設定
                    if (headers[i].trim().equals("match_at")) {
                        Date date = dateFormat.parse(values[i].trim());
                        logger.debug("対戦日変換前{}",values[i].trim());
                        logger.debug("対戦日{}",date);

                        row.put(headers[i].trim(), date);
                        continue;
                    }
                    // 公開フラグの設定
                    if (headers[i].trim().equals("public_flag")) {
                        if (headers[i].trim().equals("1")) {
                            row.put(headers[i].trim(), true);
                        } else {
                            row.put(headers[i].trim(), false);
                        }
                        continue;
                    }
                    // 上記以外は文字列データ
                    row.put(headers[i].trim(), i < values.length ? values[i].trim() : "");
                }
                jsonData.add(row);
            }

        // JSON形式に変換してレスポンスを返す
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(jsonData);
        logger.debug(json);

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new RuntimeException("セッション情報取得でエラー発生");
        }

        // セッション情報取得
        SessionInfo sessionInfo = (SessionInfo)session.getAttribute("sessionInfo");
        logger.debug("セッション情報{}",sessionInfo.toString());
        String userId = String.valueOf(sessionInfo.getUserId());

        // CSVデータ登録
        MatchService matchService = new MatchService();
        matchService.insertList(userId, jsonData);

        // APIレスポンス返却
        ServletUtil.apiResponse(res, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
