package com.raftProject.raftImplementation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.time.Instant;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListenThread implements Runnable {
		
	Message message = null;
	ObjectMapper mapper = null;
	MulticastSocket socket = null ;
	InetAddress group=null;
	DatagramPacket DpReceive = null;
	NodeInfo nodeSetup=null;
	Instant timenow=null;
	private boolean exec;
	 public ListenThread(NodeInfo node) throws IOException,SocketTimeoutException {
		super();
		message = new Message();
		mapper = new ObjectMapper();
		socket=new MulticastSocket(1234);
		nodeSetup =node; 
		group = InetAddress.getByName("228.5.6.7");
		socket.joinGroup(group);
//		socket.setSoTimeout(nodeSetup.getElectionTimeOut());
		this.exec=true;
		
	}

	public void run() {
		timenow=Instant.now();
//		 System.out.println("Thread receive started-->"+ Thread.currentThread().getName());
		 try {
		 while(this.exec) {
			
			  	byte[] receive = new byte[65535];
	        	// Step 2 : create a DatgramPacket to receive the data.
	            DpReceive = new DatagramPacket(receive, receive.length);
	            
	            // Step 3 : revieve the data in byte buffer.
	           
				socket.receive(DpReceive);
			
				timenow=Instant.now();
				
				
				
				message = mapper.readValue(receive, message.getClass());
				
				if (message.getRequest().equals("APPEND_RPC")) {
					if (message.getTerm()> nodeSetup.getCurrentTerm() && !message.getSender_name().equals(nodeSetup.getCandidateID())) {
						nodeSetup.setState("F");
						nodeSetup.setCurrentTerm(message.getTerm());
					}
					if(message.getTerm()== nodeSetup.getCurrentTerm() && !message.getSender_name().equals(nodeSetup.getCandidateID())) {
						nodeSetup.setState("F");
						nodeSetup.setCurrentLeader(message.getSender_name());
						
						
					
						
						//log consistency check- happens with every heartbeat
						//log append when new entries are present
						
						//calc current term and index of the log
						int curr_term = nodeSetup.getCurrentTerm()-1;
						int curr_index=-1;
						
						int leaderPrevIndex = message.getAppendRPC().getPrevLogIndexArr()[nodeSetup.getCandidateKey()];
						int leaderPrevTerm = message.getAppendRPC().getPrevLogTermArr()[nodeSetup.getCandidateKey()];
						
//						System.out.println("leaderPrevIndex for candidate "+nodeSetup.getCandidateKey()+" = "+leaderPrevIndex);
//						System.out.println("leaderPrevTerm for candidate"+nodeSetup.getCandidateKey()+" = "+leaderPrevTerm);
//						
						if (nodeSetup.getLog().size()>0) {
							int size = nodeSetup.getLog().size();
							curr_index=size-1;
							curr_term= nodeSetup.getLog().get(size-1).getTerm();
							
						}
//						if (leaderPrevIndex==-1 && message.getAppendRPC().getEntries().size()==0) {
//							appendReply(nodeSetup,true);
//						}
						int key = nodeSetup.getCandidateKey();
						ArrayList<Log> entry = new ArrayList<Log>();
						entry = message.getAppendRPC().getEntrylist().get(key);
						
						if(entry.size()!=0) {
							System.out.println("ENTRY for key ="+key+"="+entry);
						}
						
						
						if(curr_index>=leaderPrevIndex) {
							int fterm =  nodeSetup.getCurrentTerm()-1;
							if (leaderPrevIndex!=-1){
								fterm = nodeSetup.getLog().get(leaderPrevIndex).getTerm();
							}
							if (fterm==leaderPrevTerm) {
								//logic to append entry in the follower log
								if (leaderPrevIndex==-1 && entry.size()==0) {
									//no action required
									appendReply(nodeSetup,true);
								}else {
									//first remove if any extraneous entry from leaderPrevIndex to currindex then add
									
									for (int j=0; j<entry.size();j++) {
										nodeSetup.getLog().add(entry.get(j));
										appendReply(nodeSetup,true);
										new saveNodeInfo(nodeSetup).start();
									}
															
								
								}
								
								
							}if (fterm!=leaderPrevTerm && leaderPrevIndex>-1) {
								for(int k=leaderPrevIndex; k<nodeSetup.getLog().size();k++) {
									System.out.println("entries removed");
									nodeSetup.getLog().remove(k);
									appendReply(nodeSetup,false);
								}
							}
						}else {
							appendReply(nodeSetup,false);
						}
						
						
						if(message.getTerm()< nodeSetup.getCurrentTerm() && !message.getSender_name().equals(nodeSetup.getCandidateID())) {
							appendReply(nodeSetup,false);
						}
						
						
						if(message.getAppendRPC().getLeaderCommit()>nodeSetup.getCommitIndex()) {
							int cmitindex = Math.min(message.getAppendRPC().getLeaderCommit(), nodeSetup.getLog().size()-1);
							if(cmitindex>nodeSetup.getCommitIndex()) {
								// save from  nodeSetup.getCommitIndex() to cmitindex 
								nodeSetup.setCommitIndex(cmitindex);
								new SaveCourse(nodeSetup).start();
								new saveNodeInfo(nodeSetup).start();
							}
							
							
						}
					}
					
					
				}
				
				if (message.getRequest().equals("VOTE_REQUEST")){
//					System.out.println("election started-->"+ nodeSetup.getCandidateID());
					

					if (message.getTerm()> nodeSetup.getCurrentTerm() && !message.getSender_name().equals(nodeSetup.getCandidateID())){
//					 System.out.println("election started inside-->"+ nodeSetup.getCandidateID());
					 nodeSetup.setState("F");
					 nodeSetup.setCurrentTerm(message.getTerm());
					 nodeSetup.setVotedFor(-1);
					 //stop heartbeat thread and convert to follower
					new Election(receive,DpReceive.getPort(),DpReceive.getAddress(),message,nodeSetup).start();
					}
				}
				else {
					if (message.getTerm()> nodeSetup.getCurrentTerm() && !message.getSender_name().equals(nodeSetup.getCandidateID())) {
						nodeSetup.setState("F");
					}
//				heartbeats_leader= mapper.readValue(receive, heartbeats_leader.getClass());
				nodeSetup.setCurrentLeader(message.getSender_name());
//				System.out.println("heartbeats-->"+message.toString());
				}
	            
	  
	            // Clear the buffer after every message.
	            receive = new byte[65535];

			 }
		 }catch(IOException e2) {
	        	System.out.println("close");
	        	
	        }
		

	 }
	
	 public Instant getTime() {
	    	return timenow;
	    }
	 public void appendReply(NodeInfo node, boolean success) throws IOException {
//		 System.out.println("Append reply success = "+success);
		 AppendReply appreply = new AppendReply();
			appreply.setCurrentTerm(nodeSetup.getCurrentTerm());
			appreply.setSucess(success);
			appreply.setCandidate_key(node.getCandidateKey());
			ReplyMessage reply= new ReplyMessage();
			reply.setReplyType("APPEND_REPLY");
			
			reply.setAppendreply(appreply);
			
			
			
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
			
			
			DatagramPacket packet = new DatagramPacket(buf, buf.length, DpReceive.getAddress(), 1235);
			DatagramSocket socket1=new DatagramSocket();
			socket1.send(packet);
			socket1.close();
	 }
	 
	 public void socketClose() {
			this.socket.close();
		}
    
   

}
