package mooyeol.snsservice.controller;


import lombok.Data;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 domain에 생성일 추가해야 한다.
 * 좋아요, 조회수에 따른 검색도 추후에 구현해야 한다.
 */
@Data
public class PostConditionDto {
    private String order = "createdDate";

    @AssertFalse(message = "{AssertFalse.PostCondition.reverse}")
    public boolean reverse = false;

    private String search;

    private String hashTags;

    @NotNull(message = "{NotEmpty.PostCondition.page}")
    @Positive(message = "{Positive.PostCondition.page}")
    private int page;

    @Positive
    @Min(value = 15, message = "{Min.PostCondition.page}")
    @Max(value = 30, message = "{Max.PostCondition.page}")
    private int cnt = 15;

    private List<String> listHashTags = null;

    public void setListHashTags() {
        if(this.hashTags == null) return;
        String[] split = hashTags.split(",");

        listHashTags = new ArrayList<>();
        for (String s : split) {
            listHashTags.add("#" + s);
        }
    }
}
