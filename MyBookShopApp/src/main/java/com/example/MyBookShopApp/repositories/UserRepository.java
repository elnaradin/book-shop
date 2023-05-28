package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {


    Optional<UserEntity> findBookstoreUserByEmail(String s);


    @Query("select u.balance from UserEntity u where u.id = ?1")
    Integer getBalanceById(Integer id);

    Optional<UserEntity> findByEmail(String email);


    @Modifying
    @Query("delete UserEntity u where u.regTime = ?1 and (u.email = null or u.phone = null)")
    void deleteEmptyUsers(LocalDateTime expiryDate);
}
