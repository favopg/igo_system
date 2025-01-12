package jp.example

import jakarta.validation.Validation
import org.json.JSONObject

object ValidateUtil {

    /**
     * リクエストデータのバリデーションチェックを行う
     * @param request setter/getterのrクエスト受信オブジェクト
     * @return バリデーションチェック結果
     */
    @JvmStatic
    fun <T> validate(request: T): JSONObject {
        val validator = Validation.buildDefaultValidatorFactory().validator

        val violations = validator.validate(request)
        val response = JSONObject()

        if (!violations.isEmpty()) {
            //  エラーレスポンスを設定

            response.put("status", "error")

            for (errorInfo in violations) {
                // チェックエラーのフィールド変数に対してエラーメッセージを設定
                response.put(errorInfo.propertyPath.toString(), errorInfo.message)
            }
        }
        return response
    }


}