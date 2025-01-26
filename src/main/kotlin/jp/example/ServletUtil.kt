package jp.example

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * ServletUtil
 * サーブレットクラス専用のUtilクラス
 *
 * @author イッシー
 * @version 1.0
 * @since 1.0
 */
object ServletUtil {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * リクエスト情報のJsonデータをFormクラスに設定して返却します
     * @param req リクエスト情報
     * @param form 変換対象のFormクラス
     * @return リクエスト情報を設定したFormクラス
     */
    @JvmStatic
    fun <T> convertFormFromJson(req: HttpServletRequest, form: Class<T>): T {
        // JSONデータを読み取る
        val jsonData = StringBuilder()
        try {
            req.reader.use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    jsonData.append(line)
                }
            }
            logger.debug("変換前の受信データ{}", jsonData.toString())
            val mapper = ObjectMapper()
            return mapper.readValue(jsonData.toString(), form)
        } catch (e: IOException) {
            throw RuntimeException("jsonデータ変換処理でエラー発生")
        }
    }
}