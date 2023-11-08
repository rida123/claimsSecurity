package claims.security.entities;


import lombok.AllArgsConstructor;
import lombok.Data;

//@Entity
//@Table(name = "authorities")
@Data
@AllArgsConstructor
public class Next2Authority {
//    @Id
 //   @GeneratedValue(strategy = GenerationType.AUTO)

    private String id; //it was Long
    private String name;
    private String description;
    private String profile;

    /*public String getAuthorityName() {
        return id;
    }*/

}
