package claims.security.repositories;

import claims.security.entities.CoreDocumentFile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreDocumentFileRepository extends BaseRepository<CoreDocumentFile,String> {


    List<CoreDocumentFile> findCoreDocumentFilesByFileNameContainingIgnoreCase(String fileNameSubstring);

    List<CoreDocumentFile> findCoreDocumentFilesByPathContainingIgnoreCase(String pathSubstring);

    List<CoreDocumentFile> findCoreDocumentFilesByFileNameContainingIgnoreCaseAndPathContainingIgnoreCase(String nameSubstring, String pathSubsring);

}
