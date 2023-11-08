package claims.security.security.managers;

import claims.security.security.model.CustomBasicAuthentication;
import claims.security.security.providers.CustomBasicAuthProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class CustomBasicAuthenticationManager implements AuthenticationManager {

    private final CustomBasicAuthProvider authenticationProvider;

    public CustomBasicAuthenticationManager(CustomBasicAuthProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof CustomBasicAuthentication)) {
            throw new IllegalArgumentException("Unsupported authentication type");
        }

        // Your custom logic for authentication using the CustomAuthentication class

        return authenticationProvider.authenticate(authentication);
    }
}
