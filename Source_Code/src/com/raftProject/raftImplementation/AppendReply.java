package com.raftProject.raftImplementation;

public class AppendReply {
	private int currentTerm;
	private boolean sucess;
	private int candidate_key;
	
	public int getCandidate_key() {
		return candidate_key;
	}
	public void setCandidate_key(int candidate_key) {
		this.candidate_key = candidate_key;
	}
	public int getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(int currentTerm) {
		this.currentTerm = currentTerm;
	}
	public boolean isSucess() {
		return sucess;
	}
	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}
	
	
	
}
