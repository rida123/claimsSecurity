package claims.security.security.managers;

import claims.security.security.model.CustomTokenAuthentication;
import claims.security.security.providers.CustomJWTAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthManager implements AuthenticationManager  {
    @Autowired
    private CustomJWTAuthProvider tokenProvider;

/*    @Autowired
    private UsernamePwdProvider usernamePwdAuthProvider;*/

    @Override
    public CustomTokenAuthentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("type of " + authentication.getClass());

        if(authentication.getClass().equals(CustomTokenAuthentication.class)){
            return this.tokenProvider.authenticate(authentication);
        }
        else {
            throw new BadCredentialsException("Authentication is not supported");
        }
    }
}
