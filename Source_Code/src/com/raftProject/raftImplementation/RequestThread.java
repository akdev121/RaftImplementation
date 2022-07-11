package com.raftProject.raftImplementation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestThread extends Thread{
	
	Message message=null;
	DatagramSocket ds =null;
	InetAddress group = null;
	ObjectMapper mapper=null;
	NodeInfo nodeinfo=null;
	RequestRPC reqRPC=null;
	int count=0;
	int count_true=1;
	Leader leader;
	
	public RequestThread(NodeInfo node) throws IOException {
		super();
		nodeinfo = node;
		ds=new DatagramSocket();
		group = InetAddress.getByName("228.5.6.7");
		mapper = new ObjectMapper();
		message=new Message();
		reqRPC= new RequestRPC();
		reqRPC.setCandidate_id(nodeinfo.getCandidateID());
		reqRPC.setTerm(nodeinfo.getCurrentTerm());
		reqRPC.setRequest("VOTE_REQUEST");
		if (node.getLog().size()>0) {
			int size = node.getLog().size();
			reqRPC.setLastLogindex(size-1);
			reqRPC.setLastlogTerm(node.getLog().get(size-1).getTerm());
			
		}else {
			reqRPC.setLastLogindex(-1);
			reqRPC.setLastlogTerm(0);
		}
		message.setRequestRPC(reqRPC);
		message.setTerm(nodeinfo.getCurrentTerm());
		message.setSender_name(nodeinfo.getCandidateID());
		message.setRequest("VOTE_REQUEST");
//		ds.setSoTimeout(node.getElectionTimeOut());
		count=1;
		count_true=1;
	}

	public void run() {
		
		ReplyMessage reply = new ReplyMessage();
		
		if (nodeinfo.getState().equals("C")){
		byte[] buf = new byte[2024];
		String json="";
	
		count=1;
		count_true=1;
		try {
			 json = mapper.writeValueAsString(message);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buf = json.getBytes();
		
		DatagramPacket DpSend =new DatagramPacket(buf, buf.length, group, 1234);
		
		try {
			
			ds.send(DpSend);
			
			//start the election timer so that if majority is not received till election timeout it will start election again with new term
			
			while(nodeinfo.getState().equals("C")) {
				byte[] receivevotes = new byte[65535];
				
				DpSend = new DatagramPacket(receivevotes, receivevotes.length);
				ds.receive(DpSend);
				
				reply = mapper.readValue(receivevotes, reply.getClass());
				
				System.out.println("Received from follower under vote request");
//			    System.out.println("Vote received = "+received);
			    if (reply.getReplyType().equals("VOTE_REPLY")) {
			    	if (reply.getVotereply().isVote() && reply.getVotereply().getCurrentTerm()==nodeinfo.getCurrentTerm()) {
			    		count_true+=1;
			    	}
			    	
			        if(reply.getVotereply().getCurrentTerm()>nodeinfo.getCurrentTerm()) {
				    	nodeinfo.setState("F");
				    	nodeinfo.setVotedFor(-1);
				    	// cancel  election timer
				    	break;
				    }
			        
//				    System.out.println("vote reply received");
				    if (count_true==3){
//				    	System.out.println("count==3 for candidate node");
				    	break;
				    	
				    }
			    }
			    
			
				

			    
			   
//			        //majority vote received
//			        if (received.equals("TRUE")) {
//			        	// change to leader
//			        	//start sending heartbeat at listener ports
//			        	count_true+=1;
//			        	  	
//			        }
			        
			}
			
			try {
				if (nodeinfo.getState().equals("C")) {
					nodeinfo.setState("L");
		    		//save nodeinfo in json
		    		nodeinfo.setCurrentLeader(nodeinfo.getCandidateID());
		    		leader = new Leader(nodeinfo);
					leader.runHeartBeats();
				}
	    		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Binding Leader"+e.getMessage());
			}
	        
		}

	catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("close");
		}
		}
	}
	
	public int getCount() {
		return count;
	}
	
	public int getCountTrue() {
		return count_true;
	}
	
	public void socketClose() {
		if(leader!=null) {
			System.out.println("stop here");
			leader.cancelTimer();
		}
		if(!this.ds.isClosed()) {
			this.ds.close();
		}
		
	}
}
