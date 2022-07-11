package com.raftProject.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raftProject.dao.StudentDAO;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentDAO customerDAO;

	@Override
	public List<String> getStudents(String path) {
		
		return customerDAO.getStudents(path);
	}

	@Override
	public void saveCourse(String course, String path) throws IOException {
		
		customerDAO.saveCourse(course,path);
		
	}
}
