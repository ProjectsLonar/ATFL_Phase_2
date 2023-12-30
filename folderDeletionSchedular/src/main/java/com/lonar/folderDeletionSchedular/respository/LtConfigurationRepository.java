package com.lonar.folderDeletionSchedular.respository;

import java.io.Serializable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.lonar.folderDeletionSchedular.model.LtConfigurartion;

public interface LtConfigurationRepository extends DataTablesRepository<LtConfigurartion, Serializable>{

}
