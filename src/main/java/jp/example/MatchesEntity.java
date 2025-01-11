package jp.example;

import java.sql.Date;

public class MatchesEntity {
	private int blackPlayerId = 0;
	private int whitePlayerId = 0;
	private String result = null;
	private String kifu = null;
	private String comment = null;
	private Date matchDate = null;

	
	public int getBlackPlayerId() {
		return blackPlayerId;
	}
	public void setBlackPlayerId(int blackPlayerId) {
		this.blackPlayerId = blackPlayerId;
	}
	public int getWhitePlayerId() {
		return whitePlayerId;
	}
	public void setWhitePlayerId(int whitePlayerId) {
		this.whitePlayerId = whitePlayerId;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	

}
