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

    private final CoreUser coreUser;
    List<SecurityAuthority> userAuthorities_for_all_profiles;

    public SecurityUser(List<SecurityAuthority> userAuthorities, CoreUser coreUser) {
        this.coreUser = coreUser;
        this.userAuthorities_for_all_profiles = userAuthorities;
    }

    public CoreUser getCoreUser() {
        return this.coreUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.userAuthorities_for_all_profiles;
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
}
