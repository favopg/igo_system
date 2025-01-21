package jp.example;

/**
 * SessionInfo  
 * ユーザ情報を操作するサービスクラス
 *
 * @author イッシー
 * @version 1.0
 * @since 1.0
 * @see LoginServlet
 */
public class SessionInfo {

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int userId = 0;
    private String name = "";

}
