package com.users.usersmanagement.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.users.usersmanagement.model.LtFileUpload;

public interface LtFileUploadRepository extends DataTablesRepository<LtFileUpload, Long>{

}
