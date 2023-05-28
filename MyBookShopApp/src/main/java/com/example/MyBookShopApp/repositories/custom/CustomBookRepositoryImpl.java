package com.example.MyBookShopApp.repositories.custom;

import com.example.MyBookShopApp.dto.review.ReviewDto;
import com.example.MyBookShopApp.dto.review.SmallReviewDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.book.RatingEntity;
import com.example.MyBookShopApp.model.book.links.Book2RatingEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewEntity;
import com.example.MyBookShopApp.model.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.model.user.UserEntity;
import com.example.MyBookShopApp.repositories.Book2UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Slf4j
public class CustomBookRepositoryImpl implements CustomBookRepository {
    @PersistenceContext
    private final EntityManager em;
    private final Book2UserRepository book2UserRepository;


    @Override
    public Map<String, Integer> countRatingsByStar(Integer bookId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<BookEntity> root = cq.from(BookEntity.class);
        Join<BookEntity, Book2RatingEntity> br = root.join("linksToRatings", JoinType.INNER);
        Join<Book2RatingEntity, RatingEntity> r = br.join("rating", JoinType.INNER);
        Map<String, Integer> starValues = new HashMap<>();
        Predicate idPredicate = cb.equal(root.get("id"), bookId);
        for (int i = 1; i <= 5; i++) {
            Predicate valuePredicate = cb.equal(r.get("value"), i);
            cq
                    .select(cb.count(r.get("value")))
                    .where(cb.and(idPredicate, valuePredicate));
            Integer result = (em.createQuery(cq).getSingleResult()).intValue();
            starValues.put("star" + i, result);
        }
        return starValues;
    }

    @Override
    public List<ReviewDto> getReviewsById(Integer bookId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SmallReviewDto> cq = cb.createQuery(SmallReviewDto.class);

        Root<BookReviewEntity> r = cq.from(BookReviewEntity.class);
        Join<BookReviewEntity, UserEntity> u = r.join("user");
        Join<BookReviewEntity, BookEntity> b = r.join("book");
        Predicate idPredicate = cb.equal(b.get("id"), bookId);
        cq.multiselect(r.get("id"), u.get("name"), r.get("time"), r.get("text")).where(idPredicate);
        // get list of reviews
        List<SmallReviewDto> resultList = em.createQuery(cq).getResultList();

        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for (SmallReviewDto smallReviewDto : resultList) {
            Integer likeCount = getLikeCount(smallReviewDto.getId(), 1);
            Integer dislikeCount = getLikeCount(smallReviewDto.getId(), -1);
            Integer ratingVal = getRatingValue(smallReviewDto.getId());
            reviewDtoList.add(new ReviewDto() {{
                setReview(smallReviewDto);
                setLikeCount(likeCount);
                setDislikeCount(dislikeCount);
                setRating(ratingVal);
            }});

        }
        return reviewDtoList;
    }


    private Integer getRatingValue(Integer reviewId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
        Root<RatingEntity> r = cq.from(RatingEntity.class);
        Join<RatingEntity, UserEntity> u = r.join("user");
        Join<UserEntity, BookReviewEntity> br = u.join("reviews");
        Predicate reviewIdPred = cb.equal(br.get("id"), reviewId);
        cq.select(r.get("value")).where(reviewIdPred);
        Integer result = 0;
        try {
            result = em.createQuery(cq).setMaxResults(1).getSingleResult();

        } catch (NoResultException e) {
            log.info(e.getMessage());
        }

        return result;
    }


    private Integer getLikeCount(Integer reviewId, int value) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<BookReviewLikeEntity> rl = cq.from(BookReviewLikeEntity.class);
        Join<BookReviewLikeEntity, BookReviewEntity> r = rl.join("review");
        Predicate idPredicate = cb.equal(r.get("id"), reviewId);
        Predicate valuePredicate = cb.equal(rl.get("value"), value);
        cq.select(cb.count(rl.get("value"))).where(cb.and(idPredicate, valuePredicate));
        return em.createQuery(cq).getSingleResult().intValue();
    }

}
