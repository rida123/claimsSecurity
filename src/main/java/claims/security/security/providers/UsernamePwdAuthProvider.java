package claims.security.security.providers;

import claims.security.security.model.UsernamePasswordAuthentication;
import claims.security.security.services.JpaUserDetailsService;
import claims.security.services.PwdEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsernamePwdAuthProvider implements AuthenticationProvider {

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Autowired
    private PwdEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();


        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        String user_details_pwd = userDetails.getPassword();
//        String encoded_input_password = this.passwordEncoder.passwordEncoder().encode(password);

        String encoded_input_password = this.passwordEncoder.passwordEncoder().encode(password);
        if(encoded_input_password.matches(user_details_pwd)) {
            Authentication auth_Object = new UsernamePasswordAuthentication(userDetails, password, userDetails.getAuthorities());
//            auth_Object.setAuthenticated(true);
            return auth_Object;
        }
        throw new BadCredentialsException("Wrong username/pwd credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.equals((authentication));
    }
}
