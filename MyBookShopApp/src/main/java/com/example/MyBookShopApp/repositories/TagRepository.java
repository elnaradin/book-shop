package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.tags.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    @Query("select t from TagEntity t " +
            "join Book2TagEntity bt on t.id=bt.tag.id " +
            "join BookEntity b on bt.book.id=b.id " +
            "where b.slug =?1")
    List<TagEntity> findTagEntitiesByBookSlug(String slug);
}
