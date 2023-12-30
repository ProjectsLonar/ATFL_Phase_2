package com.lonar.atflMobileInterfaceService.repository;

import java.io.Serializable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.lonar.atflMobileInterfaceService.model.LtJobeImportExport;

public interface LtJobImportExportRepository extends DataTablesRepository<LtJobeImportExport, Serializable>{

}
