package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.dto.book.BookSlugs;
import com.example.MyBookShopApp.dto.book.FullBookDto;
import com.example.MyBookShopApp.dto.book.RatingDto;
import com.example.MyBookShopApp.dto.book.ShortBookDtoProjection;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.model.enums.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    String SHORT_BOOK_SELECT_CLAUSE =
            "select         slug, " +
                    "       image, " +
                    "       (case when length(title) > 37 " +
                    " then concat((substring(title, 1, 37)), '...') " +
                    " else title end) title, " +
                    "       discount, " +
                    "       isBestseller, " +
                    "       price, " +
                    "       discountPrice, " +
                    "       (case " +
                    "            when array_length(authors_array, 1) > 1 " +
                    "            then concat(authors_array[1], ' и прочие') " +
                    "            else array_to_string(authors_array, '') end) authors ";
    String SHORT_BOOK_FROM_SUBQUERY_SELECT_CLAUSE =
            " from (select      array(select a.name from books b_a  " +
                    "                  join book2author b2a on b2a.book_id = b_a.id " +
                    "                  join authors a on b2a.author_id = a.id " +
                    "                  where b_a.id=b.id " +
                    "                  order by b2a.sort_index)                           authors_array, " +
                    "             b.slug                                                   slug, " +
                    "             b.image                                                  image, " +
                    "             b.title                                                  title, " +
                    "             b.discount                                               discount, " +
                    "             cast(b.is_bestseller = 1 as bool)                        isBestseller, " +
                    "             b.price                                                  price, " +
                    "             round(price * (1 - cast(b.discount as float)/100))       discountPrice ";


    String SHORT_BOOK_WHERE_SLUG_NOT_IN_CLAUSE =
            " where case " +
                    "       when coalesce(:slugs) is not null " +
                    "       then slug not in (:slugs) " +
                    "       else slug not like '' end ";

    Optional<BookEntity> findBySlug(String slug);

    @Query(value = "select * from books b " +
            "where b.slug in :slug " +
            "and b.id not in (select bu.book_id from book2user bu " +
            "                   join users u on u.id = bu.user_id " +
            "                   join book2user_type but on but.id = bu.type_id" +
            "                   where u.email like :email" +
            "                   and (but.code like 'PAID' or but.code like 'ARCHIVE'))", nativeQuery = true)
    List<BookEntity> findKeptOrCartBooksBySlugIn(@Param("slug") List<String> slug, @Param("email") String email);

    List<BookEntity> findBookEntitiesBySlugIn(List<String> bookIds);

    @Query(value = "select slug,    " +
            "       image,    " +
            "       title,    " +
            "       discount,    " +
            "       isBestseller,    " +
            "       price,    " +
            "       discountPrice,    " +
            "       description,    " +
            "       (case when rating is null then 0 else rating end)  rating  " +
            "from (select b.slug                                              slug,    " +
            "             b.image                                             image,    " +
            "             b.title                                             title,    " +
            "             b.discount                                          discount,    " +
            "             cast(b.is_bestseller = 1 as bool)                   isBestseller,    " +
            "             b.price                                             price,    " +
            "             (cast(b.price as float) / 100 * (100 - b.discount)) discountPrice,    " +
            "             b.description                                       description,    " +
            "             round(cast(sum(r.value) as float) / count(r.id))    rating    " +
            "      from books b    " +
            "               left join ratings r on r.book_id = b.id    " +
            "      group by b.slug, b.image, b.title, b.discount, b.is_bestseller,    " +
            "               b.price, b.description, b.discount, b.price    " +
            "      having b.slug = :slug) book", nativeQuery = true)
    FullBookDto getFullBookInfo(
            @Param("slug") String bookSlug
    );

    @Query(value = "select sum(case when r.value = 1 then 1 else 0 end) count1, " +
            "       sum(case when r.value = 2 then 1 else 0 end) count2, " +
            "       sum(case when r.value = 3 then 1 else 0 end) count3, " +
            "       sum(case when r.value = 4 then 1 else 0 end) count4, " +
            "       sum(case when r.value = 5 then 1 else 0 end) count5, " +
            "       count(r) totalCount " +
            "from books b " +
            "         join ratings r on r.book_id = b.id " +
            "where b.slug = :slug",
            nativeQuery = true)
    RatingDto getBookRatingsCountBySlug(
            @Param("slug") String slug
    );

    @Query(value = SHORT_BOOK_SELECT_CLAUSE +
            SHORT_BOOK_FROM_SUBQUERY_SELECT_CLAUSE + ", " +
            " b.pub_date , " +
            " sum(r.value) rating_value, " +
            " (select tag.c + author.c + genre.c " +
            "   from  " +
            "    (select (case when array_agg(b2t.tag_id) && array_agg(b2t2.id) " +
            "                  then 1 else 0 end) c from book2tag b2t2 " +
            "            join  books b3 on b3.id = b2t2.book_id " +
            "            where b3.slug in :slugs) tag, " +
            "    (select (case when array_agg(b2a.author_id) && array_agg(b2a2.id) " +
            "                  then 1 else 0 end) c  from book2author b2a2 " +
            "            join  books b3 on b3.id = b2a2.book_id " +
            "            where b3.slug in :slugs) author," +
            "    (select (case when array_agg(b2g.genre_id) && array_agg(b2g2.id) " +
            "                  then 1 else 0 end) c  from book2genre b2g2 " +
            "            join  books b3 on b3.id = b2g2.book_id " +
            "            where b3.slug in :slugs) genre) match_value" +
            "      from books b " +
            "      join book2author b2a on b2a.book_id = b.id " +
            "      join authors a on b2a.author_id = a.id " +
            "      left join ratings r on b.id = r.book_id " +
            "      left join book2tag b2t on b2t.book_id = b.id " +
            "      left join book2genre b2g on b2g.book_id = b.id " +
            "      group by b.id " +
            ")  as recommended_books  " +
            SHORT_BOOK_WHERE_SLUG_NOT_IN_CLAUSE +
            " order by match_value desc, rating_value desc nulls last, pub_date desc",
            nativeQuery = true,
            countQuery = "select count(*) from books")
    Page<ShortBookDtoProjection> getRecommendedBooks(
            @Param("slugs") List<String> slugsToExclude,
            Pageable nextPage
    );

    @Query(value = SHORT_BOOK_SELECT_CLAUSE +
            SHORT_BOOK_FROM_SUBQUERY_SELECT_CLAUSE + ", " +
            " b.pub_date pub_date" +
            "      from books b " +
            "      group by b.id  " +
            "      having  b.pub_date between :from and :to   " +
            "      ) as recent_books " +
            SHORT_BOOK_WHERE_SLUG_NOT_IN_CLAUSE +
            " order by pub_date desc",
            nativeQuery = true,
            countQuery = "select count(*) from books")
    Page<ShortBookDtoProjection> getRecentBooksByPubDate(
            @Param("from") LocalDate dateFrom,
            @Param("to") LocalDate now,
            @Param("slugs") List<String> slugsToExclude, Pageable nextPage
    );

    @Query(value = SHORT_BOOK_SELECT_CLAUSE +
            SHORT_BOOK_FROM_SUBQUERY_SELECT_CLAUSE + ", " +
            "(sum(case when b2ut.code like 'PAID' then 1 else 0 end) + 0.7 * " +
            "sum(case when b2ut.code like 'CART' then 1 else 0 end)  + 0.4 * " +
            "sum(case when b2ut.code like 'KEPT' then 1 else 0 end)) sorting_value," +
            " b.id b_id" +
            "      from books b " +
            "               left join book2user b2u on b.id = b2u.book_id  " +
            "               left join book2user_type b2ut on b2u.type_id = b2ut.id  " +
            "      group by b.id ) as popular_books  " +
            SHORT_BOOK_WHERE_SLUG_NOT_IN_CLAUSE +
            " order by sorting_value desc, b_id",
            nativeQuery = true,
            countQuery = "select count(*) from books")
    Page<ShortBookDtoProjection> getPopularBooks(
            @Param("slugs") List<String> slugsToExclude,
            Pageable nextPage
    );


    @Query(value = SHORT_BOOK_SELECT_CLAUSE +
            SHORT_BOOK_FROM_SUBQUERY_SELECT_CLAUSE +
            "      from books b " +
            "      group by b.id " +
            "      having b.id in (select b2.id " +
            "                      from books b2 " +
            "                      where upper(b2.title) like upper(concat('%', :word, '%'))))" +
            " as searched_books",
            nativeQuery = true,
            countQuery = "select count(*)  " +
                    "            from books b2  " +
                    "            where upper(b2.title) like upper(concat('%', :word, '%'))")
    Page<ShortBookDtoProjection> findBooksByTitleContaining(
            @Param("word") String searchWord,
            Pageable nextPage
    );

    @Query(value = SHORT_BOOK_SELECT_CLAUSE +
            SHORT_BOOK_FROM_SUBQUERY_SELECT_CLAUSE +
            "      from books b " +
            "      group by b.id " +
            "      having b.id in (select distinct b2.id  " +
            "                      from books b2  " +
            "                               join book2genre b2g on b2.id = b2g.book_id  " +
            "                               join genres g on b2g.genre_id = g.id  " +
            "                      where g.slug = ?1)) as books_by_genre",
            nativeQuery = true,
            countQuery = "select distinct count(b) cnt   " +
                    "            from books b   " +
                    "            join book2genre b2g on b.id = b2g.book_id   " +
                    "            join genres g on b2g.genre_id = g.id   " +
                    "            where g.slug like ?1")
    Page<ShortBookDtoProjection> getBooksByGenre(String slug, Pageable pageable);

    @Query(value = SHORT_BOOK_SELECT_CLAUSE +
            SHORT_BOOK_FROM_SUBQUERY_SELECT_CLAUSE +
            "      from books b " +
            "      group by b.id " +
            "      having b.id in (select b2.id      " +
            "                      from books b2      " +
            "                               join book2author b2a on b2.id = b2a.book_id      " +
            "                               join authors a2 on b2a.author_id = a2.id      " +
            "                      where a2.slug = ?1)) as books_by_author",
            nativeQuery = true,
            countQuery = "select count(*) " +
                    "            from books b " +
                    "            join book2author b2a on b.id = b2a.book_id " +
                    "            join authors a on a.id = b2a.author_id " +
                    "            where a.slug like ?1")
    Page<ShortBookDtoProjection> getBooksListByAuthor(String slug, Pageable pageable);

    @Query(value = SHORT_BOOK_SELECT_CLAUSE +
            SHORT_BOOK_FROM_SUBQUERY_SELECT_CLAUSE +
            "      from books b " +
            "      group by b.id " +
            "      having b.id in (select b2.id " +
            "                      from books b2 " +
            "                               join book2tag b2t on b2.id = b2t.book_id " +
            "                               join tags t on b2t.tag_id = t.id " +
            "                      where t.slug = ?1)) as books_by_tag",
            nativeQuery = true,
            countQuery = "select count(b) " +
                    "            from books b  " +
                    "            join book2tag b2t on b.id = b2t.book_id  " +
                    "            join tags t on b2t.tag_id = t.id  " +
                    "            where t.slug like ?1")
    Page<ShortBookDtoProjection> getBooksByTag(String tagSlug, Pageable nextPage);


    @Query(value = SHORT_BOOK_SELECT_CLAUSE +
            SHORT_BOOK_FROM_SUBQUERY_SELECT_CLAUSE +
            "      from books b " +
            "      group by b.id " +
            "      having b.id in (select b2.id   " +
            "                      from books b2   " +
            "                               join book2user b2u on b2.id = b2u.book_id   " +
            "                               join users u on b2u.user_id = u.id   " +
            "                               join book2user_type b2ut on b2u.type_id = b2ut.id   " +
            "                      where u.email like :email and b2ut.code like :status)) " +
            "as books_by_user_and_status",
            nativeQuery = true,
            countQuery = "select count(*)  " +
                    "            from books b2  " +
                    "            join book2user b2u on b2.id = b2u.book_id  " +
                    "            join users u on b2u.user_id = u.id  " +
                    "            join book2user_type b2ut on b2u.type_id = b2ut.id  " +
                    "            where u.email like :email and b2ut.code like :status")
    List<ShortBookDtoProjection> getBooksByUserAndStatus(
            @Param("email") String email,
            @Param("status") String status
    );

    @Query("select bu.book from Book2UserEntity bu where bu.user.email like ?1 and bu.type.code = ?2")
    List<BookEntity> getBookEntitiesByUserAndStatus(
            @Param("email") String email,
            @Param("status") StatusType status
    );

    @Query(value = SHORT_BOOK_SELECT_CLAUSE +
            SHORT_BOOK_FROM_SUBQUERY_SELECT_CLAUSE +
            "      from books b " +
            "      group by b.id " +
            "      having b.slug in :slugs) as books_by_slugs", nativeQuery = true)
    List<ShortBookDtoProjection> getBooksBySlugsIn(@Param("slugs") List<String> slugs);

    @Query(value = "select sum(case when b2ut.code = 'PAID' then 1 else 0 end) myCount," +
            "       sum(case when b2ut.code = 'KEPT' then 1 else 0 end) keptCount," +
            "       sum(case when b2ut.code = 'CART' then 1 else 0 end) cartCount " +
            " from books b" +
            "         join book2user b2u on b.id = b2u.book_id" +
            "         join users u on u.id = b2u.user_id" +
            "         join book2user_type b2ut on b2u.type_id = b2ut.id" +
            " where u.email like :email", nativeQuery = true)
    BookSlugs getBooksByUserCount(@Param("email") String email);

    @Query("select b2u.book.slug from Book2UserEntity b2u " +
            "where b2u.user.email like :email")
    List<String> findSlugsByUserEmail(@Param("email") String email);

    @Query("select new com.example.MyBookShopApp.dto.book.BookSlugs(b2u.type.code, b2u.book.slug) " +
            "from Book2UserEntity b2u " +
            "where b2u.user.email like :email")
    List<BookSlugs> findBookSlugsByUserEmail(@Param("email") String email);
}
