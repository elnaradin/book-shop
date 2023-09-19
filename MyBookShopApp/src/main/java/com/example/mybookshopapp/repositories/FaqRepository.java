package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.dto.shop.FaqDto;
import com.example.mybookshopapp.model.other.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, Integer> {
    @Query("select new com.example.mybookshopapp.dto.shop.FaqDto(f.question, f.answer) " +
            "from FaqEntity f " +
            "order by f.sortIndex asc ")
    List<FaqDto> getAllFaqs();
}
