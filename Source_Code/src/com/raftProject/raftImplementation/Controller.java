package com.raftProject.raftImplementation;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Controller extends Thread {
	Controller_Req msg=null;
	DatagramSocket socket =null;
	DatagramSocket socket2 =null;
	InetAddress group = null;
	ObjectMapper mapper=null;
	NodeInfo nodeinfo=null;
	DatagramPacket DpReceive = null;
	Message message=null;
	
	Thread t_listen=null;
	Thread e_manager=null;
	ListenThread listen = null;
	ElectionManager manager =null;
	
	AppendReplyReceive receiver = null;
	Thread treceiver = null;
	
	public Controller(NodeInfo nodeinf, Message mess) throws IOException {
		msg= new Controller_Req();
		message = mess;
		socket = new DatagramSocket(5555);
		socket2 = new DatagramSocket(1235);
		InetAddress group = InetAddress.getLocalHost();
		nodeinfo=nodeinf;
	    mapper = new ObjectMapper();
	    
	    listen = new ListenThread(nodeinfo);
		t_listen = new Thread(listen);
		
		manager= new ElectionManager(listen, nodeinfo);
		e_manager = new Thread(manager);
		
		receiver = new AppendReplyReceive(nodeinfo, socket2);
		treceiver = new Thread(receiver);
		
	}
	
	public void run() {
		start_listenThread();
		
		while (true) {
			try {
				byte[] receive1 = new byte[65535];
	        	// create a DatgramPacket to receive the data.
	            DpReceive = new DatagramPacket(receive1, receive1.length);
	            socket.receive(DpReceive);  
	            
	            
	            //System.out.println(msg.toString());
	            msg= mapper.readValue(receive1, msg.getClass());
	            System.out.println("controller:-" + data(receive1));
	           
	            
//	            System.out.println(msg.toString()+"got it here");
	            
	            if (msg.getSender_name().equals("Controller")) {
	            
	            	if (msg.getRequest().equals("LEADER_INFO")) {
	            		
	            		msg.setSender_name(nodeinfo.getCandidateID());
	            	 	msg.setTerm(nodeinfo.getCurrentTerm());
	            	 	msg.setKey("LEADER");
	            	 	msg.setValue(nodeinfo.getCurrentLeader());
	            		send_message(socket,msg);
	            		
	            		
	            	}
	            	if (msg.getRequest().equals("CONVERT_FOLLOWER")) {
	            		//convert the node to follower state
	            		//stop heartbeat thread and start listener thread
	            		if (!nodeinfo.getState().equals("F")){
	            			System.out.println("convert leader to follower");
	            			nodeinfo.setState("F");
	            		}
	            		
	            		
	            	}
	            	
	            	if(msg.getRequest().equals("TIMEOUT")) {
	            		//in the listener thread do the timeout , if node is leader then start election with new term number
	            		
	            		nodeinfo.setDoTimeout(true);
	            	}
	            	
	            	if(msg.getRequest().equals("SHUTDOWN")) {
	            		// shutdown all the thread and main thread
	            		save_nodeState(nodeinfo);
	            		manager.setExec();
	            		listen.socketClose();
	            		receiver.closeSocket();
	            		System.exit(0);
	            		
	            	}
	            	
	            	if(msg.getRequest().equals("STORE")) {
	            		
	            		if (nodeinfo.getState().equals("L")){
	            			System.out.println("STORE Command to leader");
	            			Log log = new Log();
		            		log.setTerm(nodeinfo.getCurrentTerm());
		            		log.setKey(msg.getKey());
		            		log.setValue(msg.getValue());
		            		
		            		nodeinfo.getLog().add(log);
		            		
		            		nodeinfo.getMatchIndex()[nodeinfo.getCandidateKey()] = nodeinfo.getMatchIndex()[nodeinfo.getCandidateKey()]+1;
		            		
		            		for (int i=0; i<5; i++) {
		            			nodeinfo.getNextIndex()[i]=nodeinfo.getNextIndex()[i]+1;
		            		}
//		            		if (nodeinfo.getLog()==null){
//		            			ArrayList<Log> newLog = new ArrayList<Log>();
//		            			newLog.add(log);
//		            			nodeinfo.setLog(newLog);
//		            		}else {
//		            			nodeinfo.getLog().add(log);
//		            		}
		            		
		            		save_nodeState(nodeinfo);
		    
	            		}else {
	            			System.out.println("STORE Command to follower");
	            			msg.setSender_name(nodeinfo.getCandidateID());
		            	 	msg.setTerm(nodeinfo.getCurrentTerm());
		            	 	msg.setRequest("LEADER_INFO");
		            	 	msg.setKey("LEADER");
		            	 	msg.setValue(nodeinfo.getCurrentLeader());
	            			send_message(socket,msg);
	            		}
	            		
	            		
	            	}
	            	
	            	if (msg.getRequest().equals("RETRIEVE")) {
	            		if (nodeinfo.getState().equals("L")){
	            			msg.setSender_name(nodeinfo.getCandidateID());
	            			msg.setTerm(nodeinfo.getCurrentTerm());
	            			msg.setRequest("RETRIEVE");
	            			msg.setKey("COMMITED_LOGS");
	            			Log[] logs1 = new Log[nodeinfo.getLog().size()];
	            			Log[] logs2 = new Log[nodeinfo.getLog().size()];
	            			logs1 = nodeinfo.getLog().toArray(logs1);
	            			
	            			for(int i=0;i<=nodeinfo.getCommitIndex();i++) {
	            				logs2[i]=logs1[i];
	            			}
	            			
	            			ObjectMapper mapper1 = new ObjectMapper();
	            			String arrayToJson = mapper1.writeValueAsString(logs2);
	            			
	            			msg.setValue(arrayToJson);
	            			send_message(socket,msg);
	            			
	            		}else {
	            			msg.setSender_name(nodeinfo.getCandidateID());
		            	 	msg.setTerm(nodeinfo.getCurrentTerm());
		            	 	msg.setRequest("LEADER_INFO");
		            	 	msg.setKey("LEADER");
		            	 	msg.setValue(nodeinfo.getCurrentLeader());
	            			send_message(socket,msg);
	            		}
	            	}
	            	
	            }
	            // Clear the buffer after every message.
	            receive1 = new byte[65535];
	            
			}catch (IOException e) {
				System.out.println(e.getMessage());
			}
		
		}
	}
	
	 private void save_nodeState(NodeInfo nodeinfo) {
		// TODO Auto-generated method stub
		 
		 File file = new File("/data/info/nodeinfo.json");
		 ObjectMapper mapper1 = new ObjectMapper();
		 try {
	            // Serialize Java object info JSON file.
	            mapper1.writeValue(file, nodeinfo);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
	}

	private void send_message(DatagramSocket socket,Controller_Req msg) throws IOException {
		 
	
// 		System.out.println("inside"+msg.toString());
 		//DpReceive.getPort(),DpReceive.getAddress()
 		int port = DpReceive.getPort();
 		InetAddress address = DpReceive.getAddress();
// 		System.out.println("address of controller"+ address);
 		
 		byte[] buf = new byte[2024];
 		String json="";
 		try {
 			 json = mapper.writeValueAsString(msg);
 		} catch (JsonProcessingException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		
 		buf = json.getBytes();
 		DatagramPacket DpSend =new DatagramPacket(buf, buf.length, address, port);
 		
// 		System.out.println("sending data from the controller-->"+Thread.currentThread().getName());
			socket.send(DpSend);
	}

	public static StringBuilder data(byte[] a)
	    {
	        if (a == null)
	            return null;
	        StringBuilder ret = new StringBuilder();
	        int i = 0;
	        while (a[i] != 0)
	        {
	            ret.append((char) a[i]);
	            i++;
	        }
	        return ret;
	    }
	
	 public void start_listenThread() {
		 
		 t_listen.start();
		 e_manager.start();
		 treceiver.start();
	 }
	
	
}
