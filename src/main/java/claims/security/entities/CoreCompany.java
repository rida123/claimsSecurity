package claims.security.entities;


import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "CORE_COMPANY")
public class CoreCompany extends BaseEntity implements Serializable {
	
    private static final long serialVersionUID = 9218479471091972349L;
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "LEGAL_NAME", length = 200)
    private String legalName;

    @Column(nullable = false, length = 36)
    private String name;

    @Column(name = "CLIENT_ID", length = 36)
    private String clientId;

    @Column(name = "REGISTER_INFO", length = 200)
    private String registerInfo;

    @Column(name = "ADDRESS_INFO", length = 200)
    private String addressInfo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "core_company_profile",
            joinColumns = @JoinColumn(name = "core_company_id"),
            inverseJoinColumns = @JoinColumn(name = "core_profile_id")
    )
    private List<CoreProfile> companyProfiles;
    //todo: old code
/*    @OneToMany(mappedBy = "coreCompany", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CoreCompanyProfile> coreCompanyProfileList;*/
    

    public CoreCompany() {

    }
    
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
   
}
