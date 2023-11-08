package claims.security.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "authorities")
public class AppAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "name")
    private String authority_name;

    @ManyToMany(mappedBy = "authorities")
    private Set<AppUser> users;
}
