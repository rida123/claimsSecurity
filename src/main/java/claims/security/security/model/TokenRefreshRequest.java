package claims.security.security.model;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequest {
  @NotBlank
  private String refreshToken;
  private String profileId;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
