package com.lonar.folderDeletionSchedular.respository;

import java.io.Serializable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.lonar.folderDeletionSchedular.model.LtJobeImportExport;

public interface LtJobImportExportRepository extends DataTablesRepository<LtJobeImportExport, Serializable>{

}
