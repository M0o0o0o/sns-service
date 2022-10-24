package mooyeol.snsservice.controller;

import lombok.*;
import mooyeol.snsservice.domain.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class MemberDto {

    @NotNull
    @Email(message = "{Email}")
    private String email;

    @NotBlank(message = "{NotBlank.memberDto.password}")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "{Pattern.memberDto.password}")
    private String password;

    public MemberDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member toEntity() {
        Member member = new Member();
        member.setEmail(this.email);
        member.setPassword(this.password);
        return member;
    }

}


