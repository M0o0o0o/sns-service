package mooyeol.snsservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import mooyeol.snsservice.dto.PostConditionDto;
import mooyeol.snsservice.dto.PostListDto;
import mooyeol.snsservice.dto.QPostListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import java.util.List;

import static mooyeol.snsservice.domain.QMember.member;
import static mooyeol.snsservice.domain.QPost.post;
import static mooyeol.snsservice.domain.QPostTag.postTag;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostListDto> findPosts(Pageable pageable, PostConditionDto condition) {
        List<PostListDto> posts = queryFactory
                .select(
                        new QPostListDto(post.id, post.title, post.content, post.member.email, post.hearts,
                                post.views, post.createdDate))
                .from(post)
                .join(post.postTags, postTag)
                .join(post.member, member)
                .where(
                        post.id.eq(postTag.post.id),
                        post.member.id.eq(member.id),
                        titleEq(condition.getSearch()),
                        hashTagsEq(condition.getListHashTags())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .join(post.postTags, postTag)
                .join(post.member, member)
                .where(
                        post.id.eq(postTag.post.id),
                        post.member.id.eq(member.id),
                        titleEq(condition.getSearch()),
                        hashTagsEq(condition.getListHashTags())
                );

        return PageableExecutionUtils.getPage(posts, pageable, countQuery::fetchOne);
    }


    private BooleanExpression hashTagsEq(List<String> listHashTags) {
        BooleanExpression booleanExpression = null;

        for (String hashTag : listHashTags) {
            if (booleanExpression == null) {
                booleanExpression = postTag.postTagName.eq(hashTag);
                continue;
            }
            booleanExpression.and(postTag.postTagName.eq(hashTag));
        }

        return booleanExpression;
    }

    private BooleanExpression titleEq(String search) {
        return hasText(search) ? post.title.contains(search) : null;
    }
}
