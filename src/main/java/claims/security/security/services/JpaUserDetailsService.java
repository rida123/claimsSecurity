package claims.security.security.services;

import claims.security.entities.*;
import claims.security.repositories.CoreUserProfileJpaRepository;
import claims.security.repositories.CoreUserRepository;
import claims.security.security.model.SecurityAuthority;
import claims.security.security.model.SecurityUser;
import claims.security.utils.ClaimsUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
@Slf4j
public class JpaUserDetailsService implements  UserDetailsService {
    @Autowired
    private final CoreUserRepository coreUserRepository;

    private Map<String, Set<CoreRole>> userRolesPerProfile;

    private List<SecurityAuthority> userAuthorities_for_all_profiles;

    @Autowired
    private final CoreUserProfileJpaRepository coreUserProfileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser;
        System.out.println("coming username is: " + username);
        CoreUser foundCoureUser;
//        try {
        Optional<CoreUser> optionalCoreUser = coreUserRepository.findById(username);

        if (!optionalCoreUser.isPresent()) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }

        log.info("User found in the database");
        System.out.println("----------------------------------------------");

        foundCoureUser = optionalCoreUser.get();

        //GET COMPANY OF THIS USER FROM ONE OF HIS PROFILES:
        if (foundCoureUser.getProfiles().size() > 0) {
            String profileId = foundCoureUser.getProfiles().get(0).getId();

            int companyId = Integer.parseInt(profileId.substring(0, profileId.indexOf(".")));
            foundCoureUser.setCompany_id(companyId);
        }

        //THE FOLLOWING  3 steps ARE TO FETCH USER ROELS:

         /* STEP 1: fetching CoreUser registered_profiles(for each profile, a user has several roles) */
        List<CoreUserProfile> registeredProfiles = this.coreUserProfileRepository.findCoreUserProfileByCoreUserId(foundCoureUser.getId());
        //-----------FROM----------

      //=  List<CoreCompanyProfile> companyProfiles = foundCoureUser.getProfiles();

        /**STEP 2:
         * as we have registeredProfiles that represents a list of what profiles the user have
         * within his company => second step is to get what roles  this user have for each
         * profile he has access to. => We created a map of KEY (representing profile id) and
         * VALUE( representing Set of CoreRole(s) of a certain profile.) This map is filled up in helper
         * method: getRolesForInvolvedProfiles(CoreUserProfile coreUser_CompanyProfiles)
         */
        this.userRolesPerProfile = ClaimsUtils.getRolesForInvolvedProfiles(registeredProfiles);

        //STEP 3:
        System.out.println("for each company profile, list roles that user: " + foundCoureUser.getId() + " has:");
        //looping through each entry of this map that represents (profile, set_of_roles_for_that_profile)
        for (Map.Entry<String, Set<CoreRole>> rolesPerProfile_map :  this.userRolesPerProfile.entrySet()) {

            //looping through roles for a profile then creating a Next2Authority for each role
            for (CoreRole role :rolesPerProfile_map.getValue()) {
                Next2Authority authority = new Next2Authority(role.getId(), role.getId(), role.getDescription(), role.getCoreProfile());
                userAuthorities_for_all_profiles.add(new SecurityAuthority(authority));
            }
        }

        securityUser = new SecurityUser(userAuthorities_for_all_profiles, foundCoureUser);
        //returning a spring security user
        return securityUser;
    }
}