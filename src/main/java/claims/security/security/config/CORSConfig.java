package claims.security.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowedOrigins("http://localhost:9090","http://localhost:8080", "http://localhost:4200","http://10.1.208.166:8080","http://192.168.137.134:8080","http://192.168.137.87:8080","http://172.18.92.52:8080","http://172.18.92.53:8080")
                .allowCredentials(true);
        //TODO: Need to change the URL for the production URL when we deploy
    }
}
