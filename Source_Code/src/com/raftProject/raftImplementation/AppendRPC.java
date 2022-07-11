package com.raftProject.raftImplementation;
import java.util.ArrayList;

public class AppendRPC {
	private String request;
	 private int term;
	 private String leader_id;
	 private int prevLogindex;
	 private int prevlogTerm;
	 
	 private int[] prevLogIndexArr = new int[5];
	 private int[] prevLogTermArr = new int[5];
	 
	private ArrayList<Log> entries;
	
		 //leader's commitindex
	private int leaderCommit;
	
	 ArrayList<ArrayList<Log>> entrylist =new ArrayList<ArrayList<Log> >(5); 
	public ArrayList<ArrayList<Log>> getEntrylist() {
		return entrylist;
	}
	public void setEntrylist(ArrayList<ArrayList<Log>> entrylist) {
		this.entrylist = entrylist;
	}

	
	 public int[] getPrevLogIndexArr() {
		return prevLogIndexArr;
	}
	public void setPrevLogIndexArr(int[] prevLogIndexArr) {
		this.prevLogIndexArr = prevLogIndexArr;
	}
	public int[] getPrevLogTermArr() {
		return prevLogTermArr;
	}
	public void setPrevLogTermArr(int[] prevLogTermArr) {
		this.prevLogTermArr = prevLogTermArr;
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
	public String getLeader_id() {
		return leader_id;
	}
	public void setLeader_id(String leader_id) {
		this.leader_id = leader_id;
	}
	public int getPrevLogindex() {
		return prevLogindex;
	}
	public void setPrevLogindex(int prevLogindex) {
		this.prevLogindex = prevLogindex;
	}
	public int getPrevlogTerm() {
		return prevlogTerm;
	}
	public void setPrevlogTerm(int prevlogTerm) {
		this.prevlogTerm = prevlogTerm;
	}

	public ArrayList<Log> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<Log> entries) {
		this.entries = entries;
	}
	public int getLeaderCommit() {
		return leaderCommit;
	}
	public void setLeaderCommit(int leaderCommit) {
		this.leaderCommit = leaderCommit;
	}
	@Override
	public String toString() {
		return "AppendRPC [term=" + term + ", leader_id=" + leader_id + ", prevLogindex=" + prevLogindex
				+ ", prevlogTerm=" + prevlogTerm + ", log=" + entries + ", leaderCommit=" + leaderCommit + "]";
	}
	 
}
