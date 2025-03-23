package jp.favoriteigo;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.seasar.doma.jdbc.tx.TransactionManager;

/**
 * ユーザ情報を操作するサービスクラス
 * @author イッシー
 * @version 1.0
 * @since 1.0
 * @see LoginServlet
 */
public class UserService {

	private TransactionManager transaction = null;
	private UserDao dao = null;

	/**
	 * デフォルトコンストラクタ
	 */
	public UserService() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		AppConfig config = new AppConfig();
		transaction = config.getTransactionManager();
		dao = new UserDaoImpl(config);
	}

	/**
	 * ユーザ情報を使用し、ユーザDAOを呼び出し、ユーザ情報を登録します。<br>
	 * {@link UserDao#insert(UserEntity)}
	 * @param userForm 入力情報を保持したユーザ情報
	 * @return APIステータスを保持したJSONObject
	 * @throws RuntimeException DBアクセスエラーの場合にスローされます。
	 */
	public JSONObject registerUser(UserForm userForm) {
		JSONObject response = ValidateUtil.validate(userForm);

		if (!response.isEmpty()) {
			return response;
		}

		// ハッシュ化したパスワードを設定
		String hashedPassword = BCrypt.hashpw(userForm.getPassword(),BCrypt.gensalt());
		userForm.setPassword(hashedPassword);

		UserEntity entity = new UserEntity();

		// パラメタのユーザ情報を登録用データにコピーする
        try {
            BeanUtils.copyProperties(entity, userForm);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

		UserDao dao = new UserDaoImpl(new AppConfig());
		dao.insert(entity);

		response.put(ApiResponse.STATUS.getCode(), ApiResponse.OK.getCode());
		
		return response;
	}

	/**
	 * ユーザ情報を使用し、ユーザDAOを呼び出し、ユーザ情報を取得します。<br>
	 * {@link UserDao#selectFindByName(String)}
	 * @param userForm 入力情報を保持したユーザ情報
	 * @return APIステータスを保持したJSONObject
	 * @throws RuntimeException DBアクセスエラーの場合にスローされます。
	 */
	public JSONObject selectUser(UserForm userForm) {

		JSONObject response = ValidateUtil.validate(userForm);
		if (!response.isEmpty()) {
			return response;
		}

		Map<String, Object> userMap = transaction.required(() -> {
			return dao.selectFindByName(userForm.getName());
		});

		if (userMap.isEmpty()) {
			response.put(ApiResponse.STATUS.getCode(), ApiResponse.NG.getCode());
			return response;
		}

		// パスワード検証
		boolean isPasswordMatch = BCrypt.checkpw(userForm.getPassword(), userMap.get("password").toString());
		if (!isPasswordMatch) {
			response.put(ApiResponse.STATUS.getCode(), ApiResponse.NG.getCode());
			return response;
		}

		response = new JSONObject(userMap);
		response.put(ApiResponse.STATUS.getCode(), ApiResponse.OK.getCode());

		System.out.println("検索結果" + response.toString());
		
		return response;
	}
}
