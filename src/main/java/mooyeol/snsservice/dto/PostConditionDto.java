package mooyeol.snsservice.dto;


import lombok.Data;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * order regex 적용
 */
@Data
public class PostConditionDto {
    @Pattern(regexp = "^(createdDate|hearts|views)$", message = "date, heart, view 중 하나를 입력해주세요.")
    private String order;

    @Pattern(regexp = "^(true|false)$", message = "true 또는 false만 입력해주세요.")
    @NotNull
    public String desc;

    private String search;

    private String hashTags;

    @NotNull(message = "{NotEmpty.PostCondition.page}")
    @Positive(message = "{Positive.PostCondition.page}")
    private int page = 0;

    @Positive
    @Min(value = 15, message = "{Min.PostCondition.page}")
    @Max(value = 30, message = "{Max.PostCondition.page}")
    private int cnt = 15;

    private List<String> listHashTags = null;
}
