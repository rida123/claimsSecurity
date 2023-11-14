package claims.security.security.config;

import claims.security.security.filters.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    JwtAuthFilter jwtAuthFilter;
    @Value("${spring.security.passwordEncoder}")
    private String passwordEncoderClassName;



    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c-> c.disable());
        if(securityProperties.isEnabled()) {

            http.httpBasic(Customizer.withDefaults());

            http.sessionManagement(sessionManagement -> {
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            });

            http.authorizeRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/api/basicAuth/**").permitAll()
                            .requestMatchers("/notification").hasAuthority("read")
                            .anyRequest().authenticated()
            );
              http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        }
        else {
            http.authorizeRequests().anyRequest().permitAll();
        }
        return http.build();
    }
}