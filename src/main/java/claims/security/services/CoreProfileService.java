package claims.security.services;

import claims.security.entities.CoreProfile;
import claims.security.repositories.CoreProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoreProfileService extends BaseService<CoreProfile> {

    @Autowired
    CoreProfileRepository coreProfileRepository;

}
