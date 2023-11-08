package claims.security.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserInfo {
    private String displayName ;
    private String userName ;
    private String email ;
    private String userEmailSignature;
    private BigDecimal userLimitDoctorFees;
    private BigDecimal userLimitTaxiFees;
    private BigDecimal userLimitSurveyFees;
    private BigDecimal userLimitExceedPercentage;
    private BigDecimal userLimitLawyerFees;
    private String companyId ;
    private String branchId ;
    private BigDecimal recoverLimit;
    private BigDecimal userLimitHospitalFees;
    private BigDecimal paymentLimit;
    private BigDecimal userLimitExpertFees ;
    private int active ;
    private byte[] userPicture;

    private String activeDesc;
    private String companyDescription ;


    private String sysCreatedBy;
    private LocalDateTime sysCreatedDate;
    private String sysUpdatedBy;
    private LocalDateTime sysUpdatedDate;

}
