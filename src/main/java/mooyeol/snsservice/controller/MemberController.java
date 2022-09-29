package mooyeol.snsservice.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        return new ErrorResult("email", e.getMessage());
    }

    @PostMapping("/member")
    @ResponseStatus(HttpStatus.CREATED)
    public String join(@Validated @ModelAttribute MemberDto memberDto, BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Member member = new Member();
        member.setEmail(memberDto.getEmail());
        member.setPassword(memberDto.getPassword());
        memberService.join(member);

        return "ok";
    }


    @Data
    @AllArgsConstructor
    static class ErrorResult {
        private String field;
        private String message;
    }
}
