package com.raftProject.raftImplementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StartApplicationProcess implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		
		// main class which will run when we start server
		
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		NodeInfo nodeSetup = new NodeInfo();
		ObjectMapper mapper1 = new ObjectMapper();
		Message message = new Message();
		
		
		
		File file = new File("/data/info/nodeinfo.json");
		
		if(file.exists() && !file.isDirectory()){
			try {
			nodeSetup= mapper1.readValue(file,nodeSetup.getClass());
			nodeSetup.setState("F");
			}catch(IOException e) {
				System.out.println("json not found");
			}
        }else {
        	//Election Timeout
    		int elecTimeout = ThreadLocalRandom.current().nextInt(300,600);
    		nodeSetup.setElectionTimeOut(elecTimeout);
    		
    		String cand_id = System.getenv("CANDIDATE_ID");
    		nodeSetup.setCandidateID(cand_id);
    		
    		String cand_key = System.getenv("CANDIDATE_KEY");
    		nodeSetup.setCandidateKey(Integer.parseInt(cand_key));
    		
    		nodeSetup.setCurrentTerm(0);
    		nodeSetup.setState("F");//not used anywhere but if used need to check the condition when server restarts
    		nodeSetup.setHeartBeatInterval(100);
    		nodeSetup.setVotedFor(-1);
    		nodeSetup.setCommitIndex(-1);
    		nodeSetup.setLastApplied(-1);
    		
    		 try {
    	            // Serialize Java object info JSON file.
    	            mapper1.writeValue(file, nodeSetup);
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
        }
		
		if (nodeSetup.getLog()==null) {
			nodeSetup.setLog(new ArrayList<Log>());
		}
		
		
		
		//initialize nextIndex
		if (nodeSetup.getLog().size()==0) {
			Arrays.fill(nodeSetup.getNextIndex(),0);
		}else {
			Arrays.fill(nodeSetup.getNextIndex(),nodeSetup.getLog().size());
		}
		
		//initialize matchIndex
		if (nodeSetup.getLog().size()==0) {
			Arrays.fill(nodeSetup.getMatchIndex(),-1);
		}else {
			Arrays.fill(nodeSetup.getMatchIndex(),nodeSetup.getLog().size()-1);
		}
		
		message.setSender_name(nodeSetup.getCandidateID());
		message.setTerm(nodeSetup.getCurrentTerm());
		
		System.out.println("main thread of node--> "+nodeSetup.getCandidateID()+"started");
		//start thread to listen to heart Beats
		Controller controller;
		try {
			controller = new Controller(nodeSetup, message);
			Thread thread_controller = new Thread(controller);
			thread_controller.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	        
		
	}

}
