package jp.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class SampleDao {

	public void select() {
		String jdbcUrl = PropertyUtil.getProperty("db.url");
		String username = PropertyUtil.getProperty("db.user");
		String password = PropertyUtil.getProperty("db.password");

		String query = "SELECT * FROM users";

		try (
				Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery()
			) 
		{
			ResultSetMetaData metaData = resultSet.getMetaData();
			Map<String, String> row = new HashMap<>();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				System.out.println("ID: " + id + ", Name: " + name);
				
				for(int i = 1; i<= metaData.getColumnCount(); i++) {
					row.put(metaData.getColumnName(i), resultSet.getString(i));
				}
			}
			JSONObject jsonData = new JSONObject(row);
			System.out.println("データ取得値" + jsonData.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
