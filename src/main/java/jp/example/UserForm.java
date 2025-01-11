package jp.example;

import jakarta.validation.constraints.NotNull;

public class UserForm {
	
	@NotNull(message = "ユーザIDが未入力です")
	private String name = null;
	@NotNull(message = "パスワードが未入力です")
	private String password = null;
	
	private boolean isPublic = false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
}
