package mooyeol.snsservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private long id;

    @Column(name = "member_email")
    String email;

    @Column(name = "member_pwd")
    String password;

    @OneToMany(mappedBy = "member")
    List<Heart> hearts = new ArrayList<>();
}
