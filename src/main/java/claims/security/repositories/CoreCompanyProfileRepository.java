package claims.security.repositories;

import claims.security.entities.CoreCompany;
import claims.security.entities.CoreCompanyProfile;
import claims.security.entities.CoreProfile;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CoreCompanyProfileRepository extends BaseRepository<CoreCompanyProfile, String> {

    Optional<CoreCompanyProfile> findByCoreProfileAndCoreCompany(CoreProfile p, CoreCompany c);

    @Query(value = "select cp from CoreCompanyProfile cp join fetch " +
            "cp.coreCompany c join fetch cp.coreProfile p  where c.id = ?1 and " +
            "   p.id not in ?2")
    public List<CoreCompanyProfile> findByCoreCompanyIdAndNotWithinUserProfiles(String companyId, List<String> profileIds);


    @Query(value = "select cp from CoreCompanyProfile cp join fetch " +
            "cp.coreCompany c join fetch cp.coreProfile p  where c.id = ?1  "
           )
    public List<CoreCompanyProfile> findByCoreCompanyId(String companyId);
}
