package com.users.usersmanagement.repository;

import java.io.Serializable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.users.usersmanagement.model.LtMastOrganisations;

public interface LtMastOrganisationsRepository extends DataTablesRepository<LtMastOrganisations, Serializable>{

}
