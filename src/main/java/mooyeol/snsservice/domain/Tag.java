package mooyeol.snsservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity @Setter @Getter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    long id;

    @Column(name = "tag_name")
    String name;
}
