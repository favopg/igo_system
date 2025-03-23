package jp.favoriteigo.entity;

import org.seasar.doma.*;

import java.sql.Date;

@Entity
@Table(name = "matches")
public class MatchEntity {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "black_name")
	private String blackName = null;

	@Column(name = "white_name")
	private String whiteName = null;

	@Column(name = "result")
	private String result = null;

	@Column(name = "result_link")
	private String resultLink = null;

	@Column(name = "comment")
	private String comment = null;

	@Column(name = "match_at")
	private Date matchAt= null;

	@Column(name = "public_flag")
	private boolean publicFlag = false;

	@Column(name = "created_user_id")
	private int createdUserId = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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


	

	

}
