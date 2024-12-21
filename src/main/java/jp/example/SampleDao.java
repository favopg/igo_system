package jp.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * サンプルDAOクラス
 * エラー設計とか考えてない
 */
public class SampleDao {

	public JSONObject select() {
		String jdbcUrl = PropertyUtil.getProperty("db.url");
		String username = PropertyUtil.getProperty("db.user");
		String password = PropertyUtil.getProperty("db.password");
		String query = "SELECT * FROM rosters";
		JSONObject response = new JSONObject();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (
				Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery()
			) 
		{
			ResultSetMetaData metaData = resultSet.getMetaData();
			JSONArray recordsArray = new JSONArray();
			
			while (resultSet.next()) {
				String id = resultSet.getString("id");
				String name = resultSet.getString("name");
				System.out.println("ID: " + id + ", Name: " + name);
				JSONObject jsonData = new JSONObject();
				
				for(int i = 1; i<= metaData.getColumnCount(); i++) {
					// 1レコード分のカラム設定
					jsonData.put(metaData.getColumnName(i), resultSet.getString(i));
				}
				recordsArray.put(jsonData);	
			}
			response.put("records", recordsArray);
			System.out.println("データ取得値" + response.toString(2));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return response;
	}
}