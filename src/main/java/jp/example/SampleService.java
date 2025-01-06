package jp.example;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONObject;

public class SampleService {

	public SampleService() {
	}

	public void register(FormData inputData) {
		JSONObject rosterId = getRostersId(inputData);
		if (rosterId.isEmpty()) {
			// エラーレスポンスで返却する
			return;
		}
		
		MatchesEntity entity = new MatchesEntity();
		
		try {
			BeanUtils.copyProperties(entity, inputData);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		System.out.println("結果" + entity.getResult());
		System.out.println("棋譜" + entity.getKifu());
		
		entity.setBlackPlayerId(rosterId.optInt("black_player_id"));
		entity.setWhitePlayerId(rosterId.optInt("white_player_id"));
		
		// DB登録処理
		SampleDao.insert("sql/match_insert.sql", convertInsertData(entity));
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
	
	private JSONObject getRostersId(FormData input) {
		
		Map<String, Object> bindData = new LinkedHashMap<String, Object>();

		bindData.put("blackPlayer", input.getBlackPlayer());
		bindData.put("whitePlayer", input.getWhitePlayer());
		bindData.put("in1", input.getBlackPlayer());
		bindData.put("in2", input.getWhitePlayer());
		 			
		return SampleDao.selectRosterId("sql/rosters_select_name.sql", bindData);
	}

	/**
	 * formから登録用に変換する
	 * 設定する際にSQLのバインド変数の順番と合せること
	 * @param input formデータ
	 * @return 登録用データ
	 */
	public Map<String, Object> convertInsertData(MatchesEntity entity) {

		Map<String, Object> bindData = new LinkedHashMap<String, Object>();

		bindData.put("blackPlayerId", entity.getBlackPlayerId());
		bindData.put("whitePlayerId", entity.getWhitePlayerId());
		bindData.put("result", entity.getResult());
		bindData.put("kifu", entity.getKifu());

		return bindData;
	}
}
