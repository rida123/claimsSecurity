package claims.security.repositories;

import claims.security.entities.CoreUserPreference;
import claims.security.http.response.CoreUserPreferenceProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface CoreUserPreferenceRepository extends  BaseRepository<CoreUserPreference,String>{

@Query(value = "select  up  from CoreUserPreference up join fetch up.coreUser u" +
        " join fetch up.coreCompany c where "  +
        " u.id = ?1  ")
    Optional<CoreUserPreference> findByCoreUser(String id);




    @Query(" Select up from CoreUserPreference up  " +
            " join fetch up.coreUser u " +
            "   join fetch up.coreCompany c  where " +
            " lower(up.displayName) LIKE  %?1%  and  lower(u.id)  LIKE %?2% ")
            List<CoreUserPreference> searchByDisplayNameAndUserName( String displayName ,String username );

    @Query(" Select up from CoreUserPreference up  " +
            " join fetch up.coreUser u " +
            "   join fetch up.coreCompany c  where " +
            "  lower(u.id) LIKE %?1% ")
    List<CoreUserPreference> searchByUserName( String username );

    @Query(" Select up from CoreUserPreference up  " +
            " join fetch up.coreUser u " +
            "   join fetch up.coreCompany c  where " +
            " lower(up.displayName) LIKE  %?1% ")
    List<CoreUserPreference> searchByDisplayName(String displayName);


    @Query("select e from CoreUserPreference  e where e.coreCompany.id = ?1 and lower(e.displayName) like %?2%")
    List<CoreUserPreference> searchUserByCompanyAndName(String companyId, String nameSubstring);

    List<CoreUserPreference> searchByDisplayNameContainingIgnoreCase(String subString);

    @Query(value = "SELECT ID, DISPLAY_NAME FROM CORE_USER_PREFERENCE", nativeQuery = true)
    List<CoreUserPreferenceProjection> findUserNameCodeAndDescription();


}
