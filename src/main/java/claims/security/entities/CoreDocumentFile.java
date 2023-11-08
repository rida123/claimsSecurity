package claims.security.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Data
@Entity
@Table(name = "CORE_DOCUMENT_FILE")
public class CoreDocumentFile extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -4677067274716571428L;

    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(length = 20)
    private String cmp;

    @Lob
    @Column(name="CONTENT ")
    private byte[] content;

    @Column(name = "CONTENT_TYPE", nullable = false, length = 512)
    private String contentType;

    @Column(name = "FILE_NAME", length = 120)
    private String fileName;

    @Column(name = "TABLE_NAME", length = 120)
    private String tableName;

    @Column(name="PATH",length = 512)
    private String path;

//    public CoreDocumentFile() {
//        this.id = UUID.randomUUID().toString();
//    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
