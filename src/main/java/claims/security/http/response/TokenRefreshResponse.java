package claims.security.http.response;

public class TokenRefreshResponse {
  private String token;
  private String refreshToken;
  private String tokenType = "Bearer";

  public TokenRefreshResponse(String token, String refreshToken) {
    this.token = token;
    this.refreshToken = refreshToken;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

}
