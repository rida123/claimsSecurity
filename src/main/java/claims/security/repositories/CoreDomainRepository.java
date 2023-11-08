package claims.security.repositories;


import claims.security.entities.CoreDomain;

import java.util.List;

public interface CoreDomainRepository extends BaseRepository<CoreDomain, String> {

    List<CoreDomain> findCoreDomainsByCodeContainingIgnoreCase(String codeSubstring);

    List<CoreDomain> findCoreDomainsByDescriptionContainingIgnoreCase(String descriptionSubstring);

    List<CoreDomain> findCoreDomainsByCodeContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String code, String description);

}
