package com.lonar.cartservice.atflCartService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lonar.cartservice.atflCartService.model.NotificationDetails;
@Repository
public interface NotificationDetailsRepository extends JpaRepository<NotificationDetails, Long> {

}
