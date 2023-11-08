
package claims.security.repositories;

import claims.security.entities.CoreDomain;
import claims.security.entities.CoreDomainValue;
import claims.security.http.response.CoreDomainValueDTO;
import claims.security.http.response.CoreDomainValueProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CoreDomainValueRepository extends BaseRepository<CoreDomainValue, String> {

  /*  @Query(" select dv.code as COVER_CODE, dv.description as COVER_DESCRIPTION from CoreDomainValue dv where dv.coreDomain.id = 'COVER_TYPE' ")
    List<CoverTypesResponse> findAllCoverTypesResponse();

    @Query(" select dv.code as COVER_CODE, dv.description as COVER_DESCRIPTION from CoreDomainValue dv where dv.coreDomain.id = 'PRODUCT.TYPE' ")
    List<ProductTypesResponse> findAllProductTypesResponse();

    @Query(" select dv.code as COVER_CODE, dv.description as COVER_DESCRIPTION from CoreDomainValue dv where dv.coreDomain.id = 'COVER_TYPE' and dv.code= ?1 ")
    Optional<CoverTypesResponse> findCoverTypesResponseByCode(String code);*/

    List<CoreDomainValue> findCoreDomainValueByCoreDomain(CoreDomain coreDomain);

    @Query(value = "SELECT VAL1, VAL2, VAL3 FROM CORE_DOMAIN_VALUE WHERE CORE_DOMAIN_ID = ?1 AND CODE = ?2 AND VAL4 = ?3 AND NVL(VAL5,'N') = 'Y'", nativeQuery = true)
    List<CoreDomainValueProjection> getPolicyNotiRequiresFieldsByCmp(String fromPage, String cmp, String nature);

    @Query(value = "SELECT VAL1, VAL2, VAL3 FROM CORE_DOMAIN_VALUE WHERE CORE_DOMAIN_ID = 'NO_DATA_REQ' AND CODE = ?1 AND NVL(VAL5, 'N') = 'Y'", nativeQuery = true)
    List<CoreDomainValueProjection> getNoDataNotiRequiredByCmp(String company);

    @Query(value = "select o from CoreDomainValue o where o.coreDomain.id = 'RESP'")
    List<CoreDomainValueDTO> getAllCarRespReasonCode();



    List<CoreDomainValue> getCoreDomainValuesByCoreDomainAndCodeContainingIgnoreCase(CoreDomain domain, String code);

    List<CoreDomainValue> getCoreDomainValuesByCoreDomainAndDescriptionContainingIgnoreCase(CoreDomain domain, String descriptionSubstring);

    List<CoreDomainValue> getCoreDomainValuesByCoreDomainAndCodeContainingIgnoreCaseAndDescriptionContainingIgnoreCase(CoreDomain domain, String code, String description);


    @Query("select e from CoreDomainValue e where e.coreDomain.id = 'SEVERITY'")
    List<CoreDomainValueDTO> getAllSeverityTypes();
    Optional<CoreDomainValue> findByCode(String default_password);


    @Query(value = "SELECT CODE FROM CORE_DOMAIN_VALUE WHERE CORE_DOMAIN_ID = :domainId AND VAL1 = :cmp", nativeQuery = true)
    List<String> findEmailsByDomainAndCmp(String domainId, String cmp);

    @Query(value = "select o from CoreDomainValue o where o.coreDomain.id='app.locale'")
    List<CoreDomainValue> findLocaleList();
    @Query(value = "select o from CoreDomainValue o where o.coreDomain.id='app.css'")
    List<CoreDomainValue> findCssList();
    @Query(value = "SELECT val1, val2, val3 FROM CORE_DOMAIN_VALUE WHERE CORE_DOMAIN_ID = 'PRE_RISK_SUR_REQ' and CODE = :cmp and NVL(VAL4,'N') = 'Y'",
            nativeQuery = true)
    List<CoreDomainValueProjection> getPreRiskSurveyRequiredFields(@Param("cmp") String cmp);

}
