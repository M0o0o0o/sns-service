package mooyeol.snsservice.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.exception.ErrorResponse;
import mooyeol.snsservice.service.PostService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;

    @GetMapping("/post/{id}")
    public ResponseEntity<Object> findPost(@PathVariable Long id) {
        Optional<Post> optionalPost = postService.findPost(id);
        if(optionalPost.isEmpty())
            return new ResponseEntity<>(new ErrorResponse("존재하지 않는 게시글입니다."), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new PostGetDto(optionalPost.get()), HttpStatus.OK);
    }


    @PostMapping("/post")
    public ResponseEntity<Object> addPost(@ModelAttribute @Validated PostAddDto postDto, BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        List<String> hashTags = postDto.getHashTags() != null ? getHashTagsList(postDto.getHashTags(), bindingResult) : null;

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        Optional<Post> createdPost = postService.addPost(post, hashTags);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/post/" + createdPost.get().getId());
        return new ResponseEntity<>("게시글이 생성되었습니다.", headers, HttpStatus.CREATED);
    }



    @PatchMapping("/post/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable Long id, @ModelAttribute PostUpdateDto postDto, BindingResult bindingResult) throws BindException {
        List<String> hashTags = postDto.getHashTags() != null ? getHashTagsList(postDto.getHashTags(), bindingResult) : null;

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Post post = postService.updatePost(id, postDto, hashTags);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/post/" + post.getId());

        return new ResponseEntity<>("게시글이 수정되었습니다.", headers, HttpStatus.OK);
    }


    // 우선 게시글이 존재하지 않는 경우도 확인을 해봐야 한다.
    @DeleteMapping("/post/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
        try {
           postService.deletePost(id);
            return new ResponseEntity<>(new ErrorResponse("게시글이 삭제되었습니다."), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse("존재하지 않는 게시글입니다."), HttpStatus.NOT_FOUND);
        }
    }

    private List<String> getHashTagsList(String hashTags, BindingResult bindingResult) {

        List<String> result = Arrays.stream(hashTags.split(",")).collect(Collectors.toList());

        if (result.size() > 20 || result.size() < 1) {
            bindingResult.rejectValue("hashTags", "range", new Object[]{"1", "20"}, "해시태그는 1~20개 까지만 입력 가능합니다.");
        }

        String pattern = "#([0-9a-zA-Z가-힣]*)";
        for (String hashTag : result) {
            if (!Pattern.matches(pattern, hashTag)) {
                bindingResult.rejectValue("hashTags", "pattern", "해시태그는 #으로 시작해야 합니다.");
                break;
            }
        }

        return result;
    }
}
