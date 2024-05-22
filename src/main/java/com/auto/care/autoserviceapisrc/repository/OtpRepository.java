package com.auto.care.autoserviceapisrc.repository;


import com.auto.care.autoserviceapisrc.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

}
