package com.raftProject.raftImplementation;
import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class saveNodeInfo extends Thread {
	
	NodeInfo nodeinfo=null;
	
	public saveNodeInfo(NodeInfo node) {
		nodeinfo = node;
	}
	
	public void run() {
		File file = new File("/data/info/nodeinfo.json");
		ObjectMapper mapper1 = new ObjectMapper();
		
		try {
            // Serialize Java object info JSON file.
            mapper1.writeValue(file, nodeinfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
