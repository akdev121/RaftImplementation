package com.raftProject.raftImplementation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AppendReplyReceive implements Runnable {
	
	
	DatagramSocket socket = null;
	DatagramPacket DpReceive = null;
	NodeInfo nodeSetup=null;
	ObjectMapper mapper=null;
	InetAddress address=null;
	byte[] receive=null;
//	Message message= null;
	
	public AppendReplyReceive(NodeInfo nodeSetup1,DatagramSocket socket1) throws IOException{
		super();
		this.socket = socket1;
		this.nodeSetup = nodeSetup1;
		this.mapper = new ObjectMapper();
//		this.message = message;
	}
	
	public void run() {
		ReplyMessage reply = new ReplyMessage();
		
		while(true) {
		
				try {
					
//					System.out.println("leader is at ----------->"+ nodeSetup.getCandidateID());
					byte[] receive = new byte[65535];
					
					DpReceive = new DatagramPacket(receive, receive.length);
					
//					System.out.println("Received from follower");
					socket.receive(DpReceive);
					
					reply = mapper.readValue(receive, reply.getClass());
					
					if (nodeSetup.getState().equals("L") && nodeSetup.getCurrentTerm()==reply.getAppendreply().getCurrentTerm()) {
						if(!reply.getAppendreply().isSucess()) {
//							System.out.println("Received from follower FALSE");
							int key = reply.getAppendreply().getCandidate_key();
//							System.out.println("key="+key);
							if(nodeSetup.getNextIndex()[key]>0) {
								nodeSetup.getNextIndex()[key]=nodeSetup.getNextIndex()[key]-1;
							}	
						}else if(reply.getAppendreply().isSucess()){
							int key = reply.getAppendreply().getCandidate_key();
							if (nodeSetup.getNextIndex()[key]<nodeSetup.getLog().size()) {
								nodeSetup.getNextIndex()[key]=nodeSetup.getNextIndex()[key]+1;
								nodeSetup.getMatchIndex()[key]=nodeSetup.getMatchIndex()[key]+1;
//								System.out.println("enter here success");
							}
							
						}
						//commit log entries
						 Map<Integer, Integer> mp = new HashMap<Integer, Integer>();
						 for (int i=0;i<5;i++) {
							 if (mp.containsKey(nodeSetup.getMatchIndex()[i])) {
								 mp.put(nodeSetup.getMatchIndex()[i], mp.get(nodeSetup.getMatchIndex()[i]) + 1);
							 }else {
								 mp.put(nodeSetup.getMatchIndex()[i], 1);
							 }
						 }
						 int max = 0;
						 
						 for (Map.Entry<Integer, Integer> entry : mp.entrySet())
					        {	
							 	if (entry.getValue()>=3){
							 		max = entry.getKey();
							 	}
					           
					        }
						 
						 if (max>nodeSetup.getCommitIndex()) {
							//commit from commit_index to max 
							 nodeSetup.setCommitIndex(max);
							 System.out.println("Commit happens");
							 new saveNodeInfo(nodeSetup).start();
							 new SaveCourse(nodeSetup).start();
						 }
						
						 //If lastapplied< commitindex
						 //apply till commit index and increase lastapplied
						
					}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("close");
				break;
			}
			
			
		}
	}
	
	public void closeSocket() {
		if(!socket.isClosed()) {
			this.socket.close();
		}
		
	}
	
}
