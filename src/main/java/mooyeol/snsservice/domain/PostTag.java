package mooyeol.snsservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_tag_id")
    private long id;

    @Column(name = "post_tag_name")
    private String postTagName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

}
