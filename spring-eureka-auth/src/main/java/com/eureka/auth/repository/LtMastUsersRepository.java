package com.eureka.auth.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.eureka.auth.model.LtMastUsers;

public interface LtMastUsersRepository extends DataTablesRepository<LtMastUsers, String> {

}
