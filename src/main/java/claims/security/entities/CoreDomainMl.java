package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


@Data
@Entity
@Table(name = "CORE_DOMAIN_ML")
public class CoreDomainMl extends BaseEntity  implements Serializable {
    private static final long serialVersionUID = 8725055481072528273L;

    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(nullable = false, unique = true, length = 6)
    private String lng;

    @Column(length = 128)
    private String description;

    @Column(length = 128)
    private String label1;
    
    @Column(length = 128)
    private String label2;
    
    @Column(length = 128)
    private String label3;
    
    @Column(length = 128)
    private String label4;
    
    @Column(length = 128)
    private String label5;
    
    @Column(length = 128)
    private String label6;
    
    @Column(length = 128)
    private String label7;
    
    @Column(length = 128)
    private String label8;
    
    @Column(length = 128)
    private String label9;
    
    @Column(length = 128)
    private String label10;


    @Column(name = "SYS_ACTIVE_FLAG", nullable = false)
    private Integer sysActiveFlag;

    @ManyToOne
    @JoinColumn(name = "CORE_DOMAIN_ID")
    private CoreDomain coreDomain;
     
    public CoreDomainMl() {
    	this.id = UUID.randomUUID().toString();
    }

}
