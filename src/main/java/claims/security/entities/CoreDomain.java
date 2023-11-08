package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "CORE_DOMAIN")
public class CoreDomain extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 5316756408303106300L;

    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(nullable = false, length = 128)
    private String code;
    
    @Column(nullable = false, length = 128)
    private String description;

    @Column(length = 128)
    private String label1;
    
    @Column(length = 128)
    private String label10;
    
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
    
    @Column(name = "PREFERENCE_CODE", nullable = false, length = 128)
    private String preferenceCode;
    
    @Column(length = 3000)
    private String regex1;
    
    @Column(length = 256)
    private String regex10;
    
    @Column(length = 256)
    private String regex2;
    
    @Column(length = 256)
    private String regex3;
    
    @Column(length = 256)
    private String regex4;
    
    @Column(length = 256)
    private String regex5;
    
    @Column(length = 256)
    private String regex6;
    
    @Column(length = 256)
    private String regex7;
    
    @Column(length = 256)
    private String regex8;
    
    @Column(length = 256)
    private String regex9;
    
    @Column(name = "SYS_ACTIVE_FLAG", nullable = false)
    private Integer sysActiveFlag;

    @OneToMany(mappedBy = "coreDomain", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CoreDomainMl> coreDomainMlList;
    
    @OneToMany(mappedBy = "coreDomain", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CoreDomainValue> coreDomainValueList;


 /*   public CoreDomain() {
    	this.id = UUID.randomUUID().toString();
    }    
 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
