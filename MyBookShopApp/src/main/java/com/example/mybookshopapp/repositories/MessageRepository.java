package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.model.book.review.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

}
