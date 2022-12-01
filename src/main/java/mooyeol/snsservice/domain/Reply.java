package mooyeol.snsservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mooyeol.snsservice.repository.TimeEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Reply extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private long id;

    @Column(length = 200, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
