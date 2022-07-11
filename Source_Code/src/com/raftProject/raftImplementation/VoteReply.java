package com.raftProject.raftImplementation;

public class VoteReply {
	private int currentTerm;
	private boolean vote;
	public int getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(int currentTerm) {
		this.currentTerm = currentTerm;
	}
	public boolean isVote() {
		return vote;
	}
	public void setVote(boolean vote) {
		this.vote = vote;
	}
	@Override
	public String toString() {
		return "VoteReply [currentTerm=" + currentTerm + ", vote=" + vote + "]";
	}
	
	
}
