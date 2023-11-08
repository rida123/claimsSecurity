package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CORE_USER")
@Data
public class CoreUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3961396589345698205L;

    @Id
    private String id;

    private String pwd;

    @Column(name = "ACTIVE_FLAG")
    private Integer activeFlag;

   /* @Column(name = "CORE_USER_ID")
    private String username;*/

    @Column(name = "PWD_ENCRYPTED")
    private String encryptedPwd;

    // Management of Checkbox when is based item
    @Transient
    private Boolean activeFlagBoolean;

    @Transient
    private String rowState;

    @Transient
    boolean back;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "core_user_profile",
            joinColumns = @JoinColumn(name = "core_user_id"),
            inverseJoinColumns = @JoinColumn(name = "core_company_profile_id")
    )
    private List<CoreCompanyProfile> profiles = new ArrayList<>();

    @Transient
    private int company_id;

//    public CoreUser() {
//        this.id = UUID.randomUUID().toString();
//    }

    public void denyProfile(CoreCompanyProfile profile) {
        if(this.profiles.contains(profile)) {
            boolean done = this.profiles.remove(profile);
            System.out.println("profile remove result: " + done);
        }
    }
 public String toString() {
        return getId().toString();
    }

}