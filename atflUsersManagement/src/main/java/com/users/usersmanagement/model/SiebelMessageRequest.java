package com.users.usersmanagement.model;

public class SiebelMessageRequest {
private SiebelMessage SiebelMessage;

public SiebelMessage getSiebelMessage() {
	return SiebelMessage;
}

public void setSiebelMessage(SiebelMessage siebelMessage) {
	SiebelMessage = siebelMessage;
}

@Override
public String toString() {
	return "SiebelMessageRequest [SiebelMessage=" + SiebelMessage + "]";
}

}
