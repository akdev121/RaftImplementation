package com.raftProject.raftImplementation;
import java.util.ArrayList;

public class NodeInfo {
	
	private String candidateID;
	private ArrayList<Log> log;
	
	
	private int ElectionTimeOut;
	private int heartBeatInterval;
	private String state;
	private int commitIndex;
	private String currentLeader;
	private boolean doTimeout;
	private int candidateKey;
	
	
	private int lastApplied;
	private int nextIndex[] = new int[5];
	private int matchIndex[] = new int[5];
	private int currentTerm;
	private int votedFor;
	
	
	
	public int getCandidateKey() {
		return candidateKey;
	}

	public void setCandidateKey(int candidateKey) {
		this.candidateKey = candidateKey;
	}

	public int getLastApplied() {
		return lastApplied;
	}

	public void setLastApplied(int lastApplied) {
		this.lastApplied = lastApplied;
	}

	public int[] getNextIndex() {
		return nextIndex;
	}

	public void setNextIndex(int[] nextIndex) {
		this.nextIndex = nextIndex;
	}

	public int[] getMatchIndex() {
		return matchIndex;
	}

	public void setMatchIndex(int[] matchIndex) {
		this.matchIndex = matchIndex;
	}

	public int getCommitIndex() {
		return commitIndex;
	}

	public void setCommitIndex(int commitIndex) {
		this.commitIndex = commitIndex;
	}

	public boolean isDoTimeout() {
		return doTimeout;
	}

	public void setDoTimeout(boolean doTimeout) {
		this.doTimeout = doTimeout;
	}

	public String getCurrentLeader() {
		return currentLeader;
	}

	public void setCurrentLeader(String currentLeader) {
		this.currentLeader = currentLeader;
	}

	public String getCandidateID() {
		return candidateID;
	}

	public void setCandidateID(String candidateID) {
		this.candidateID = candidateID;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(int currentTerm) {
		this.currentTerm = currentTerm;
	}
	public int getVotedFor() {
		return votedFor;
	}
	public void setVotedFor(int votedFor) {
		this.votedFor = votedFor;
	}
	public ArrayList<Log> getLog() {
		return log;
	}
	public void setLog(ArrayList<Log> log) {
		this.log = log;
	}
	public int getElectionTimeOut() {
		return ElectionTimeOut;
	}
	public void setElectionTimeOut(int electionTimeOut) {
		ElectionTimeOut = electionTimeOut;
	}
	public int getHeartBeatInterval() {
		return heartBeatInterval;
	}
	public void setHeartBeatInterval(int heartBeatInterval) {
		this.heartBeatInterval = heartBeatInterval;
	}

	@Override
	public String toString() {
		return "NodeInfo [currentTerm=" + currentTerm + ", votedFor=" + votedFor + ", log=" + log + ", ElectionTimeOut="
				+ ElectionTimeOut + ", heartBeatInterval=" + heartBeatInterval + ", state=" + state + ", candidateID="
				+ candidateID + ", currentLeader=" + currentLeader + "]";
	}
	

	
	
}
