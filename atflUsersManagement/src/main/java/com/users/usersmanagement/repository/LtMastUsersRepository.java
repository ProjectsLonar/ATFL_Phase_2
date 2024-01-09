package com.users.usersmanagement.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.users.usersmanagement.model.LtMastUsers;

public interface LtMastUsersRepository extends DataTablesRepository<LtMastUsers, Long> {

}
