package claims.security.security.managers;

import claims.security.security.providers.UsernamePwdAuthProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SignInAuthenticationManager implements AuthenticationManager {

    private final UsernamePwdAuthProvider signInAuthProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(signInAuthProvider.supports(authentication.getClass())) {
            return signInAuthProvider.authenticate(authentication);
        }

        throw new BadCredentialsException("SignInAuthenticationManager => Bad credentials");
    }
}
