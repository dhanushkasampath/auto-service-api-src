package com.auto.care.autoserviceapisrc.repository;


import com.auto.care.autoserviceapisrc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query(value = "SELECT u FROM User u WHERE u.userName = :username")
    User findByUserName(@Param("username") String userNameProvided);
    User findOneByEmail(String providedUserName);

}
