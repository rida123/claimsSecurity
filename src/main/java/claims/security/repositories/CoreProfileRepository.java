package claims.security.repositories;

import claims.security.entities.CoreProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface CoreProfileRepository extends BaseRepository<CoreProfile, String>{

//    List<CoreProfile> findByCoreCompany(CoreCompany c);

/*    @Query(value = "select co from CoreProfile  co join fetch co.where co.code not in ?1 ")
public List<CoreProfile> findByCodeNotIn(List<String> allProfiles);*/


                 @Query (   "select o from CoreProfile o where UPPER(o.code) like UPPER(CONCAT('%',CONCAT(:code,'%'))) " +
                            "and UPPER(o.name) like UPPER(CONCAT('%',CONCAT(:name,'%'))) " +
                            "order by o.code")
    public List<CoreProfile> findByCodeAndName(String name,String code);
}
