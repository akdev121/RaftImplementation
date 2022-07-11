package com.raftProject.raftImplementation;

public class ReplyMessage {
	private String replyType;
	
	private VoteReply votereply;
	private AppendReply appendreply;

	public String getReplyType() {
		return replyType;
	}

	public AppendReply getAppendreply() {
		return appendreply;
	}

	public void setAppendreply(AppendReply appendreply) {
		this.appendreply = appendreply;
	}

	public void setReplyType(String replyType) {
		this.replyType = replyType;
	}

	public VoteReply getVotereply() {
		return votereply;
	}

	public void setVotereply(VoteReply votereply) {
		this.votereply = votereply;
	}
	
}
