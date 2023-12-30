package com.users.usersmanagement.model;

import java.util.List;

public class SiebelMessage {
private String IntObjectFormat;
private String MessageId;
private String IntObjectName;
private String MessageType;
private ListOfOutletInterface listOfOutletInterface;
public String getIntObjectFormat() {
	return IntObjectFormat;
}
public void setIntObjectFormat(String intObjectFormat) {
	IntObjectFormat = intObjectFormat;
}
public String getMessageId() {
	return MessageId;
}
public void setMessageId(String messageId) {
	MessageId = messageId;
}
public String getIntObjectName() {
	return IntObjectName;
}
public void setIntObjectName(String intObjectName) {
	IntObjectName = intObjectName;
}
public String getMessageType() {
	return MessageType;
}
public void setMessageType(String messageType) {
	MessageType = messageType;
}

public ListOfOutletInterface getListOfOutletInterface() {
	return listOfOutletInterface;
}
public void setListOfOutletInterface(ListOfOutletInterface listOfOutletInterface) {
	this.listOfOutletInterface = listOfOutletInterface;
}
@Override
public String toString() {
	return "SiebelMessage [IntObjectFormat=" + IntObjectFormat + ", MessageId=" + MessageId + ", IntObjectName="
			+ IntObjectName + ", MessageType=" + MessageType + ", listOfOutletInterface=" + listOfOutletInterface + "]";
}

}
