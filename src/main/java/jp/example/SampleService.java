package jp.example;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONObject;

public class SampleService {

	public SampleService() {
	}

	public JSONObject register(FormData inputData) {
		
		JSONObject response = new JSONObject();
		
		JSONObject rosterId = getUserId(inputData);
		if (rosterId.optString("status").equals("error")) {
			// エラーレスポンスで返却する
			response.put("status", "error");
			response.put("message", "黒番、白番のいずれかが登録されていないユーザです。ご確認ください");
			return response;
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
		response.put("status", "success");
		
		return response;
	}

	public JSONObject checkValidate(FormData inputData) {
		return ValidateUtil.validate(inputData);
	}
	
	private JSONObject getUserId(FormData input) {
		
		Map<String, Object> bindData = new LinkedHashMap<String, Object>();

		bindData.put("blackPlayer", input.getBlackPlayer());
		bindData.put("whitePlayer", input.getWhitePlayer());
		bindData.put("in1", input.getBlackPlayer());
		bindData.put("in2", input.getWhitePlayer());
		 			
		return SampleDao.selectFromBind("sql/users_select_id.sql", bindData);
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
		bindData.put("comment", entity.getComment());
		bindData.put("match_at", entity.getMatchDate());
		
		return bindData;
	}
}
