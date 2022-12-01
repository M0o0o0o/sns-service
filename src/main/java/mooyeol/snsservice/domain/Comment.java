package mooyeol.snsservice.domain;

import lombok.Getter;
import lombok.Setter;
import mooyeol.snsservice.repository.TimeEntity;

import javax.persistence.*;

@Entity @Getter @Setter
public class Comment extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

    @Column(name = "content", length = 200, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

}
