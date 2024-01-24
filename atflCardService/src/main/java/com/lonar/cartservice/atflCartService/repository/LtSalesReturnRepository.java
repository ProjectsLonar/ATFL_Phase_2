 package com.lonar.cartservice.atflCartService.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.lonar.cartservice.atflCartService.model.LtSalesReturnHeader;

public interface LtSalesReturnRepository extends DataTablesRepository<LtSalesReturnHeader, Long> {

}
