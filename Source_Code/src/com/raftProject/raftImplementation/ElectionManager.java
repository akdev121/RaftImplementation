package com.raftProject.raftImplementation;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class ElectionManager implements Runnable{
	
	ListenThread server1=null;
	NodeInfo nodeinfo = null;
	boolean exec;
	private RequestThread reqthread=null;
	
	public ElectionManager(ListenThread server, NodeInfo nodeinfo) {
		this.server1 = server;
		this.nodeinfo = nodeinfo;
		this.exec=true;
	}

	@Override
	public void run() {
//		System.out.println("Thread receive started election manager-->"+ Thread.currentThread().getName());
		while(this.exec) {
			Instant time=server1.getTime();
			if (time!=null) {
				if ((Duration.between(server1.getTime(),Instant.now()).toMillis()>nodeinfo.getElectionTimeOut() && nodeinfo.getState().equals("F")) || nodeinfo.isDoTimeout()) {
					
//					System.out.println("election start");
					nodeinfo.setState("C");
					nodeinfo.setDoTimeout(false);
					try {
						startElection();
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					break;
				}
			}
			
			
			}
		
		
		
		
		
	}
	
	public void startElection() throws IOException, InterruptedException {
//		System.out.println("timeout of "+nodeinfo.getCandidateID()+" occurred");
		 //change state from follower to candidate
		nodeinfo.setState("C");
		 //increase the term number
		nodeinfo.setCurrentTerm(nodeinfo.getCurrentTerm()+1);
		 //start election
		
		//vote for itself
		nodeinfo.setVotedFor(nodeinfo.getCurrentTerm());
		
		this.reqthread = new RequestThread(nodeinfo);
		Thread thread_request= new Thread(this.reqthread);
		thread_request.start();
		
//		Thread.sleep(50);
//		
//		int count = reqthread.getCount();
//		int count_true = reqthread.getCountTrue();
//		
//		if (count_true>count/2) {
//		System.out.println("starting heartbeats......");
//    	try {
//    		nodeinfo.setState("L");
//    		//save nodeinfo in json
//    		nodeinfo.setCurrentLeader(nodeinfo.getCandidateID());
//			new Leader(nodeinfo).runHeartBeats();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
			
			
		
	}
	
	public void setExec() {
		if(this.reqthread!=null) {
			this.reqthread.socketClose();
		}
		
		this.exec=false;
	}
	
	

}
