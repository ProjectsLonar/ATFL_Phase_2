package com.lonar.folderDeletionSchedular.respository;

import java.io.Serializable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.lonar.folderDeletionSchedular.model.LtJobeLogs;

public interface LttJobeLogsRepository extends DataTablesRepository<LtJobeLogs, Serializable>{

}
