package claims.security.http.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CoreUserProfileResponse {

    private String id;

    private String userCode;

    private String companyId;

    private String code;

    private String name;

    private String description;
    private String displayName;
    private byte[] logo ;
  /*  @Column(name = "PROFILE_TYPE", length = 32)
    private String profileType;*/


    public CoreUserProfileResponse() {
        this.id = UUID.randomUUID().toString();
    }

}
