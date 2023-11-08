package claims.security.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myapp.security")
@Getter
@Setter
public class SecurityProperties {
    private boolean enabled;


    @Value("${myapp.security.userTable}")
    private String userTable;

    @Value("${myapp.security.authorityTable}")
    private String authorityTable;


    // getters and setters
}
