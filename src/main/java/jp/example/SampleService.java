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
			
			String errorMessage = violations.stream()
                    .findFirst()
                    .map(ConstraintViolation::getMessage)
                    .orElse("No validation errors");
			
			// エラーレスポンスを返却
			response.put("status", "error");
			response.put("message", errorMessage);
			
		}
		return response;
		
	}
}
