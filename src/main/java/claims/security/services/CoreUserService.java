//rida
package claims.security.services;

import claims.security.entities.*;
import claims.security.exceptions.BadRequestException;
import claims.security.exceptions.NotFoundException;
import claims.security.http.response.ApiResponse;
import claims.security.http.response.StatusCode;
import claims.security.repositories.CoreCompanyProfileRepository;
import claims.security.repositories.CoreCompanyRepository;
import claims.security.repositories.CoreUserDaoRepository;
import claims.security.repositories.CoreUserRepository;
import claims.security.utils.ClaimsUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class CoreUserService extends BaseService<CoreUser> {
    @Autowired
    private DBUtils db;

    @Autowired
    CarsInsuranceEmployeeService carsInsuranceEmployeeService;

    @Autowired
    private CoreUserDaoRepository userDaoRepository;

    @Autowired
    private ClaimsUtils claimsUtils;

    @Autowired
    private PwdEncoder passwordEncoder;

    @Autowired
    private CoreCompanyProfileRepository coreCompanyProfileRepository;

    @Autowired
    private CoreCompanyProfileService companyProfileService;

    @Autowired
    private CoreCompanyService companyService;

    @Autowired
    private CoreProfileService profileService;
    @Autowired
    private CoreUserRepository coreUserRepository;

    @Autowired
    private CoreUserProfileService coreUserProfileService;

    @Autowired
    private CoreUserProfilePermService coreUserProfilePermService;
    @Autowired
    private CoreCompanyRepository coreCompanyRepository;

    @PersistenceContext
    private EntityManager em;

    //old params: (String coreUserId, CoreCompanyProfile profile)

    /**
     * searchUserByRole
     */
    public ApiResponse searchUsersByUserInfoAndRole(String username, String displayname, String roleId){
        List<UserInfo> userInfoList =  this.userDaoRepository.searchUsersByRole(username, displayname, roleId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setTitle("User search result");
        apiResponse.setData(userInfoList);
        return  apiResponse;
    }
    //end searchUserByRole

    public CoreCompany getCompanyByCoreUser(String coreUserId) {
        //1-if coreUserId is wrong => getEmployeeInfo will throw an exception
        CarsInsuranceEmployee employeeInfo = getEmployeeInfo(coreUserId);

        //usersInsurance in the database represents company that user works for
        String company_branch_info = employeeInfo.getCarsBranch().getId();
        String insurance_companyId = String.valueOf(employeeInfo.getCarsBranch().getId().substring(0, company_branch_info.indexOf(".")));
        Optional<CoreCompany> optionalCoreCompany = this.companyService.findById(insurance_companyId);
        if (optionalCoreCompany.isEmpty()) {
            throw new NotFoundException("CoreUserService => Company Id parameter is wrong");
        }
        return optionalCoreCompany.get();

    }


    @Transactional

    public CarsInsuranceEmployee getEmployeeInfo(String coreUserId) {
        Optional<CarsInsuranceEmployee> checkEmployee = this.carsInsuranceEmployeeService.findByUsersCode(coreUserId);
        CarsInsuranceEmployee employeeInfo = checkEmployee.orElseThrow(() -> new BadRequestException("user of ID: " + coreUserId + " doesn't exist"));
        return employeeInfo;
    }


    /**
     * get all profiles that a user is enrolled in
     *
     * @param userId
     * @return
     */
    public List<CoreProfile> getProfilesPerUser(String userId) {

        CoreUser foundCoureUser = this.getCoreUser(userId);

        /*testing:
        System.out.println("profiles that i have:");
        for (CoreCompanyProfile p: foundCoureUser.getProfiles()) {
            System.out.println("code: " + p.getId());
        }*/
        List<CoreUserProfile> registeredProfiles = this.coreUserProfileService.getUserProfiles(foundCoureUser.getId());

        List<CoreProfile> myCoreProfiles = new ArrayList<>();

        for (CoreUserProfile core_user_profile : registeredProfiles) {
            String coreCompanyProfileId = core_user_profile.getCoreCompanyProfileId();
            //now fetch profile_id:
            String coreProfileId = (coreCompanyProfileId.substring(coreCompanyProfileId.indexOf(".") + 1));
            //now fetch CoreProfile object consisting of all roles for this profile part of these roles
            // are granted to the user and others not granted yet
            CoreProfile coreProfile = this.getCoreProfile(coreProfileId);

            Set<CoreRole> allRolesPerProfile = coreProfile.getProfileRoles();

            if (core_user_profile.getUserRoles() != null) {
                for (CoreRole granted_role : core_user_profile.getUserRoles()) {
                    for (CoreRole role : allRolesPerProfile) {
                        if (granted_role.equals(role)) {
                            role.setGranted(true);
                            break;
                        }
                    }
                }
            }

            coreProfile.setRoles(allRolesPerProfile);
            myCoreProfiles.add(coreProfile);
            Collections.sort(myCoreProfiles, new Comparator<CoreProfile>() {
                @Override
                public int compare(CoreProfile profile1, CoreProfile profile2) {
                    return profile1.getDescription().compareTo(profile2.getDescription());
                }
            });
        }
        System.out.println("end looping through map");
        return myCoreProfiles;
    }

    public CoreUser getCoreUser(String coreUserId) {
        Optional<CoreUser> checkUser = this.coreUserRepository.findById(coreUserId);
        CoreUser user = checkUser.orElseThrow(() -> new NotFoundException("user of ID: " + coreUserId + " doesn't exist"));
        return user;
    }

    public CoreProfile getCoreProfile(String profileId) {
        Optional<CoreProfile> checkProfile = this.profileService.findById(profileId);
        CoreProfile profile = checkProfile.orElseThrow(() -> new BadRequestException("profile of ID: " + profileId + " doesn't exist"));
        return profile;
    }

    @Transactional

    public CoreCompanyProfile getCompanyProfile(String profileId, String companyId) {
        return this.companyProfileService.
                findByCoreProfileAndCoreCompany(profileId, companyId).orElseThrow(() -> new NotFoundException("company profile of ID: " + profileId + " doesn't exist"));

    }


    public List<CoreRole> getProfileDefaultAccessRoles(String userProfileId) {
        System.out.println("test");
        if (!userProfileId.isEmpty()) {
            System.out.println(userProfileId);
            StringBuffer stringBuffer = new StringBuffer(" ");
            stringBuffer.append(" select rol.* from CORE_ROLE rol , CORE_USER_PROFILE uProf , CORE_COMPANY_PROFILE cProf ,CORE_PROFILE prof ");
            stringBuffer.append(" where uProf.CORE_COMPANY_PROFILE_ID = cProf.ID and cProf.CORE_PROFILE_ID = prof.ID and rol.CORE_PROFILE_ID = prof.ID");
            stringBuffer.append(" and uProf.ID = ? and NVL(rol.ROLE_DEFAULT_ACCESS,'N') = 'Y' order by UPPER(rol.ID) ");
            Query query = em.createNativeQuery(stringBuffer.toString(), CoreRole.class);
            query.setParameter(1, userProfileId);
            return query.getResultList();


        }

        return null;
    }


    /**
     * this method checks if first login of the user
     *
     * @param coreUserId
     * @return
     */
    public boolean isFirstLogin(String coreUserId) {
        Optional<CoreUserPreference> userPreference_check =
                this.db.coreUserPreferenceRepository.findByCoreUser(coreUserId);
        if (userPreference_check.isEmpty()) {
            throw new NotFoundException("User of ID: " + coreUserId + " not found in database");
        }
        LocalDateTime pwd_updated_date = userPreference_check.get().getPwdLastUpdatedDate();
        Long loginAfterPassword_updated =
                this.db.userTraceRepository.countSuccessfulLoginsAfterPwdChange(coreUserId, pwd_updated_date);

        System.out.println("Successful login attempts after password change or after user being added => "
                + loginAfterPassword_updated);
        if (loginAfterPassword_updated > 0) {
            return false; //not first login
        }
        return true; //first login

    }

    /*  public List<CoreUser> getAllUsers() {
          List<String> usernames= new ArrayList<>();
          List<CoreUser> coreUsers= db.userRepository.findAll();
         *//* coreUsers.forEach(coreUser -> {
            usernames.add(coreUser.getId());
        });
        return  usernames;*//*
        return coreUsers;
    }*/
    public boolean matchRegex(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public ApiResponse findUserByCompanyAndName(String companyId, String nameSubstring) {

        List<CoreUserPreference> users = this.userDaoRepository.searchCoreUser(companyId, nameSubstring);
/*
      List<CoreUserPreference> users =
              this.db.coreUserPreferenceRepository.searchUserByCompanyAndName(companyId, nameSubstring);*/
        List<CoreUserResponse> userResponses = new ArrayList<>();
        for (CoreUserPreference userPref : users) {
            CoreUserResponse userResponse = new CoreUserResponse(userPref.getCoreUser().getId(), userPref.getDisplayName());
            userResponses.add(userResponse);
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(StatusCode.OK.getCode());
        apiResponse.setTitle("Users by company Id and name substring");
        apiResponse.setData(userResponses);
        return apiResponse;
    }
}
