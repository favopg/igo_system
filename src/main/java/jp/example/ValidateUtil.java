package jp.example;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.json.JSONObject;

public class ValidateUtil {


	/**
	 * リクエストデータのバリデーションチェックを行う
	 * @param request setter/getterのrクエスト受信オブジェクト
	 * @return バリデーションチェック結果
	 */
	public static <T> JSONObject validate(T request) {
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		Set<ConstraintViolation<T>> violations = validator.validate(request);
		JSONObject response = new JSONObject();

		if (!violations.isEmpty()) {

			//  エラーレスポンスを設定
			response.put("status", "error");

			for (ConstraintViolation<T> errorInfo : violations) {
				// チェックエラーのフィールド変数に対してエラーメッセージを設定
				response.put(errorInfo.getPropertyPath().toString(), errorInfo.getMessage());
			}
		}
		return response;
	}
}
