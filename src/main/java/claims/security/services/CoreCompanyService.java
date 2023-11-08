package claims.security.services;

import claims.security.entities.CoreCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoreCompanyService extends BaseService<CoreCompany> {

    @Autowired
    DBUtils db;

    
}
