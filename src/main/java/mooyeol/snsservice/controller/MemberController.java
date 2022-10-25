package mooyeol.snsservice.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        return new ErrorResult("email", e.getMessage());
    }

    @PostMapping("/member")
    public ResponseEntity<Object> join(@Validated @RequestBody  MemberDto memberDto, BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Member member = memberDto.toEntity();
        Member savedMember = memberService.join(member);

        return new ResponseEntity<>("회원가입을 축하합니다.", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/member/logout")
    public ResponseEntity<Object> lgoout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);

    }

    @Data
    @AllArgsConstructor
    static class ErrorResult {
        private String field;
        private String message;
    }
}
