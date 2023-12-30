package com.users.usersmanagement.model;

public class RelatedOrganization {

	private String IsPrimaryMVG;
	private String Organization;
	public String getIsPrimaryMVG() {
		return IsPrimaryMVG;
	}
	public void setIsPrimaryMVG(String isPrimaryMVG) {
		IsPrimaryMVG = isPrimaryMVG;
	}
	public String getOrganization() {
		return Organization;
	}
	public void setOrganization(String organization) {
		Organization = organization;
	}
	@Override
	public String toString() {
		return "RelatedOrganization [IsPrimaryMVG=" + IsPrimaryMVG + ", Organization=" + Organization + "]";
	}

	
}
