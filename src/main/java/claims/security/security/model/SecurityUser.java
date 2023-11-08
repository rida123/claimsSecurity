package claims.security.security.model;

import claims.security.entities.*;
import claims.security.repositories.CoreUserProfileJpaRepository;
import claims.security.utils.ClaimsUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@AllArgsConstructor
public class SecurityUser implements UserDetails {

//    private final AppUser user;

    private final CoreUserProfileJpaRepository coreUserProfileRepository;
    private final CoreUser coreUser;

    private Map<String, Set<CoreRole>> userRolesPerProfile;

    public SecurityUser(CoreUserProfileJpaRepository coreUserProfileRepository, CoreUser coreUser) {
        this.coreUser = coreUser;
        this.coreUserProfileRepository = coreUserProfileRepository;
    }


    public CoreUser getCoreUser() {
        return this.coreUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<CoreCompanyProfile> companyProfiles = this.coreUser.getProfiles();

        List<CoreUserProfile> registeredProfiles = this.coreUserProfileRepository.findCoreUserProfileByCoreUserId(coreUser.getId());

        //for testing we list profiles:

        System.out.println(":::::: TESTING::: listing registered coreUserProfiles for user: " + coreUser.getId());
        for(CoreUserProfile coreUserProfile: registeredProfiles){
            System.out.println("profile: " + coreUserProfile.getCoreCompanyProfileId());
        }
        System.out.println("::::END TESTING::::");
//        List<CoreRole> userRoles = new ArrayList<>();

        /**
         * as we have registeredProfiles that represents a list of what profiles the user have
         * within his company => second step is to get what roles  this user have for each
         * profile he has access to. => We created a map of key representing profile id and value
         * representing Set of CoreRole(s) of a certain profile. This map is filled up in helper
         * method: getRolesForInvolvedProfiles(CoreUserProfile coreUser_CompanyProfiles)
         */
        this.userRolesPerProfile = ClaimsUtils.getRolesForInvolvedProfiles(registeredProfiles);

        List<SecurityAuthority> grantedAuthorities_for_all_profiles = new ArrayList<>();

        //TESTING DATA:::
        System.out.println("for each company profile, list roles that user: " + coreUser.getId() + " has:");
        for (Map.Entry<String,  Set<CoreRole>> entry :  this.userRolesPerProfile.entrySet()) {
            // TESTING DATA:::
            System.out.println("PROFILE: " + entry.getKey() + ", user:  " + coreUser.getId() + " has the following roles:");
            System.out.println("----------");
            for (CoreRole r: entry.getValue()) {
                System.out.println("     ROLE: " + r.getDescription());
            }
            System.out.println("**** end testing ****");
            for (CoreRole role :entry.getValue()) {
                Next2Authority authority = new Next2Authority(role.getId(), role.getId(), role.getDescription(), role.getCoreProfile());
                grantedAuthorities_for_all_profiles.add(new SecurityAuthority(authority));
            }
        }
        //        return List.of(()->"read");
      /*  return user.getAuthorities()
                .stream().map(SecurityAuthority::new).collect(Collectors.toList());*/
        //RETURNING ALL ALL USER AUTHORITIES FOR ALL PROFILES
        return grantedAuthorities_for_all_profiles;
    }

    @Override
    public String getPassword() {
        return coreUser.getEncryptedPwd();
    }

    @Override
    public String getUsername() {
        return this.coreUser.getId();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if(coreUser.getActiveFlag()==1){
            return  true;
        }
        else {
            return false;
        }
    }

  /*  @Override
    public String toString() {
        return "SecurityUser{" +
                ", user=" + user +
                '}';
    }*/

    public  Map<String, Set<CoreRole>> getUserRolesPerProfile() {
        return this.userRolesPerProfile;
    }
}
