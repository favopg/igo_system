package jp.example;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

	public UserService() {

	}

	public JSONObject registUser(UserForm uf) {
		JSONObject response = ValidateUtil.validate(uf);

		if (!response.isEmpty()) {
			return response;
		}

		// ハッシュ化したパスワードを設定
		String hashedPassword = BCrypt.hashpw(uf.getPassword(),BCrypt.gensalt());
		uf.setPassword(hashedPassword);
						
		SampleDao.insert("sql/users_insert.sql", convertInsertData(uf));
		
		response.put("status", "success");
		
		return response;
	}
	
	public JSONObject selectUser(UserForm uf) {
		JSONObject response = ValidateUtil.validate(uf);

		if (!response.isEmpty()) {
			return response;
		}
		
		Map<String, Object> bindData = new HashMap<String,Object>();
		bindData.put("name", uf.getName());
		
		response = SampleDao.selectFromBind("sql/users_select.sql", bindData);
		
		if (response.isEmpty()) {
			response.put("status", "error");
			return response;
		}
		
		// パスワード検証
		boolean isPasswordMatch = BCrypt.checkpw(uf.getPassword(), response.optString("password_hash"));
		if (!isPasswordMatch) {
			response.put("status", "error");
			return response;
		}

		response.put("status", "success");
		
		return response;
		
	}
	
	/**
	 * formから登録用に変換する
	 * 設定する際にSQLのバインド変数の順番と合せること
	 * @param <T>
	 * @param input formデータ
	 * @return 登録用データ
	 */
	public <T> Map<String, Object> convertInsertData(T entity) {

		Map<String, Object> bindData = new LinkedHashMap<String, Object>();

		Class<?> clazz = entity.getClass();
		
		// クラスのすべてのフィールドを取得
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // プライベートフィールドにもアクセス可能にする
            try {
                // フィールド名をキー、フィールド値を値として設定
                bindData.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
		return bindData;
	}
}
