package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.dto.tag.ShortTagDto;
import com.example.mybookshopapp.dto.tag.TagDtoProjection;
import com.example.mybookshopapp.model.book.tags.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    long deleteBySlug(String slug);
    Optional<TagEntity> findBySlug(String slug);
    List<TagEntity> findBySlugIn(Collection<String> slugs);

    @Query("select new com.example.mybookshopapp.dto.tag.ShortTagDto(b2t.tag.slug, b2t.tag.name) " +
            "from Book2TagEntity b2t " +
            "where b2t.book.slug like  ?1")
    List<ShortTagDto> getTagsByBookSlug(String slug);

    @Query(value = "select t.slug                                                 slug,   " +
            "       t.name                                                        name,   " +
            "       round((cast((count(b.id) - min) as float) / (max - min)) * 3) size   " +
            "from (select max(books_count) max,   " +
            "             min(books_count) min   " +
            "      from (select count(b) books_count   " +
            "            from tags t   " +
            "                     left join book2tag b2t on t.id = b2t.tag_id   " +
            "                     left join books b on b2t.book_id = b.id   " +
            "            group by t.slug, t.name) bc) vals,   " +
            "     tags t   " +
            "         left join book2tag b2t on t.id = b2t.tag_id   " +
            "         left join books b on b.id = b2t.book_id   " +
            "group by t.name, t.slug, t.name, max, min",
            nativeQuery = true)
    List<TagDtoProjection> getAllTags();


    @Query("select new com.example.mybookshopapp.dto.tag.ShortTagDto(t.slug, t.name) from  TagEntity t " +
            "where t.slug = ?1")
    Optional<ShortTagDto> getShortTagDtoBySlug(String tagSlug);
    @Query("select new com.example.mybookshopapp.dto.tag.ShortTagDto(t.slug, t.name) from  TagEntity t " +
            "order by t.name asc ")
    List<ShortTagDto> getShortTagDtos();
}
