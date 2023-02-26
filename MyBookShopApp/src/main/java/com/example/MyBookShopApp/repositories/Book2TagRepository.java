package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.links.Book2TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2TagRepository extends JpaRepository<Book2TagEntity, Integer> {

    @Query(value = "Select max(c) from (SELECT tag_id, Count(book_id) c from book2tag group by tag_id ) as a ", nativeQuery = true)
    Integer findMaxTagValue();

    @Query(value = "Select min(c) tag_id from (SELECT tag_id, Count(book_id) c from book2tag group by tag_id) as a ", nativeQuery = true)
    Integer findMinTagValue();
}
