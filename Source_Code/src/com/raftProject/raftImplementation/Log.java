package com.raftProject.raftImplementation;

public class Log {
	
	private int term;
	private String key;
	private String Value;
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
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	@Override
	public String toString() {
		return "Log [term=" + term + ", key=" + key + ", Value=" + Value + "]";
	}
	
	

}
