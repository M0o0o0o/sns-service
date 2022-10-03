package mooyeol.snsservice.controller;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberDto {

    @NotBlank(message = "{NotBlank.memberDto.email}")
    @Email(message = "{Email}")
    private String email;

    @NotBlank(message = "{NotBlank.memberDto.password}")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "{Pattern.memberDto.password}")
    private String password;
}


