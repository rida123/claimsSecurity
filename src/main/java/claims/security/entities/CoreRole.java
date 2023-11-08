package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Table(name = "CORE_ROLE")
public class CoreRole extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -7854836778109067961L;

    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Transient
    private Boolean selected;

    @Transient
    private Boolean granted = Boolean.FALSE;

  /*  @ManyToOne
    @JoinColumn(name = "CORE_PROFILE_ID", nullable = true)*/
   @Column(name="CORE_PROFILE_ID")
    private String coreProfile;

   /* @OneToMany(mappedBy = "coreRole", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CoreUserRole> coreUserRoleList;*/

 /*   @OneToMany(mappedBy = "coreRole", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CoreUserProfilePerm> coreUserProfilePermList;

    @OneToMany(orphanRemoval = true, mappedBy = "coreRole", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CoreTaskflowPerm> coreTaskflowPermList;*/

    @Column(name = "DESCRIPTION", nullable = false, length = 4000)
    private String description;



    public CoreRole() {
         this.id=UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CoreRole role = (CoreRole) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
