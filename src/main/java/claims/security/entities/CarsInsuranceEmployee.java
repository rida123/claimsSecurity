package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


@Data
@Entity
@Table(name = "CARS_INSURANCE_EMPLOYEE")
public class CarsInsuranceEmployee extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6669762397109160465L;

    @Id
    @Column(name = "INSURANCE_EMPLOYEE_ID", nullable = false, length = 36)
    private String insuranceEmployeeId;

    @Column(name = "USER_LIMIT_EXPERT_FEES")
    private BigDecimal userLimitExpertFees;

    @Column(name = "USERS_ABREV", length = 50)
    private String usersAbrev;

    @Column(name = "USERS_BRANCH")
    private BigDecimal usersBranch;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USERS_BRANCH_ID" )
    private CarsBranch carsBranch;

    @Column(name = "USERS_CODE", length = 50)
    private String usersCode;

    @Column(name = "USERS_INSURANCE")
    private BigDecimal usersInsurance;

    @Column(name = "USERS_LIMIT")
    private BigDecimal usersLimit;

    @Column(name = "USERS_NEED_FINAL_APPROVAL", length = 36)
    private String usersNeedFinalApproval;

    @Column(name = "USERS_STATE", length = 2)
    private String usersState;

    @Column(name = "USER_LIMIT_DOCTOR_FEES")
    private BigDecimal userLimitDoctorFees;

    @Column(name = "USER_LIMIT_HOSPITAL_FEES")
    private BigDecimal userLimitHospitalFees;

    @Column(name = "USER_LIMIT_TAXI_FEES")
    private BigDecimal userLimitTaxiFees;

    @Column(name = "USER_LIMIT_SURVEY_FEES")
    private BigDecimal userLimitSurveyFees;

    @Column(name = "USER_LIMIT_EXCEED_PERCENTAGE")
    private BigDecimal userLimitExceedPercentage;

    @Column(name = "USER_LIMIT_RECOVERY")
    private BigDecimal userLimitRecovery;


    @Column(name = "USER_LIMIT_LAWYER_FEES")
    private BigDecimal userLimitLawyerFees;

    public CarsInsuranceEmployee() {
        this.insuranceEmployeeId = UUID.randomUUID().toString();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}

