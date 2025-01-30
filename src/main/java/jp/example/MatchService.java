package jp.example;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.seasar.doma.jdbc.tx.TransactionManager;

/**
 * 対戦情報を操作するサービスクラス
 * @author イッシー
 * @version 1.0
 * @since 1.0
 * @see MatchServlet
 */
public class MatchService {

	private TransactionManager transaction = null;
	private MatchDao dao = null;

	/**
	 * デフォルトコンストラクタ
	 */
	public MatchService() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		AppConfig config = new AppConfig();
		transaction = config.getTransactionManager();
		dao = new MatchDaoImpl(config);
	}

	/**
	 * idを使用し、編集用の対戦データを取得します<br>
	 * @param id id
	 * @return 編集用対戦データ
	 */
	public JSONObject getMatch(int id) {
		Map<String, Object> matchMap = transaction.required(() -> {
			return dao.selectFindById(id);
		});

		JSONObject response = new JSONObject(matchMap);
		response.put(ApiResponse.STATUS.getCode(), ApiResponse.OK.getCode());

		return response;
	}

	/**
	 * ユーザIDを使用し、ログインユーザの対戦一覧と公開設定している他のユーザの対戦情報すべてを取得します。<br>
	 * {@link MatchDao#selectFindByUserId(int)}
	 * @param userId セッション保持しているユーザID
	 * @return 対戦情報一覧
	 * @throws RuntimeException DBアクセスエラーの場合にスローされます。
	 */
	public JSONObject getMatchList(int userId) {
		List<Map<String, Object>> matchMapList = transaction.required(() -> {
			return dao.selectFindByUserId(userId);
		});

		// 検索結果から黒の勝利数を集計
		List<Map<String, Object>> blackVictoryList = matchMapList.stream()
				.filter(row -> row.get("result") != null &&
						row.get("result").toString().contains("黒") &&
						Integer.parseInt(row.get("created_user_id").toString()) == userId)
				.toList();

		// 検索結果から白の勝利数を数える
		List<Map<String, Object>> whiteVictoryList = matchMapList.stream()
				.filter(row -> row.get("result") != null &&
						row.get("result").toString().contains("白") &&
						Integer.parseInt(row.get("created_user_id").toString()) == userId)
				.toList();

		JSONObject response = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		for (Map<String, Object> map : matchMapList) {
			JSONObject json = new JSONObject(map);
			jsonArray.put(json);
		}

		response.put("records", jsonArray);
		response.put("black_victory_cnt", blackVictoryList.size());
		response.put("white_victory_cnt", whiteVictoryList.size());
		response.put(ApiResponse.STATUS.getCode(), ApiResponse.OK.getCode());
		return response;
	}

	/**
	 * 入力情報から、対戦テーブルに対戦情報を登録します。<br>
	 * {@link MatchDao#insertMatch(MatchEntity)}
	 * @param inputData 入力された対戦情報
	 * @param sessionInfo セッション情報のユーザ名
	 * @return APIレスポンスを詰めた　JSONObject
	 * @throws RuntimeException DBアクセスエラーの場合にスローされます。
	 */
	public JSONObject register(MatchForm inputData, SessionInfo sessionInfo) {

		JSONObject response = new JSONObject();

		String userName = sessionInfo.getName();

		// 黒番、白番のどちらかにログインユーザが含まれているかチェックする
		if (!inputData.getBlackName().equals(userName) && !inputData.getWhiteName().equals(userName) ) {
			response.put(ApiResponse.STATUS.getCode(), ApiResponse.NG.getCode());
			response.put("message", "黒番、白番のいずれかが登録されていないユーザです。ご確認ください");
			return response;
		}

		// 入力値をエンティティに変換
		MatchEntity entity = new MatchEntity();
		try {
			BeanUtils.copyProperties(entity, inputData);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		// 作成ユーザIDにログインユーザIDを設定
		entity.setCreatedUserId(sessionInfo.getUserId());

		// 登録処理
		if (dao.insertMatch(entity) != 1) {
			response.put(ApiResponse.STATUS.getCode(), ApiResponse.NG.getCode());
			response.put("message", "対戦成績登録処理で失敗しました");
			return response;
		}

		response.put(ApiResponse.STATUS.getCode(), ApiResponse.OK.getCode());
		return response;
	}

	/**
	 * 入力情報から、対戦テーブルを複数削除します。<br>
	 * {@link MatchDao#deleteMatch(List)}
	 * @param matchIds 選択された対戦ID
	 * @return APIレスポンスを詰めた　JSONObject
	 * @throws RuntimeException DBアクセスエラーの場合にスローされます。
	 */
	public JSONObject delete(int[] matchIds) {
		JSONObject response = new JSONObject();

		List<MatchEntity> matchEntityList = new ArrayList<>();
        for (int matchId : matchIds) {
            MatchEntity entity = new MatchEntity();
            entity.setId(matchId);
            matchEntityList.add(entity);
        }

		// 削除実行
		dao.deleteMatch(matchEntityList);
		response.put(ApiResponse.STATUS.getCode(), ApiResponse.OK.getCode());
		return response;
	}
}
