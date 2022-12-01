package mooyeol.snsservice.controller;

import lombok.RequiredArgsConstructor;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 댓글 등록, 수정, 삭제
 */
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // post_id, content,
    @PostMapping("/comment/{postId}")
    public String addPost(@AuthenticationPrincipal Object principal,
                          @PathVariable long postId, @RequestBody String content,
                          BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        commentService.addpost((Member) principal, postId, content);

    }
}
