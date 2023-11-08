package claims.security.services;

import claims.security.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBUtils {

    @Autowired
    public CoreDomainValueRepository coreDomainValueRepository;

    @Autowired
    public CoreUserRepository coreUserRepository;

    @Autowired
    public CoreUserPreferenceRepository coreUserPreferenceRepository;

    @Autowired
    public CoreDocumentFileRepository coreDocumentFileRepository;

    @Autowired
    public RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public CoreUserProfilePermRepository coreUserProfilePermRepository;
    @Autowired
    public CarsInsuranceEmployeeRepository carsInsuranceEmployeeRepository;

    @Autowired
    public CoreUserProfileJpaRepository coreUserProfileJpaRepository;

    @Autowired
    public UserTraceRepository userTraceRepository;

}
