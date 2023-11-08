package claims.security.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CoreUserResponse {

    private String username;
    /*private String companyId;
    private String companyName;*/
    private String userFullName;
}
