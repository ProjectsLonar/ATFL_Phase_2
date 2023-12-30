package com.lonar.cartservice.atflCartService.repository;


import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.lonar.cartservice.atflCartService.model.LtMastOutles;

//import com.lonar.cartservice.atflCartService.model.LtMastOutlets;


@Repository
public interface LtMastOutletRepository  extends DataTablesRepository<LtMastOutles, String> {




}
