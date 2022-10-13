package mooyeol.snsservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mooyeol.snsservice.repository.TimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter @ToString
public class Post extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private long id;

    @Column(name = "post_title")
    private String title;

    @Column(name = "post_content")
    private String content;

    @Column(name = "post_hearts", columnDefinition = "integer default 0")
    private long hearts;

    @Column(name = "post_views", columnDefinition = "integer default 0")
    private long views;

    @OneToMany(mappedBy = "post")
    private List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostTag> postTags = new ArrayList<>();
}
