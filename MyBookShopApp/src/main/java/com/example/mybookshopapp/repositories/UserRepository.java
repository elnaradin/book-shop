package com.example.mybookshopapp.repositories;

import com.example.mybookshopapp.dto.user.UserDto;
import com.example.mybookshopapp.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    long deleteByEmail(String email);
    Optional<UserEntity> findFirstByPhone(String phone);

    boolean existsByEmail(String s);

    boolean existsByPhone(String s);

    Optional<UserEntity> findByEmail(String email);

    @Query(value = "select u.name fullName, " +
            "              u.balance balance, " +
            "              u.email email, " +
            "              u.phone phone, " +
            "              u.reg_time regTime," +
            "              (select count(*) > 0 from user_roles r " +
            "                  join user2role u2r " +
            "                  on u2r.role_id = r.id " +
            "                  where u2r.user_id = u.id and r.role like 'ADMIN') isAdmin" +
//            "                  (select count(*) > 0 from user_roles r " +
//            "                  join user2role u2r " +
//            "                  on u2r.role_id = r.id " +
//            "                  where u2r.user_id = u.id and r.role like 'USER') isUser"+
            "                  from users u " +
            "                  where  upper(u.name) like upper(concat('%', :word, '%')) " +
            "                  or  upper(u.email) like upper(concat('%', :word, '%')) " +
            "                  or u.phone like concat('%', :word, '%')",
            nativeQuery = true,
    countQuery = " select count(*) from users u "+
            "      where  upper(u.name) like upper(concat('%', :word, '%')) " +
            "      or  upper(u.email) like upper(concat('%', :word, '%')) " +
            "      or u.phone like concat('%', :word, '%')"
    )
    Page<UserDto> getUserDtoPageByNameOrEmailOrPhone(@Param("word") String searchWord, Pageable pageRequest);
}
