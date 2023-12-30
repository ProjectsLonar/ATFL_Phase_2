package com.lonar.folderDeletionSchedular.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "lt_job_schedule")
@JsonInclude(Include.NON_NULL)
public class LtJobeSchedule {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "schedule_id")
	private Long scheduleId;

	@Column(name = "schedule_type")
	private String scheduleType;
	
	@Column(name = "days")
	private Long days;
	
	@Column(name = "time")
	private String time;
	
	@Column(name = "hours")
	private String hours;
	
	@Column(name = "job_type")
	private String jobType;
	
	@Column(name = "frequency")
	private Long frequency;
	
	
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

	public Long getDays() {
		return days;
	}

	public void setDays(Long days) {
		this.days = days;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public Long getFrequency() {
		return frequency;
	}

	public void setFrequency(Long frequency) {
		this.frequency = frequency;
	}
	
	
	
}