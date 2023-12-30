package com.users.usersmanagement.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.users.usersmanagement.model.LtMastOutletsDump;

public interface LtMastOutletDumpRepository extends DataTablesRepository<LtMastOutletsDump, String> {
}
