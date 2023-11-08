package claims.security.repositories;

import claims.security.entities.*;
import claims.security.services.DBUtils;
import claims.security.utils.ClaimsUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor //in order to make constructor injection
public class CoreUserDaoRepository {

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    private DBUtils dbUtils;

    @Autowired
    private ClaimsUtils claimsUtils;

    public List<CoreUserPreference> searchCoreUser(String insuranceId, String nameSubstring) {

        CarsInsurance insurance = em.find(CarsInsurance.class, insuranceId);

        List<Predicate> conditions = new ArrayList<>();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<CoreUserPreference> criteriaQuery = criteriaBuilder.createQuery(CoreUserPreference.class); //passing result class as param

        //        select * from cars_client; as follows:
        Root<CoreUserPreference> root = criteriaQuery.from(CoreUserPreference.class);
        //now WHERE clause(each predicate is a where condition):
        Join<CoreUserPreference, CoreCompany> user_company_Join = root.join("coreCompany");
        Predicate insurance_predicate = criteriaBuilder.equal(user_company_Join.get("id"), insurance.getInsuranceId());

        if (insurance != null) {
            conditions.add(insurance_predicate);
        }

        //where name
        if (nameSubstring != null && !nameSubstring.trim().isEmpty()) {
            Predicate name_predicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("displayName")), "%" + nameSubstring.toLowerCase() + "%");
            conditions.add(name_predicate);
        }

        //toArray(new Predicate[0])) will return table of predicates
        criteriaQuery.where(criteriaBuilder.and(conditions.toArray(new Predicate[0])));

        TypedQuery<CoreUserPreference> clientQuery = em.createQuery(criteriaQuery);
        return clientQuery.getResultList();
    }

    public List<UserInfo> searchUsersByRole(String username, String displayname, String roleId) {
        List<Predicate> conditions = new ArrayList<>();
        List<String> usersThatHaveThisRole = new ArrayList<>();
        boolean usernameAndDisplayNameCriteria = false;
        boolean roleCriteria = false;

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<CoreUserPreference> criteriaQuery = criteriaBuilder.createQuery(CoreUserPreference.class); //passing result class as param

        Root<CoreUserPreference> root = criteriaQuery.from(CoreUserPreference.class);

        //where displayname
        if(displayname != null && !displayname.trim().isEmpty()) {
            Predicate displayname_predicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("displayName")), "%" + displayname.toLowerCase() + "%");
            conditions.add(displayname_predicate);
            usernameAndDisplayNameCriteria = true;
        }
        //where username
        if(username != null && !username.trim().isEmpty()) {
            Join<CoreUserPreference, CoreUser> userPref_coreUser_Join = root.join("coreUser");

            Predicate username_predicate = criteriaBuilder.like(  criteriaBuilder.lower(userPref_coreUser_Join.get("id")),  "%" +username.toLowerCase() +  "%" );

/*            Predicate username_predicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("displayName")), "%" + displayname.toLowerCase() + "%");*/
            conditions.add(username_predicate);
            usernameAndDisplayNameCriteria = true;
        }
        //toArray(new Predicate[0])) will return table of predicates
        criteriaQuery.where(criteriaBuilder.and(conditions.toArray(new Predicate[0])));

        TypedQuery<CoreUserPreference> userPreferenceQuery  = em.createQuery(criteriaQuery);
        List<CoreUserPreference> userPreferenceList = userPreferenceQuery.getResultList();

        if(roleId != null && !roleId.trim().isEmpty()){
            List<CoreUserProfilePerm> usersHavingGivenRole = dbUtils.coreUserProfilePermRepository.getUserProfilesWithRole(roleId);
            usersHavingGivenRole.forEach((userProfilePerm) -> {
                CoreUserProfile userProfile = userProfilePerm.getCoreUserProfile();
                if(userProfile != null) {
                    usersThatHaveThisRole.add(userProfile.getCoreUserId());
                }
            });
            roleCriteria = true;
        }
        //return username and display name search that may have or have no where condition
        if(roleCriteria == false){
            List<UserInfo> usersHavingRoleParam = new ArrayList<>();
            userPreferenceList.forEach( (userPref)-> {
                UserInfo userInfo = new UserInfo();
                CoreUser coreUser = userPref.getCoreUser();
                userInfo.setSysCreatedBy(claimsUtils.getUserDisplayName(coreUser.getSysCreatedBy()));
                userInfo.setSysUpdatedBy(claimsUtils.getUserDisplayName(coreUser.getSysUpdatedBy()));
                userInfo.setSysCreatedDate(coreUser.getSysCreatedDate());
                userInfo.setSysUpdatedDate(coreUser.getSysUpdatedDate());
                userInfo.setActive(coreUser.getActiveFlag());
                userInfo.setActiveDesc(coreUser.getActiveFlag() == 1 ? "Active" : "InActive");

                userInfo.setUserName(coreUser.getId());
                //
                if (coreUser.getId() != null) {
                    Optional<CarsInsuranceEmployee> carsInsuranceEmployeeOptional = dbUtils.carsInsuranceEmployeeRepository.findByUsersCode(coreUser.getId());
                    carsInsuranceEmployeeOptional.ifPresentOrElse(
                            (carsInsuranceEmployee) -> {
                                userInfo.setPaymentLimit(carsInsuranceEmployee.getUsersLimit());
                                userInfo.setRecoverLimit(carsInsuranceEmployee.getUserLimitRecovery());
                                userInfo.setUserLimitDoctorFees(carsInsuranceEmployee.getUserLimitDoctorFees());
                                userInfo.setUserLimitLawyerFees(carsInsuranceEmployee.getUserLimitLawyerFees());
                                userInfo.setUserLimitHospitalFees(carsInsuranceEmployee.getUserLimitHospitalFees());
                                userInfo.setUserLimitSurveyFees(carsInsuranceEmployee.getUserLimitSurveyFees());

                                userInfo.setUserLimitTaxiFees(carsInsuranceEmployee.getUserLimitTaxiFees());
                                userInfo.setUserLimitExpertFees(carsInsuranceEmployee.getUserLimitExpertFees());
                                userInfo.setUserLimitExceedPercentage(carsInsuranceEmployee.getUserLimitExceedPercentage());
                                if (carsInsuranceEmployee.getUsersBranch() != null) {
                                    userInfo.setBranchId(carsInsuranceEmployee.getUsersBranch().toString());
                                }

                            },
                            () -> {

                            }

                    );

                }
                Optional<CoreUserPreference> coreUserPreferenceOptional = dbUtils.coreUserPreferenceRepository.findByCoreUser(coreUser.getId());
                coreUserPreferenceOptional.ifPresentOrElse(
                        (coreUserPreference) -> {
                            userInfo.setEmail(coreUserPreference.getUserEmail());
                            userInfo.setCompanyId(coreUserPreference.getCoreCompany().getId());
                            userInfo.setCompanyDescription(coreUserPreference.getCoreCompany().getLegalName());
                            userInfo.setDisplayName(coreUserPreference.getDisplayName());
                            userInfo.setUserEmailSignature(coreUserPreference.getUserEmailSignature());
                            userInfo.setUserPicture(coreUserPreference.getUserPicture());
                            userInfo.setSysCreatedBy(claimsUtils.getUserDisplayName(coreUserPreference.getCoreUser().getSysCreatedBy()));
                            userInfo.setSysUpdatedBy(claimsUtils.getUserDisplayName(coreUserPreference.getCoreUser().getSysUpdatedBy()));
                            userInfo.setSysCreatedDate(coreUserPreference.getCoreUser().getSysCreatedDate());
                            userInfo.setSysUpdatedDate(coreUserPreference.getCoreUser().getSysUpdatedDate());
                        },
                        () -> {

                        });
                usersHavingRoleParam.add(userInfo);
            });
            return usersHavingRoleParam;
        }
        else {/////////
            List<UserInfo> usersHavingRoleParam = new ArrayList<>();
            userPreferenceList.forEach( (userPref)-> {
                CoreUser coreUser = userPref.getCoreUser();
                if(usersThatHaveThisRole.contains(coreUser.getId())){
                    UserInfo userInfo = new UserInfo();
                    userInfo.setSysCreatedBy(claimsUtils.getUserDisplayName(coreUser.getSysCreatedBy()));
                    userInfo.setSysUpdatedBy(claimsUtils.getUserDisplayName(coreUser.getSysUpdatedBy()));
                    userInfo.setSysCreatedDate(coreUser.getSysCreatedDate());
                    userInfo.setSysUpdatedDate(coreUser.getSysUpdatedDate());
                    userInfo.setActive(coreUser.getActiveFlag());
                    userInfo.setActiveDesc(coreUser.getActiveFlag() == 1 ? "Active" : "InActive");

                    userInfo.setUserName(coreUser.getId());
                    //
                    if (coreUser.getId() != null) {
                        Optional<CarsInsuranceEmployee> carsInsuranceEmployeeOptional = dbUtils.carsInsuranceEmployeeRepository.findByUsersCode(coreUser.getId());
                        carsInsuranceEmployeeOptional.ifPresentOrElse(
                                (carsInsuranceEmployee) -> {
                                    userInfo.setPaymentLimit(carsInsuranceEmployee.getUsersLimit());
                                    userInfo.setRecoverLimit(carsInsuranceEmployee.getUserLimitRecovery());
                                    userInfo.setUserLimitDoctorFees(carsInsuranceEmployee.getUserLimitDoctorFees());
                                    userInfo.setUserLimitLawyerFees(carsInsuranceEmployee.getUserLimitLawyerFees());
                                    userInfo.setUserLimitHospitalFees(carsInsuranceEmployee.getUserLimitHospitalFees());
                                    userInfo.setUserLimitSurveyFees(carsInsuranceEmployee.getUserLimitSurveyFees());

                                    userInfo.setUserLimitTaxiFees(carsInsuranceEmployee.getUserLimitTaxiFees());
                                    userInfo.setUserLimitExpertFees(carsInsuranceEmployee.getUserLimitExpertFees());
                                    userInfo.setUserLimitExceedPercentage(carsInsuranceEmployee.getUserLimitExceedPercentage());
                                    if (carsInsuranceEmployee.getUsersBranch() != null) {
                                        userInfo.setBranchId(carsInsuranceEmployee.getUsersBranch().toString());
                                    }

                                },
                                () -> {

                                }

                        );

                    }
                    Optional<CoreUserPreference> coreUserPreferenceOptional = dbUtils.coreUserPreferenceRepository.findByCoreUser(coreUser.getId());
                    coreUserPreferenceOptional.ifPresentOrElse(
                            (coreUserPreference) -> {
                                userInfo.setEmail(coreUserPreference.getUserEmail());
                                userInfo.setCompanyId(coreUserPreference.getCoreCompany().getId());
                                userInfo.setCompanyDescription(coreUserPreference.getCoreCompany().getLegalName());
                                userInfo.setDisplayName(coreUserPreference.getDisplayName());
                                userInfo.setUserEmailSignature(coreUserPreference.getUserEmailSignature());
                                userInfo.setUserPicture(coreUserPreference.getUserPicture());
                                userInfo.setSysCreatedBy(claimsUtils.getUserDisplayName(coreUserPreference.getCoreUser().getSysCreatedBy()));
                                userInfo.setSysUpdatedBy(claimsUtils.getUserDisplayName(coreUserPreference.getCoreUser().getSysUpdatedBy()));
                                userInfo.setSysCreatedDate(coreUserPreference.getCoreUser().getSysCreatedDate());
                                userInfo.setSysUpdatedDate(coreUserPreference.getCoreUser().getSysUpdatedDate());
                            },
                            () -> {

                            });
                    usersHavingRoleParam.add(userInfo);
                }

            });
            return usersHavingRoleParam;
        }//end else

    }//end function searchUsersByRole

}
