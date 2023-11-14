package claims.security.security.services;

import claims.security.entities.CoreDomainValue;
import claims.security.http.response.CoreUserProfileResponse;
import claims.security.services.DBUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@Service
public class JWTService {

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @Autowired
    private DBUtils db;
    private long expirationTime = 1800000L; //equivalent to 30 minutes;

    @Autowired
//    private InvalidatedTokenRepository invalidatedTokenRepository;

    @PostConstruct
    private void initKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048); //or 1024
        KeyPair keyPair = generator.generateKeyPair();
        privateKey = (RSAPrivateKey)keyPair.getPrivate();
        publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    public String generateToken(String name, List<String> authorities) {

        Optional<CoreDomainValue> coreDomainValueOptional3 = db.coreDomainValueRepository.findById("PASSWORD_CONFIGURATON.TOKEN_LIFE");
        coreDomainValueOptional3.ifPresent(coreDomainValue -> {
            expirationTime= Integer.parseInt(coreDomainValue.getVal1())* 60000L;
        });
        String[] myAuthoritiesArray = new String[authorities.size()];
        myAuthoritiesArray = authorities.toArray(myAuthoritiesArray);

//        LocalDate tokenLife = LocalDate.
        return JWT.create()
                .withClaim("user", name)
                .withArrayClaim("authorities", myAuthoritiesArray)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }

    public String generateTokenFromProfileNames(String name) {
        Optional<CoreDomainValue> coreDomainValueOptional3 = db.coreDomainValueRepository.findById("PASSWORD_CONFIGURATON.TOKEN_LIFE");
        coreDomainValueOptional3.ifPresent(coreDomainValue -> {
            expirationTime= Integer.parseInt(coreDomainValue.getVal1())* 60000L;
        });
        List<String> profiles_ = new ArrayList<>();
//        for (CoreUserProfileResponse p : profiles) {
//            profiles_.add(p.getName());
//        }
        String[] user_registeredProfiles = new String[profiles_.size()];
        user_registeredProfiles = profiles_.toArray(user_registeredProfiles);

//        LocalDate tokenLife = LocalDate.
        return JWT.create()
                .withClaim("user", name)
                .withArrayClaim("authorities", user_registeredProfiles)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.RSA256(publicKey, privateKey));

    }


    public String validateToken(String token) throws JWTVerificationException {
        String tokenPayload_encoded = JWT.require(Algorithm.RSA256(publicKey, privateKey))
                .build()
                .verify(token)
                .getPayload();
        return new String(Base64.getDecoder().decode(tokenPayload_encoded));
    }

    /**
     * this method return true if token is expired or token is invalid(incase user logged out of system)
     * @param jwtToken
     * @return
     */
    public boolean checkExpiredToken(String jwtToken) {
        //
        //check if token is expired:
        String payload = this.validateToken(jwtToken);
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> payLoadMap = parser.parseMap(payload);
        String jwt_life = payLoadMap.get("exp").toString();
        long jwt_period = Long.valueOf(jwt_life);
        System.out.println("jwt ends at: " + jwt_period);
        //
        long currentTime = System.currentTimeMillis();
        System.out.println("current date " + currentTime);
        boolean expired = currentTime > (jwt_period*1000);
        return expired;
        // in case  we are using black list tokens we must check also(in addition to expiry date)
        // if token is saved in blacklist table invalidated_tokens)
//        boolean logged_out_token = invalidatedTokenRepository.existsInvalidatedTokenByToken(jwtToken);
//        return (expired || logged_out_token);
    }











}
