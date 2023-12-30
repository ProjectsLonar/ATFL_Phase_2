package com.atflMasterManagement.masterservice.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

//import com.atflMasterManagement.masterservice.model.LtMastProducts;

import com.atflMasterManagement.masterservice.model.LtMastProducts;

//public interface LtMastProductsRepository extends DataTablesRepository<LtMastProducts, Long> {
//
//}

public interface LtMastProductsRepository extends DataTablesRepository<LtMastProducts, String> {

}