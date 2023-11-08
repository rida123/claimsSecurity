package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "CORE_PROFILE")
public class CoreProfile extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8614115025161705635L;

    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(nullable = false, length = 12)
    private String code;

    @Column(name = "SORT_CODE", nullable = true, length = 30)
    private String sortCode;

    @Column(nullable = false, unique = true, length = 60)
    private String name;

    @Column(nullable = false, length = 100)
    private String description;


   /* @ManyToOne
    @JoinColumn(name = "CORE_TASKFLOW_ID", nullable = true)
    private CoreTaskflow coreTaskflow;*/

    @Column(name = "PROFILE_TYPE", length = 32)
    private String profileType;

    @Column(name = "RENDERED_EXPRESSION", length = 256)
    private String renderedExpression;

    @Transient
    private Boolean selected;
    
    @Transient
    private String taskflowCode;

    @OneToMany
    @JoinColumn(name="core_profile_id")
    private Set<CoreRole> profileRoles;
    
    @Transient
    private String rowState;

    public CoreProfile() {
       this.id = UUID.randomUUID().toString();
    }

    public void setRoles(Set<CoreRole> roles) {
        this.profileRoles = roles;
    }
}
