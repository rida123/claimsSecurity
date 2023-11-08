package claims.security.security.model;

import claims.security.entities.AppAuthority;
import claims.security.entities.Next2Authority;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;


@AllArgsConstructor
public class SecurityAuthority implements GrantedAuthority {

    private final Next2Authority authority;

    @Override
    public String getAuthority() {
        return authority.getName();
    }
}
