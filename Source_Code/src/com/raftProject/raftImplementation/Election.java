package com.raftProject.raftImplementation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Election extends Thread {
	
	InetAddress address=null;
	int port=0;
	byte[] receive=null;
	DatagramSocket socket = null;
	Message message=null;
	NodeInfo nodeSetup=null;
	ObjectMapper mapper=null;
	
	
	
	public Election(byte[] receive, int port, InetAddress address, Message message,NodeInfo nodeinfo) throws IOException {
		super();
		this.address = address;
		this.port = port;
		this.receive = receive;
		this.socket=new DatagramSocket();
		this.message=message;
		this.nodeSetup=nodeinfo;
		mapper = new ObjectMapper();
	}
	
	public void run() {
		
		try {
			
		int lastTerm=0;
		int size=nodeSetup.getLog().size();
		if (nodeSetup.getLog().size()>0) {
			lastTerm = nodeSetup.getLog().get(size-1).getTerm();
		}
		
		ReplyMessage reply= new ReplyMessage();
		reply.setReplyType("VOTE_REPLY");
		VoteReply votereply = new VoteReply();
		
		
		boolean logComplete=false;
//		String votemessage ="";
		
		if (message.getRequestRPC().getLastlogTerm()>lastTerm){
			logComplete=true;
		}else if (message.getRequestRPC().getLastlogTerm()==lastTerm && message.getRequestRPC().getLastLogindex()>=size-1) {
			logComplete=true;
		}
		
		if (nodeSetup.getCurrentTerm()==message.getRequestRPC().getTerm() && logComplete && nodeSetup.getVotedFor()!=message.getTerm()) {
			votereply.setVote(true);
		}else {
			votereply.setVote(false);
		}
		
		votereply.setCurrentTerm(nodeSetup.getCurrentTerm());
		
		reply.setVotereply(votereply);
		
		byte[] buf = new byte[2024];
		String json="";
		json = mapper.writeValueAsString(reply);
		
		try {
			 json = mapper.writeValueAsString(reply);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buf = json.getBytes();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		
		socket.send(packet);
		
		nodeSetup.setVotedFor(message.getRequestRPC().getTerm());
		
//		if (nodeSetup.getCurrentTerm()<=message.getTerm()) {
//			if( nodeSetup.getVotedFor()!=message.getTerm()) {
//			   votemessage = "TRUE";
//				nodeSetup.setVotedFor(message.getTerm());
//				byte[] buf = new byte[256];
//				buf = votemessage.getBytes();
////				System.out.println("Voting reply");
//				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
//				
//				socket.send(packet);
//				
//			}
//		}
//		else {
//			votemessage = "FALSE";
//		}
		}catch(IOException e1) {
			System.out.println("Exeception occurred at Election.java");
		}
		
	}
	
	
}
