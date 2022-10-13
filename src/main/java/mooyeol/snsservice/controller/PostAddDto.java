package mooyeol.snsservice.controller;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PostAddDto {

    @NotBlank(message = "{NotBlank.postAddDto.title}")
    @Size(max=255, message = "{Size.postAddDto.title}")
    private String title;

    @NotBlank(message = "{NotBlank.postAddDto.content}")
    @Size(max=255, message = "{Size.postAddDto.content}")
    private String content;

    private String hashTags;
}
