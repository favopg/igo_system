package jp.example;

import jakarta.validation.constraints.NotBlank;

import java.sql.Date;

public class MatchForm {

	@NotBlank(message = "黒番は必ず入力してください")
	private String blackName = null;

	@NotBlank(message = "白番は必ず入力してください")
	private String whiteName = null;

	@NotBlank(message = "結果は必ず入力してください")
	private String result = null;

	private String resultLink = null;

	private String comment = null;

	private Date matchAt= null;

	private boolean publicFlag = false;

	private int createdUserId = 0;

	public String getBlackName() {
		return blackName;
	}

	public void setBlackName(String blackName) {
		this.blackName = blackName;
	}

	public String getWhiteName() {
		return whiteName;
	}

	public void setWhiteName(String whiteName) {
		this.whiteName = whiteName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultLink() {
		return resultLink;
	}

	public void setResultLink(String resultLink) {
		this.resultLink = resultLink;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getMatchAt() {
		return matchAt;
	}

	public void setMatchAt(Date matchAt) {
		this.matchAt = matchAt;
	}

	public boolean isPublicFlag() {
		return publicFlag;
	}

	public void setPublicFlag(boolean publicFlag) {
		this.publicFlag = publicFlag;
	}

	public int getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(int createdUserId) {
		this.createdUserId = createdUserId;
	}

	@Override
	public String toString() {
		return "MatchForm{" +
				"blackName=" + blackName +
				", whiteName=" + whiteName +
				", result=" + result +
				", resultLink=" + resultLink +
				", comment=" + comment +
				", matchAt=" + matchAt +
				", publicFlag=" + publicFlag +
				", createdUserId=" + createdUserId +
				"}";
	}
}
