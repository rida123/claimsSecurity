package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "core_user_profile")
@Data
public class CoreUserProfile extends  BaseEntity {

    @Id
    private String id;

    @Column(name = "core_user_id")
    private String coreUserId;

    @Column(name = "core_company_profile_id")
    private String coreCompanyProfileId;

    @Column(name="FULL_FLAG")
    private Integer fullFlag;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "core_user_profile_perm",
            joinColumns = @JoinColumn(name = "core_user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "core_role_id")
    )
    private Set<CoreRole> userRoles;

    public CoreUserProfile() {
//        this.id = UUID.randomUUID().toString(); (id is generated programmatically)
    }
}
