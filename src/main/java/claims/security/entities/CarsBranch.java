package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Data
@Entity
@Table(name = "CARS_BRANCH")
public class CarsBranch extends BaseEntity implements Serializable {


    @Id
    @Column(name = "BRANCH_ID", nullable = false, length = 36)
    private String id;

    @Column(name = "BRANCH_ADR_1", length = 40)
    private String address1;
 
    @Column(name = "BRANCH_ADR_2", length = 40)
    private String address2;
    
    @Column(name = "BRANCH_CODE", nullable = false)
    private String code;
    
    @Column(name = "BRANCH_DES", length = 400)
    private String description;
    
    @Column(name = "BRANCH_DES_ARA", length = 400)
    private String arabic_description;
    
  /*  @OneToMany(orphanRemoval = true, mappedBy = "carsBranch", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CarsFilesSent> carsFilesSentList;*/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BRANCH_INSURANCE_ID")
    private CarsInsurance carsInsurance;

  /*  @OneToMany(orphanRemoval = true, mappedBy = "carsBranch", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CarsLossCar> carsLossCarList;*/

 /*   @OneToMany(orphanRemoval = true, mappedBy = "carsBranch", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CarsLossTowing> carsLossTowingList;
    @OneToMany(orphanRemoval = true, mappedBy = "carsBranch", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CarsInvoiceRecp> carsInvoiceRecpList;
    @OneToMany(orphanRemoval = true, mappedBy = "carsBranch", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CarsPolicy> carsPolicyList;
    @OneToMany(orphanRemoval = true, mappedBy = "carsBranch", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CarsResPayInfo> carsResPayInfoList;
    @OneToMany(orphanRemoval = true, mappedBy = "carsBranch", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CarsResPay> carsResPayList;
    @OneToMany(orphanRemoval = true, mappedBy = "carBranch", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<CarsBlackList> carsBlackListList;
 */
    public CarsBranch() {

    }
    
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
