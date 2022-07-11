package com.raftProject.raftImplementation;


public class Message {
	
	private String sender_name;
	private String request;
	private int term;
	private String key;
	private String value;
	
	private AppendRPC appendRPC;
	private RequestRPC requestRPC;
	 
	public AppendRPC getAppendRPC() {
		return appendRPC;
	}
	public void setAppendRPC(AppendRPC appendRPC) {
		this.appendRPC = appendRPC;
	}
	public RequestRPC getRequestRPC() {
		return requestRPC;
	}
	public void setRequestRPC(RequestRPC requestRPC) {
		this.requestRPC = requestRPC;
	}
	public String getSender_name() {
		return sender_name;
	}
	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Message [sender_name=" + sender_name + ", request=" + request + ", term=" + term + ", key=" + key
				+ ", value=" + value + "]";
	}
	
}
