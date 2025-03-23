package jp.favoriteigo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/igo_app/uploadCsv")
@MultipartConfig
public class CsvUploadServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(CsvUploadServlet.class);
    static Map<String, String> mapping = new HashMap<String, String>();

    static {
        mapping.put("対戦ID", "id");
        mapping.put("黒番", "black_name");
        mapping.put("白番", "white_name");
        mapping.put("対戦日", "match_at");
        mapping.put("結果", "result");
        mapping.put("公開フラグ", "public_flag");
        mapping.put("棋譜URL", "result_link");
        mapping.put("コメント", "comment");
    }

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse res) {

        JSONObject response = new JSONObject();
        Part filePart = null;
        try {
            filePart = request.getPart("file");
            if (filePart == null || filePart.getSize() == 0) {
                response.put(ApiResponse.STATUS.getCode(), ApiResponse.NG.getCode());
                response.put("message", "CSVファイルアップロードに失敗しました");
                ServletUtil.apiResponse(res, response);
                return;
            }
        } catch (Exception e) {
            logger.debug("システムエラー{}",e.getMessage());
            throw new RuntimeException(e);
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
                    if (headers[i].trim().equals("対戦日")) {
                        Date date = dateFormat.parse(values[i].trim());
                        logger.debug("対戦日変換前{}",values[i].trim());
                        logger.debug("対戦日{}",date);

                        row.put(getMapping(headers[i].trim()), date);
                        continue;
                    }
                    // 公開フラグの設定
                    if (headers[i].trim().equals("公開フラグ")) {
                        if (values[i].trim().equals("1")) {
                            row.put(getMapping(headers[i].trim()), true);
                        } else {
                            row.put(getMapping(headers[i].trim()), false);
                        }
                        continue;
                    }
                    // 上記以外は文字列データ
                    row.put(getMapping(headers[i].trim()), i < values.length ? values[i].trim() : "");
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
            logger.debug("システムエラー{}",e.getMessage());
            throw new RuntimeException(e);
        }


    }

    /**
     * 後続処理用にリクエストのヘッダー項目を変換する
     * @param header ヘッダー項目名
     * @return 変換後のヘッダー項目名
     */
    private String getMapping(String header) {
        return mapping.get(header);
    }
}
