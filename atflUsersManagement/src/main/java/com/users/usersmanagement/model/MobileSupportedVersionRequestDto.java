package com.users.usersmanagement.model;

public class MobileSupportedVersionRequestDto {
	private String platform;
	private Long buildNumber;

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Long getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(Long buildNumber) {
		this.buildNumber = buildNumber;
	}
}
