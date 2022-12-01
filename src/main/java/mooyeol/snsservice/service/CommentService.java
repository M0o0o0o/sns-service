package mooyeol.snsservice.service;

import lombok.RequiredArgsConstructor;
import mooyeol.snsservice.domain.Comment;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.repository.CommentRepository;
import mooyeol.snsservice.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void addpost(Member member, long postId, String content) {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isEmpty()) {
            // 게시글 찾을 수 없는 경우 처리
        }

        Comment comment = getComment(member, post.get(), content);
        commentRepository.save(comment);
    }

    private Comment getComment(Member member, Post post, String content) {
        Comment comment = new Comment();
        comment.setMember(member);
        comment.setPost(post);
        comment.setContent(content);
        return comment;
    }
}
