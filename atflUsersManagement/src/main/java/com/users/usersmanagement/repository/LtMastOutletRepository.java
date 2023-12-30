package com.users.usersmanagement.repository;


import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.users.usersmanagement.model.LtMastOutlets;
import com.users.usersmanagement.model.LtMastOutlets;

public interface LtMastOutletRepository  extends DataTablesRepository<LtMastOutlets, String> {
}