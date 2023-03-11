package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {

    Page<BookEntity> findBookEntitiesByTitleContaining(String example, Pageable nextPage);

    Integer countBookEntitiesByTitleContaining(String example);

    Page<BookEntity> findBookEntitiesByPubDateBetweenOrderByPubDateDesc(LocalDate from, LocalDate to, Pageable nextPage);

    BookEntity findBookEntityBySlug(String slug);

    List<BookEntity> findBookEntitiesBySlugIn(String[] slugs);

    @Query("select b from BookEntity b " +
            "join Book2UserEntity bu on b.id = bu.book.id " +
            "join Book2UserTypeEntity but on bu.type.id = but.id " +
            "where bu.user.id = ?1 and but.code =?2")
    List<BookEntity> findBookEntitiesByUserAndStatus(Integer userId, String statusId);

    @Query(value = "select b from BookEntity b " +
            "join Book2UserEntity bu on b.id = bu.book.id " +
            "join Book2UserTypeEntity but on bu.type.id = but.id " +
            "group by b.id  " +
            "order by sum(case when but.code = 'PAID' then 1 else 0 end)  + " +
            "0.7 * sum(case when but.code = 'CART' then 1 else 0 end) + " +
            "0.4 * sum(case when but.code = 'KEPT' then 1 else 0 end) desc")
    Page<BookEntity> findPopularBooks(Pageable pageable);

    @Query(value = "Select b from BookEntity b " +
            "join Book2TagEntity bt on b.id=bt.book.id " +
            "where bt.tag.id = ?1")
    Page<BookEntity> findBookEntitiesByTagIs(Integer tagId, Pageable pageable);

    @Query(value = "select count(b) from BookEntity b " +
            "join Book2TagEntity bt on b.id=bt.book.id " +
            "join TagEntity t on bt.tag.id=t.id " +
            "where t.id = ?1")
    Integer countBookEntitiesByTag(Integer tagId);


    @Query(value = "Select b from BookEntity b " +
            "join Book2GenreEntity bg on b.id=bg.book.id " +
            "join GenreEntity g on bg.genre.id = g.id " +
            "where g.id = ?1")
    Page<BookEntity> findBookEntitiesByGenre(Integer genreId, Pageable pageable);

    @Query("SELECT count (b) from BookEntity  b " +
            "join Book2GenreEntity bg on b.id=bg.book.id " +
            "where bg.genre.id = :genreId")
    Integer countBookEntitiesByGenre(Integer genreId);

    @Query("select b from BookEntity b " +
            "join Book2AuthorEntity ba on b.id = ba.book.id " +
            "where ba.author.id =:authorId")
    Page<BookEntity> findBookEntitiesByAuthorId(Integer authorId, Pageable pageable);

    @Query("select count(b) from BookEntity b " +
            "join Book2AuthorEntity ba on b.id = ba.book.id " +
            "where ba.author.id =:authorId")
    Integer countBookEntitiesByAuthorId(Integer authorId);
}
