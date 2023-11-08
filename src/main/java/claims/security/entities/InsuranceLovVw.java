package claims.security.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "INSURANCE_LOV_VW")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class InsuranceLovVw implements Serializable {
    private static final long serialVersionUID = 8378658864610020343L;
    @Id
    @Column(name = "INSURANCE_CODE", nullable = false)
    private BigDecimal insuranceCode;
    @Column(name = "INSURANCE_DESC", length = 75)
    private String insuranceDesc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InsuranceLovVw that = (InsuranceLovVw) o;
        return insuranceCode != null && Objects.equals(insuranceCode, that.insuranceCode);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
