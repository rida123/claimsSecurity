package claims.security.repositories;

import claims.security.entities.CoreCompany;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface CoreCompanyRepository extends BaseRepository<CoreCompany, String> {


    @Query(value =" select distinct cProf.CORE_COMPANY_ID from CORE_USER_PROFILE prof , CORE_USER_PROFILE_PERM perm , CORE_COMPANY_PROFILE cProf " +

            " where  prof.CORE_USER_ID = ?1  and " +

            "            prof.ID = perm.CORE_USER_PROFILE_ID  and prof.CORE_COMPANY_PROFILE_ID = cProf.ID "
            ,nativeQuery = true)
    Long findCompanyByUser(String user);
    @Query(value =" select  c from CoreCompany c where id=?1 ")
    List<CoreCompany> findCompanyByID(String id);
}
