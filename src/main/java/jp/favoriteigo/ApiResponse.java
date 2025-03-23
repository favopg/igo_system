package jp.favoriteigo;

public enum ApiResponse {
    OK("success"), NG("error"), STATUS("status");

    private final String code;

    ApiResponse(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
