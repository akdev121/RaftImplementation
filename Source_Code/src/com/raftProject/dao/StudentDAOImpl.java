package com.raftProject.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raftProject.raftImplementation.Controller_Req;

@Component
public class StudentDAOImpl implements StudentDAO {
	
	@Override
	public List<String> getStudents(String path) {
		
		List<String> words = new ArrayList<String>();
		BufferedReader reader;

		try {
			Path path1 = Paths.get("/data/info"+"/userInput.txt");
			if (Files.exists(path1)) {
				reader = new BufferedReader(new FileReader("/data/info"+"/userInput.txt"));
				String line;
				try {
					while ((line = reader.readLine()) != null) {
					    words.add(line);
					}
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return words;
	}

	@Override
	public void saveCourse(String course,String path) throws IOException {
		
		Controller_Req req = new Controller_Req();
		ObjectMapper mapper = new ObjectMapper();
		Controller_Req reply = new Controller_Req();
		req.setSender_name("Controller");
		req.setRequest("STORE");
		req.setKey("Save");
		req.setValue(course);
		
		
		DatagramSocket ds = new DatagramSocket();
		InetAddress address = InetAddress.getLocalHost();
		
		byte[] buf = new byte[2024];
		String json="";
		
		try {
			 json = mapper.writeValueAsString(req);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buf = json.getBytes();
		
		DatagramPacket DpSend =new DatagramPacket(buf, buf.length, address, 5555);
		
		try {

			ds.send(DpSend);
			
//			byte[] receive = new byte[65535];
//			
//			DpSend = new DatagramPacket(receive, receive.length);
//			
//			ds.receive(DpSend);
//			
//			reply = mapper.readValue(receive, reply.getClass());
//			
			
			
			
		}catch(IOException e) {
			System.out.println("execution error");
		}
		ds.close();
		
	}
		
	
}
