package claims.security.security.config;

import claims.security.repositories.UserRepository;
import claims.security.security.services.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityServicesConfig {


    @Autowired
    UserRepository userRepository;

}
