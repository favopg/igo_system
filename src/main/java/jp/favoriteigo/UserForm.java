package jp.favoriteigo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserForm {
	
	@NotNull(message = "ユーザIDが未入力です")
	@NotBlank(message = "ユーザIDが未入力です")
	private String name = null;
	@NotNull(message = "パスワードが未入力です")
	@NotBlank(message = "パスワードが未入力です")
	private String password = null;
	
	private boolean isPublic = false;
	private String chessAbility = null;
	private String chessAbilityKbn = null;

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
	public String getChessAbility() {
		return chessAbility;
	}
	public void setChessAbility(String chessAbility) {
		this.chessAbility = chessAbility;
	}
	public String getChessAbilityKbn() { return chessAbilityKbn;}
	public void setChessAbilityKbn(String chessAbilityKbn) { this.chessAbilityKbn = chessAbilityKbn; }

}
