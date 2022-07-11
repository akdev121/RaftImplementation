package com.raftProject.raftImplementation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.TimerTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SendHeartBeats extends TimerTask {
	
	private DatagramSocket ds;
	private InetAddress group ;
	private ObjectMapper mapper;
	private Message msg ;
	public NodeInfo nodeinfo;
	
	public SendHeartBeats(DatagramSocket ds, InetAddress group, ObjectMapper mapper, Message message, NodeInfo nodein) {
		super();
		this.ds = ds;
		this.group = group;
		this.mapper = mapper;
		this.msg = message;
		this.nodeinfo=nodein;
	}



	@Override
	public void run() {
		
		if (nodeinfo.getState().equals("L")) {
			
			
			int lastlogindex = nodeinfo.getLog().size()-1;
			int[] nextIndex = nodeinfo.getNextIndex();
			
			for(int k=0; k<5;k++) {
				msg.getAppendRPC().getEntrylist().get(k).clear();
			}
			
			for (int i=0; i<5;i++) {
				if (lastlogindex>=nextIndex[i]) {
					for (int j=nextIndex[i]; j<=lastlogindex;j++) {
//						System.out.println("adding logs");
						msg.getAppendRPC().getEntrylist().get(i).add(nodeinfo.getLog().get(j));
					}
					
				}
				
			}
//			System.out.println("size of entrylist"+msg.getAppendRPC().getEntrylist().size());
//			System.out.println("size of entrylist 0"+msg.getAppendRPC().getEntrylist().get(0).size());
//			System.out.println("content of entrylist 0"+msg.getAppendRPC().getEntrylist().get(0));
//			
			int[] prevIndex = new int[5];
			int[] prevTerm = new int[5];
			
			for(int i=0; i<5;i++) {
				if (nextIndex[i]==0) {
					prevIndex[i]=-1;
					prevTerm[i]=nodeinfo.getCurrentTerm()-1;
				}else {
					prevIndex[i]=nextIndex[i]-1;
					prevTerm[i]=nodeinfo.getLog().get(prevIndex[i]).getTerm();
				}
			}
			
	
			msg.getAppendRPC().setPrevLogIndexArr(prevIndex);
			msg.getAppendRPC().setPrevLogTermArr(prevTerm);
		
			msg.getAppendRPC().setEntries(nodeinfo.getLog());
			
			msg.getAppendRPC().setLeaderCommit(nodeinfo.getCommitIndex());
			
			byte[] buf = new byte[65535];
			String json="";
			try {
				 json = mapper.writeValueAsString(msg);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			buf = json.getBytes();
			DatagramPacket DpSend =new DatagramPacket(buf, buf.length, group, 1234);
			try {
//				System.out.println("sending data-->"+Thread.currentThread().getName());
				ds.send(DpSend);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
