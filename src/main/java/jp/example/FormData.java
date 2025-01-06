package jp.example;

import jakarta.validation.constraints.NotNull;

public class FormData {
	@NotNull(message = "黒番が未入力です")
	private String blackPlayer = null;
	private String whitePlayer = null;
	private String result = null;
	private String kifu = null;
	
	public String getBlackPlayer() {
		return blackPlayer;
	}
	public void setBlackPlayer(String blackPlayer) {
		this.blackPlayer = blackPlayer;
	}
	public String getWhitePlayer() {
		return whitePlayer;
	}
	public void setWhitePlayer(String whitePlayer) {
		this.whitePlayer = whitePlayer;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getKifu() {
		return kifu;
	}
	public void setKifu(String kifu) {
		this.kifu = kifu;
	}
	

}
