package claims.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;

@Configuration
public class PwdEncoder {

    @Autowired
    DBUtils db;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                String passwordToHash = String.valueOf(rawPassword);
                String generatedPassword = null;
                try {
                    MessageDigest md = this.getMessageDigest();
                    String value = getConfigurationValue("saltSha");
                    StringUtils.hasLength(value);
                    md.update(value.getBytes());
                    byte[] bytes = md.digest(passwordToHash.getBytes());
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < bytes.length; i++) {
                        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    generatedPassword = sb.toString();
                    return generatedPassword;
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }

            }

            public String getConfigurationValue(String key) {
                String value = null;
                value= db.coreUserRepository.findConfigByKey(key);
                return value;
            }

            public MessageDigest getMessageDigest() throws  Exception {
                return  MessageDigest.getInstance("SHA-512");
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                String plainPassword = String.valueOf(rawPassword);
                return    (!encode(plainPassword).matches(encodedPassword) );
            }
        };
    }


    public  String getConfigurationValue(String key) {
        String value = null;
        value = this.db.coreUserRepository.findConfigByKey(key);
        return value;
    }

}
