package mooyeol.snsservice.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostListDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Long hearts;
    private Long views;
    private LocalDateTime createdDate;


    @QueryProjection
    public PostListDto(Long id, String title, String content, String writer, Long hearts, Long views, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.hearts = hearts;
        this.views = views;
        this.createdDate = createdDate;
    }
}
