package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "core_user_trace")
@Data
public class UserTrace implements Serializable {

    @Id
    private String id;

    @Column(name = "core_user_id")
    private String coreUserId;

    @Column(name="core_company_id")
    private Integer companyId;

    private String comments;

    @Column(name="object_code")
    private String objectCode;

    @Column(name="object_id")
    private String objectId;

    @Column(name="SYS_CREATED_DATE")
    private LocalDateTime sysCreatedDate;

    @Column(name="SYS_UPDATED_DATE")
    private LocalDateTime sysUpdatedDate;

  @Transient
    private String displayName;

    @Transient
    private byte[] logo;

    @Transient
    private byte[] userPicture;

    @Column(name="SYS_VERSION_NUMBER")
    private Long sysVersionNumber;

    public UserTrace() {
        this.id= UUID.randomUUID().toString();
    }

//    public UserTrace(String coreUserId, String companyId, String comments, LocalDateTime createdDate)


    @Override
    public String toString() {
        return "UserTrace{" +
                "id='" + id + '\'' +
                ", coreUserId='" + coreUserId + '\'' +
                ", companyId=" + companyId +
                ", sysCreatedDate=" + sysCreatedDate +
                '}';
    }
}
