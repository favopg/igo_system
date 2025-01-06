package jp.example;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.json.JSONObject;

public class SampleService {

	public SampleService() {
	}
	
	public void register(FormData inputData) {
		// バリデーションチェック
	}
	
	public JSONObject checkValidate(FormData inputData) {
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		Set<ConstraintViolation<FormData>> violations = validator.validate(inputData);
		JSONObject response = new JSONObject();
		
		if (!violations.isEmpty()) {
			
			//  エラーレスポンスを設定
			response.put("status", "error");
			
			for (ConstraintViolation<FormData> errorInfo : violations) {
				// チェックエラーのフィールド変数に対してエラーメッセージを設定
				response.put(errorInfo.getPropertyPath().toString(), errorInfo.getMessage());
			}
		}
		return response;
		
	}
}
