package com.raftProject.raftImplementation;
public class RequestRPC {
	
	 private int term;
	 private String candidate_id;
	 private int lastLogindex;
	 private int lastlogTerm;
	 private String request;
	 
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
	public String getCandidate_id() {
		return candidate_id;
	}
	public void setCandidate_id(String candidate_id) {
		this.candidate_id = candidate_id;
	}
	public int getLastLogindex() {
		return lastLogindex;
	}
	public void setLastLogindex(int lastLogindex) {
		this.lastLogindex = lastLogindex;
	}
	public int getLastlogTerm() {
		return lastlogTerm;
	}
	public void setLastlogTerm(int lastlogTerm) {
		this.lastlogTerm = lastlogTerm;
	}
	@Override
	public String toString() {
		return "RequestRPC [term=" + term + ", candidate_id=" + candidate_id + ", lastLogindex=" + lastLogindex
				+ ", lastlogTerm=" + lastlogTerm + "]";
	}
	 
	 

}
