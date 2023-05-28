package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.dto.author.SmallAuthorsDto;
import com.example.MyBookShopApp.dto.genre.GenreDto;
import com.example.MyBookShopApp.dto.tag.SmallTagsDto;
import com.example.MyBookShopApp.model.book.BookEntity;
import com.example.MyBookShopApp.repositories.custom.CustomBookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer>, CustomBookRepository {

    Page<BookEntity> findBookEntitiesByTitleContaining(String example, Pageable nextPage);

    Integer countBookEntitiesByTitleContaining(String example);

    Page<BookEntity> findBookEntitiesByPubDateBetweenOrderByPubDateDesc(LocalDate from, LocalDate to, Pageable nextPage);

    Integer countByPubDateBetween(LocalDate from, LocalDate to);

    BookEntity findBookEntityBySlug(String slug);

    List<BookEntity> findBookEntitiesByIdIn(List<Integer> bookIds);


    @Query(value = "select b from BookEntity b " +
            "inner join Book2UserEntity bu on b.id = bu.book.id " +
            "inner join Book2UserTypeEntity but on bu.type.id = but.id " +
            "group by b.id  " +
            "order by sum(case when but.code = 'PAID' then 1 else 0 end)  + " +
            "0.7 * sum(case when but.code = 'CART' then 1 else 0 end) + " +
            "0.4 * sum(case when but.code = 'KEPT' then 1 else 0 end) desc")
    Page<BookEntity> findPopularBooks(Pageable pageable);

    @Query(value = "Select b from BookEntity b " +
            "inner join Book2TagEntity bt on b.id=bt.book.id " +
            "where bt.tag.id = ?1")
    Page<BookEntity> findBookEntitiesByTagIs(Integer tagId, Pageable pageable);

    @Query(value = "select count(b) from BookEntity b " +
            "inner join Book2TagEntity bt on b.id=bt.book.id " +
            "inner join TagEntity t on bt.tag.id=t.id " +
            "where t.id = ?1")
    Integer countBookEntitiesByTag(Integer tagId);


    @Query(value = "Select b from BookEntity b " +
            "inner join Book2GenreEntity bg on b.id=bg.book.id " +
            "inner join GenreEntity g on bg.genre.id = g.id " +
            "where g.id = ?1")
    Page<BookEntity> findBookEntitiesByGenre(Integer genreId, Pageable pageable);


    @Query("select b from BookEntity b " +
            "inner join Book2AuthorEntity ba on b.id = ba.book.id " +
            "where ba.author.id =:authorId")
    Page<BookEntity> findBookEntitiesByAuthorId(Integer authorId, Pageable pageable);

    @Query("select count(b) from BookEntity b " +
            "inner join Book2AuthorEntity ba on b.id = ba.book.id " +
            "where ba.author.id =:authorId")
    Integer countBookEntitiesByAuthorId(Integer authorId);

    @Query("SELECT new com.example.MyBookShopApp.dto.genre.GenreDto(bg.genre.id, count (b))  from BookEntity  b " +
            "inner join Book2GenreEntity bg on b.id=bg.book.id " +
            "group by bg.genre.id")
    List<GenreDto> countBooksByGenre();

    @Query("select count (b) from BookEntity b inner join Book2GenreEntity bg on bg.book.id=b.id where bg.genre.slug =?1")
    Integer countBookEntitiesByGenre(String genreSlug);

    @Query("select b from BookEntity b inner join Book2UserEntity bu on bu.book.id=b.id where bu.user.id =?1 and bu.type.code=?2")
    List<BookEntity> findBooksByUserIdAndStatus(Integer id, String status);

    @Query("select bu.author.name from BookEntity b join Book2AuthorEntity bu on bu.book.id = b.id where b.id =?1 order by bu.sortIndex ASC ")
    List<String> getAuthorNames(Integer bookId);

    @Query("select sum (br.rating.value) / count (br.rating.value) from BookEntity b inner join Book2RatingEntity br on br.book.id = b.id where b.id = ?1")
    Integer getBookRatingById(Integer bookId);

    @Query("select new com.example.MyBookShopApp.dto.author.SmallAuthorsDto(ba.author.name, ba.author.slug) from BookEntity b " +
            "inner join Book2AuthorEntity ba on ba.book.id = b.id where b.id = ?1 order by ba.sortIndex asc ")
    List<SmallAuthorsDto> getAuthorNamesAndSlugsById(Integer bookId);

    @Query("select count(br.rating) from BookEntity b inner join Book2RatingEntity br on br.book.id= b.id where b.id = ?1")
    Integer countRatingsById(Integer bookId);

    @Query("select new com.example.MyBookShopApp.dto.tag.SmallTagsDto(bt.tag.id, bt.tag.name) from BookEntity  b join Book2TagEntity  bt on bt.book.id= b.id where b.id = ?1")
    List<SmallTagsDto> getTagsById(Integer bookId);


    @Query("select bu.type.code from BookEntity b join Book2UserEntity bu on bu.book.id = b.id where b.id = ?1 and bu.user.id =?2")
    Optional<String> findBookStatusById(Integer bookId, Integer userId);


    List<BookEntity> findBookEntityByIdIn(List<Integer> bookIds);

    @Query("select count(b) from BookEntity b join Book2UserEntity bu on bu.book.id=b.id where bu.user.id=?1 and bu.type.code=?2")
    Integer countBooksByUserIdAndStatus(Integer userId, String status);

    @Query("select b from BookEntity b join Book2UserEntity bu on bu.book.id=b.id where bu.user.id=?1 and bu.type.code=?2")
    List<BookEntity> getBooksByUserIdAndStatus(Integer userId, String status);

    List<BookEntity> getBooksByIdIn(List<Integer> bookIds);
}
