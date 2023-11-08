package claims.security.repositories;

import claims.security.entities.CoreUserProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface CoreUserProfileJpaRepository extends BaseRepository<CoreUserProfile, String> {

    List<CoreUserProfile> findCoreUserProfileByCoreUserId(String coreUserId);

    @Query(value =  "select pTfPerm.ID from CORE_USER_PROFILE uProf , CORE_COMPANY_PROFILE cProf ," +
            " CORE_PROFILE prof ,  CORE_TASKFLOW_PERM tfPerm , CORE_PROFILE_TASKFLOW_PERM pTfPerm " +
            " where uProf.ID = ?2 and  uProf.CORE_COMPANY_PROFILE_ID = cProf.ID and " +
            " cProf.CORE_PROFILE_ID = prof.ID and prof.CORE_TASKFLOW_ID = tfPerm.CORE_TASKFLOW_ID " +
            " and tfPerm.PERMISSION_VERB = ?1  and tfPerm.ID = pTfPerm.CORE_TASKFLOW_PERM_ID ",
            nativeQuery = true)
    String getCoreProfTfPermId(String role , String userProf);

    @Query(value =  "select cup from CoreUserProfile cup join fetch cup.userRoles r where cup.coreUserId = ?1" )
    List<CoreUserProfile> findCoreUserProfileByCoreUserIdWithAllData(String coreUserId);

    @Query(value = "SELECT COUNT(*) FROM CORE_USER_PROFILE prf, CORE_USER_PROFILE_PERM perm " +
            "WHERE perm.CORE_USER_PROFILE_ID = prf.ID " +
            "AND perm.CORE_ROLE_ID = :roleId " +
            "AND prf.CORE_USER_ID = :userId", nativeQuery = true)
    int countUserPermissionByRole(@Param("roleId") String roleId, @Param("userId") String userId);

}
