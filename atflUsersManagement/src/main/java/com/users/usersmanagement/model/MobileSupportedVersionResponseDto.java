package com.users.usersmanagement.model;

public class MobileSupportedVersionResponseDto {
	private boolean forceUpdate;
	private Long buildNumber;

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	public Long getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(Long buildNumber) {
		this.buildNumber = buildNumber;
	}

}
