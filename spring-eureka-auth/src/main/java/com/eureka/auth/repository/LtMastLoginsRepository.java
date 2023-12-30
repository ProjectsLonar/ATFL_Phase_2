package com.eureka.auth.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.eureka.auth.model.LtMastLogins;


public interface LtMastLoginsRepository extends DataTablesRepository<LtMastLogins, Long> {

}
