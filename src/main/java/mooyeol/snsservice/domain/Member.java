package mooyeol.snsservice.domain;

import lombok.Getter;
import lombok.Setter;
import mooyeol.snsservice.repository.TimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private long id;

    @Column(name = "member_email", unique = true, length = 100, nullable = false)
    String email;

    @Column(name = "member_pwd", length = 20, nullable = false)
    String password;

    @OneToMany(mappedBy = "member")
    List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<Reply> replies = new ArrayList<>();
}

