package com.raftProject.raftImplementation;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Leader {
	
	NodeInfo node = null;
	Message message = null;
	AppendRPC appendRPC=null;
	private Timer timer;
	private TimerTask task;

	public Leader(NodeInfo nodeinfo) {
		super();
		this.node = nodeinfo;
		message = new Message();
		appendRPC = new AppendRPC();
		message.setTerm(node.getCurrentTerm());
		message.setSender_name(node.getCandidateID());
		message.setRequest("APPEND_RPC");
	
		appendRPC.setLeader_id(nodeinfo.getCandidateID());
		appendRPC.setTerm(nodeinfo.getCurrentTerm());
		message.setAppendRPC(appendRPC);
		//node.setCurrentLeader(node.getCandidateID());
	}



	public void runHeartBeats() throws IOException {
		DatagramSocket ds = new DatagramSocket();
        //InetAddress ip = InetAddress.getLocalHost();
//        System.out.println("Local host of leader"+InetAddress.getLocalHost().getHostName() );
        InetAddress group = InetAddress.getByName("228.5.6.7");
        
        ObjectMapper mapper = new ObjectMapper();
        
      //initialize nextIndex
      		if (node.getLog().size()==0) {
      			Arrays.fill(node.getNextIndex(),0);
      		}else {
      			Arrays.fill(node.getNextIndex(),node.getLog().size());
      		}
      		
      	//initialize matchIndex
    		if (node.getLog().size()==0) {
    			Arrays.fill(node.getMatchIndex(),-1);
    		}else {
    			Arrays.fill(node.getMatchIndex(),node.getLog().size()-1);
    		}
        
        for (int i=0;i<5;i++) {
        	ArrayList<Log> log = new ArrayList<Log>();
        	message.getAppendRPC().getEntrylist().add(log);
        }
        
        message.getAppendRPC().setLeaderCommit(node.getCommitIndex());
        
        timer = new Timer();
        task =  new SendHeartBeats(ds, group, mapper, message,node);//https://www.geeksforgeeks.org/java-util-timer-class-java/
        System.out.println("processing Node-->"+node.getCandidateID());
        
        timer.schedule(task, 0,node.getHeartBeatInterval()); //heart beat timeout  
	}
	
	public void cancelTimer() {
		task.cancel();
	}
}
