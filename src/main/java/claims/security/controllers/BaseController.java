package claims.security.controllers;


import claims.security.security.model.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {
    //  private TokenUtil tokenUtil;
    public SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Type===== " + authentication.getPrincipal().toString());
        return (SecurityUser) authentication.getPrincipal();

    }


}
