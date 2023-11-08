package claims.security.security.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationToken {

    private final Custom_Authentication_Type authentication_type;
    private Boolean authenticationStatus = false;

    @Override
    public Object getPrincipal() {
        return super.getPrincipal();
    }

    public Boolean getAuthenticationStatus() {
        return authenticationStatus;
    }

    public void setAuthenticationStatus(Boolean authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public UsernamePasswordAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
        this.authentication_type = Custom_Authentication_Type.USERNAME_PWD_AUTHENTICATION;
    }

    public UsernamePasswordAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.authentication_type = Custom_Authentication_Type.USERNAME_PWD_AUTHENTICATION;
        this.authenticationStatus = true;
    }

    public Custom_Authentication_Type getAuthentication_type() {
        return authentication_type;
    }
}
