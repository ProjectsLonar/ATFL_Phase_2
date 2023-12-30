package com.users.usersmanagement.repository;

import java.io.Serializable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.users.usersmanagement.model.LtConfigurartion;

public interface LtConfigurationRepository extends DataTablesRepository<LtConfigurartion, Serializable>{

}
