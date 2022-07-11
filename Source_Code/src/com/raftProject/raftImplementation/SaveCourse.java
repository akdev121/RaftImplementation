package com.raftProject.raftImplementation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SaveCourse extends Thread {
	
	NodeInfo nodeinfo=null;
	

	public SaveCourse(NodeInfo nodeinfo) {
		this.nodeinfo = nodeinfo;
	}
	
	public void run() {
		BufferedWriter out;
		int last_index=nodeinfo.getLastApplied();
		
		if(nodeinfo.getLastApplied()<nodeinfo.getCommitIndex()) {
			for(int i=last_index;i<nodeinfo.getCommitIndex();i++) {
				try {
					out = new BufferedWriter(new FileWriter("/data/info"+"/userInput.txt",true));
					out.write(nodeinfo.getLog().get(i+1).getValue());
					out.newLine();
					out.close();
					nodeinfo.setLastApplied(nodeinfo.getLastApplied()+1);
				}catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("saving error");
				}
				
			}
		}
		
	}
	
	
}
