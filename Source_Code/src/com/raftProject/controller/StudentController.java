package com.raftProject.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raftProject.raftImplementation.Controller_Req;
import com.raftProject.raftImplementation.NodeInfo;
import com.raftProject.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	private String leader;

	@GetMapping("/list")
	public String listCustomer(HttpServletRequest request,Model theModel) throws IOException {
		
		//String container = System.getenv("ENV_SERVICE");
		getNodeDetails();
		String container = System.getenv("CANDIDATE_ID");
		
		if (leader.equals(container)) {
			String path = request.getServletContext().getRealPath("/");
			
			List<String> courses = studentService.getStudents(path);
			
			theModel.addAttribute("courses",courses);
			
			
	
			return "list-courses";
		}
		else {
			return "error_page";
		}

		
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		

			String course = "";
			
			theModel.addAttribute("customer",course);
			
			return "student-form";
		
	}
	
	@RequestMapping("/saveCourse")
	public String saveCustomer(HttpServletRequest request) throws IOException {
		String path = request.getServletContext().getRealPath("/");
		String course = request.getParameter("course");
		
//		String container = System.getenv("ENV_SERVICE");
//		System.out.println("Container is = "+container);
		
//		if (container.equals("leader")){
//			String uri1 = "http://worker1:8080/student-course-reg/student/saveCourseEndPoint?course="+course;
//			RestTemplate template = new RestTemplate();
//			String result1 = template.getForObject(uri1, String.class);
//			String uri2 = "http://worker2:8080/student-course-reg/student/saveCourseEndPoint?course="+course;
//			RestTemplate template2 = new RestTemplate();
//			String result2 = template2.getForObject(uri2, String.class);
//			System.out.println(result1);
//			System.out.println(result2);
			studentService.saveCourse(course,path);
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "redirect:/student/list";
//		}
	
//		else {
//			return "error_page";
//		}
		
	}
//	@GetMapping("/saveCourseEndPoint")
//	public @ResponseBody String saveCustomerApi(@RequestParam String course) {
//		//String course = request.getParameter("course");
//		String path="";
//		try {
//			studentService.saveCourse(course,path);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(course);
//		System.out.println("API IS Called");
//		return "SUCCESS";
//	}
	
	@RequestMapping(value = "/getCourse", method = RequestMethod.GET)
	@ResponseBody
	public  String getCourses() {
		String path="";
		List<String> courses = studentService.getStudents(path);
		String json=courses.toString();
		return json;
	}
	
	public void getNodeDetails() throws IOException {
		Controller_Req req = new Controller_Req();
		ObjectMapper mapper = new ObjectMapper();
		Controller_Req reply = new Controller_Req();
		req.setSender_name("Controller");
		req.setRequest("LEADER_INFO");
		
		String container = System.getenv("CANDIDATE_ID");
		
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
			
			byte[] receive = new byte[65535];
			
			DpSend = new DatagramPacket(receive, receive.length);
			
			ds.receive(DpSend);
			
			reply = mapper.readValue(receive, reply.getClass());
			
			
			
			
		}catch(IOException e) {
			System.out.println("execution error");
		}
		
		leader = reply.getValue();
	}
	
}
