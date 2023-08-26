package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.dto.shop.DocumentDto;
import com.example.MyBookShopApp.model.other.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {
    @Query("select new com.example.MyBookShopApp.dto.shop.DocumentDto(d.title, substring(d.text, 0, 700) , d.slug) " +
            "from DocumentEntity d " +
            "order by d.sortIndex asc ")
    List<DocumentDto> getAllDocuments();

    @Query("select new com.example.MyBookShopApp.dto.shop.DocumentDto(d.title, d.text , d.slug) " +
            "from DocumentEntity d  where d.slug like ?1")
    DocumentDto getDocumentBySlug(String slug);
}
