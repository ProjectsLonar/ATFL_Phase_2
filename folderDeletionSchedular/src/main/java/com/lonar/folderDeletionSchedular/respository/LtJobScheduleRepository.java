package com.lonar.folderDeletionSchedular.respository;

import java.io.Serializable;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

import com.lonar.folderDeletionSchedular.model.LtJobeSchedule;

public interface LtJobScheduleRepository extends DataTablesRepository<LtJobeSchedule , Serializable>{

}
