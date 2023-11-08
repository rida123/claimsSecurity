package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "CORE_DOMAIN_VALUE")
public class CoreDomainValue extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -1510709639133809428L;

    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(nullable = false, length = 128)
    private String code;

    @Column(nullable = false, length = 128)
    private String description;

    @Column(name = "SYS_ACTIVE_FLAG", nullable = false)
    private Integer sysActiveFlag = Integer.valueOf(0);
    @Column(length = 128)
    private String val1;
  
    @Column(length = 128)
    private String val10;
    @Column(length = 128)
    private String val11;
    @Column(length = 128)
    private String val2;
    
    @Column(length = 128)
    private String val3;
    
    @Column(length = 128)
    private String val4;
    
    @Column(length = 128)
    private String val5;
    
    @Column(length = 128)
    private String val6;
    
    @Column(length = 128)
    private String val7;
    
    @Column(length = 128)
    private String val8;
    
    @Column(length = 128)
    private String val9;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORE_DOMAIN_ID")
    private CoreDomain coreDomain;

    /*
    *TODO: commented out on 27/04/2023 to be uncommented when needed
    @OneToMany(mappedBy = "coreDomainValue", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CoreDomainValueMl> coreDomainValueMlList;
*/
   /* public CoreDomainValue() {
    	this.id = UUID.randomUUID().toString();
    }*/
}
