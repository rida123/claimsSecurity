package claims.security.security.services;


import claims.security.entities.CoreDomainValue;
import claims.security.entities.RefreshToken;
import claims.security.exceptions.NotFoundException;
import claims.security.exceptions.TokenRefreshException;
import claims.security.repositories.RefreshTokenRepository;
import claims.security.services.DBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
//  @Value("${claims.app.jwtRefreshExpirationMs}")
//  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;
@Autowired
  private DBUtils db;


  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(String userId) {
    RefreshToken refreshToken = new RefreshToken();


    //getting access token time out:
    Optional<CoreDomainValue> token_optional = this.db.coreDomainValueRepository.findById("PASSWORD_CONFIGURATON.SESSI_TIM_OUT");
    if(!token_optional.isPresent()) {
      throw  new NotFoundException("Refresh token time out is not defined: PASSWORD_CONFIGURATON.SESSI_TIM_OUT");
    }
    Long refresh_token_time_out = Long.valueOf(token_optional.get().getVal1());

    refreshToken.setUser(db.coreUserRepository.findById(userId).get());
//    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    //refresh token specified in milliseconds
    refreshToken.setExpiryDate(Instant.now().plusMillis(refresh_token_time_out * 60000));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setId(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);

    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(String userId) {
    return refreshTokenRepository.deleteByUser(db.coreUserRepository.findById(userId).get());
  }
}
