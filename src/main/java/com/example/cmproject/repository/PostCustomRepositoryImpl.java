package com.example.cmproject.repository;

import com.example.cmproject.entity.Post;
import com.example.cmproject.entity.SearchType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.cmproject.entity.QPost.post;
@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository{
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Post> searchByKeywordAndType(Pageable pageable, String keyword, SearchType type) {
        BooleanBuilder predicate = new BooleanBuilder();

        switch (type) {
            case TITLE:
                predicate.and(post.title.containsIgnoreCase(keyword));
                break;
            case USER:
                predicate.and(post.user.nickName.containsIgnoreCase(keyword));
                break;
            case CONTENT:
                predicate.and(post.content.containsIgnoreCase(keyword));
                break;
            case TITLE_CONTENT:
                predicate.and(post.title.containsIgnoreCase(keyword)
                        .or(post.content.containsIgnoreCase(keyword)));
                break;
            case TITLE_CONTENT_USER:
                predicate.and(post.title.containsIgnoreCase(keyword)
                        .or(post.content.containsIgnoreCase(keyword))
                        .or(post.user.nickName.containsIgnoreCase(keyword)));
                break;
        }

        JPAQuery<Post> query = queryFactory.selectFrom(post)
                .where(predicate)
                .groupBy(post.postId);

        List<Post> postList = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory.selectFrom(post)
                .where(predicate);

        long total = countQuery.fetchCount();

        return new PageImpl<>(postList, pageable, total);
    }
}
