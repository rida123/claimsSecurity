package claims.security.security.providers;

import claims.security.security.model.CustomTokenAuthentication;
import claims.security.security.services.JWTService;
import claims.security.security.services.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JwtAuthProvider implements AuthenticationProvider {
    @Autowired
    private JpaUserDetailsService userDetailsService;
    @Autowired
    JWTService jwtService;

    /**
     * authenticationProvider will validate the token by
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomTokenAuthentication ca = (CustomTokenAuthentication) authentication;
        //---------------------- start ------------------------------------------------------------
        {
            //extracting jwt from Authorization header
//            String jwtToken = ca.getToken().substring(7);
            String jwtToken = ca.getToken();

            try {
                //step1 in validation check token exist in the database
                boolean token_is_expired = this.jwtService.checkExpiredToken(jwtToken);
                if(token_is_expired) {
                    throw new AuthenticationException("Invalid Authentication, token is expired") {};
                }
                //this part validates the token by checking the signing
                String payload = this.jwtService.validateToken(jwtToken);

                JsonParser parser = JsonParserFactory.getJsonParser();
                //parseMap receives json and returns map(add to code fragments)
                Map<String, Object> payLoadMap = parser.parseMap(payload);
                String user = payLoadMap.get("user").toString();

                List<String> authorities_names = (List<String>) payLoadMap.get("authorities");

                List<GrantedAuthority> authorities = new ArrayList<>();
                //for each authority name we create a GrantedAuthority object
                for (String authority: authorities_names) {
                    GrantedAuthority ga = new SimpleGrantedAuthority(authority);
                    authorities.add(ga);
                }
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(user);
                return new CustomTokenAuthentication(true, jwtToken, authorities, userDetails);

            }
            catch (Exception e) {
                System.out.println("Exception: " + e.toString());
                //token is not valid =>null will be stored in the SecurityContextHolder
                return null;
            }


        }
        //------- end ------------------

    }

    @Override
    public boolean supports(Class<?> authentication) {

        return CustomTokenAuthentication.class.equals(authentication);
    }
}
