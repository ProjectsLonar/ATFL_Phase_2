package com.users.usersmanagement.model;

import java.util.List;

public class ListOfRelatedOrganization {

	private RelatedOrganization relatedOrganization;


	public RelatedOrganization getRelatedOrganization() {
		return relatedOrganization;
	}


	public void setRelatedOrganization(RelatedOrganization relatedOrganization) {
		this.relatedOrganization = relatedOrganization;
	}


	@Override
	public String toString() {
		return "ListOfRelatedOrganization [getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}
