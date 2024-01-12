package com.lonar.cartservice.atflCartService.repository;

import java.io.Serializable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.lonar.cartservice.atflCartService.model.LtSoLines;

public interface LtSoLinesRepository extends DataTablesRepository<LtSoLines, Long>{

}
