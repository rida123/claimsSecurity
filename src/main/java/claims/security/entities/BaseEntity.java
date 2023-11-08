package claims.security.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class BaseEntity {

    @Column(name="SYS_CREATED_BY")
    private String sysCreatedBy;

    @Column(name="SYS_CREATED_DATE")
    private LocalDateTime sysCreatedDate;

    @Column(name="SYS_UPDATED_BY")
    private String sysUpdatedBy;

    @Column(name="SYS_UPDATED_DATE")
    private LocalDateTime sysUpdatedDate;

    @Column(name="SYS_VERSION_NUMBER")
    private Long sysVersionNumber;

    //default constructor
    public BaseEntity() { }
}
