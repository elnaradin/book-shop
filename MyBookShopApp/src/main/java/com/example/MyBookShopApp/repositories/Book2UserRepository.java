package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.links.Book2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface Book2UserRepository extends JpaRepository<Book2UserEntity, Integer> {

//    @Transactional
//    @Modifying
//    @Query("delete from Book2UserEntity b where b.book.slug = ?1 and b.type.code=?2")
//    void deleteBook2UserEntityByBookSlug(String slug, String status);

    boolean existsByTypeCodeAndUserId(String code, Integer userId);


    boolean existsByUserIdAndBookId(Integer userId, Integer bookId);

    @Transactional
    @Modifying
    @Query("update Book2UserEntity bu set bu.type.id = ?2 where bu.book.id = ?1 and bu.user.id = ?3")
    void changeBookStatus(Integer bookId, Integer book2UserTypeId, Integer userId);
}
