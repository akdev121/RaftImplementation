package com.raftProject.raftImplementation;

public class Controller_Req {
	private String sender_name;
	private String request;
	private int term;
	private String key;
	private String value;
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
		return "Controller_Req [sender_name=" + sender_name + ", request=" + request + ", term=" + term + ", key=" + key
				+ ", value=" + value + "]";
	}
	
	
}
