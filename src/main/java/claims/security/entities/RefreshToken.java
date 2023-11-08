package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "CORE_REFRESHTOKEN",schema = "PORTAL")
public class RefreshToken {
  @Id
  @Column(name = "REFRESHTOKEN_ID")
  private String id;

  @OneToOne
  @JoinColumn(name = "REFRESHTOKEN_USER_ID", referencedColumnName = "id")
  private CoreUser user;

  @Column(nullable = false, unique = true,name = "REFRESHTOKEN_TOKEN")
  private String token;

  @Column(nullable = false,name = "REFRESHTOKEN_EXPIRYDATE")
  private Instant expiryDate;



}
