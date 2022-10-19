package mooyeol.snsservice.controller;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListDto {
    private long id;
    private String title;
    private String content;
    private String writer;
    private long hearts;
    private long views;
    private LocalDateTime createdDate;
}
