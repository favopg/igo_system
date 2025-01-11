package jp.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * サンプルDAOクラス
 * エラー設計とか考えてない
 */
public class SampleDao {

	private static String jdbcUrl = null;
	private static String username = null;
	private static String password = null;

	static {
		jdbcUrl = PropertyUtil.getProperty("db.url");
		username = PropertyUtil.getProperty("db.user");
		password = PropertyUtil.getProperty("db.password");
		try {
			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static JSONObject select(String sqlFilePath) {
		JSONObject response = new JSONObject();
		List<Map<String, Object>> dataList = new ArrayList<>();
		try (
				Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
				PreparedStatement preparedStatement = connection.prepareStatement(getSql(sqlFilePath));
				ResultSet resultSet = preparedStatement.executeQuery();) {
			ResultSetMetaData metaData = resultSet.getMetaData();
			JSONArray recordsArray = new JSONArray();

			while (resultSet.next()) {
				JSONObject jsonData = new JSONObject();
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					// 1レコード分のカラム設定
					jsonData.put(metaData.getColumnName(i), resultSet.getString(i));
					row.put(metaData.getColumnName(i), resultSet.getObject(i));
				}
				recordsArray.put(jsonData);
				dataList.add(row);
			}

			// 検索結果から黒の勝利数を数える
			List<Map<String, Object>> blackVictoryList = dataList.stream()
					.filter(row -> row.get("result") != null && row.get("result").toString().contains("黒"))
					.collect(Collectors.toList());

			// 検索結果から白の勝利数を数える
			List<Map<String, Object>> whiteVictoryList = dataList.stream()
					.filter(row -> row.get("result") != null && row.get("result").toString().contains("白"))
					.collect(Collectors.toList());

			response.put("records", dataList);
			response.put("black_victory_cnt", blackVictoryList.size() + "勝");
			response.put("white_victory_cnt", whiteVictoryList.size() + "勝");

			System.out.println("データ取得値" + response.toString(2));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static JSONObject selectFromBind(String sqlFilePath, Map<String, Object> data) {
		JSONObject response = new JSONObject();

		try (
				Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
				PreparedStatement preparedStatement = connection.prepareStatement(getSql(sqlFilePath));) {

			int cnt = 1;
			for (String key : data.keySet()) {
				// バインド変数設定
				preparedStatement.setObject(cnt, data.get(key));
				cnt++;
			}

			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				ResultSetMetaData metaData = resultSet.getMetaData();

				if (!resultSet.next()) {
					// 0件の場合は空
//					response.put("status", "error");
//					response.put("message", "登録されていないユーザを指定されています。");
					return response;
				}

				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					// 1レコード分のカラム設定
					response.put(metaData.getColumnName(i), resultSet.getObject(i));
				}
				response.put("status", "success");
			}

			System.out.println("データ取得値" + response.toString(2));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static void insert(String sqlFilePath, Map<String, Object> data) {
		try (
				Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
				PreparedStatement preparedStatement = connection.prepareStatement(getSql(sqlFilePath));) {
			int cnt = 1;
			for (String key : data.keySet()) {
				// バインド変数設定
				preparedStatement.setObject(cnt, data.get(key));
				cnt++;
			}

			int rowsInserted = preparedStatement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("データが正常に挿入されました。");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * SQLファイルパスからSQL文を取得する
	 * @param sqlPath SQLパス sqlフォルダから指定する
	 * @return SQL文
	 */
	private static String getSql(String sqlPath) {

		StringBuilder sql = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(SampleDao.class.getClassLoader().getResourceAsStream(sqlPath)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				sql.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sql.toString();
	}
}