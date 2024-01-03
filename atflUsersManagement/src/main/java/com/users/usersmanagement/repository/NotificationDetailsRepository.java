package com.users.usersmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.users.usersmanagement.model.NotificationDetails;
@Repository
public interface NotificationDetailsRepository extends JpaRepository<NotificationDetails, Long> {

}
